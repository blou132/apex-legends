#!/usr/bin/env python3
"""Targeted APK ELF/JNI inventory. Does not extract native libraries."""

import hashlib
import json
import struct
import sys
import zipfile
from pathlib import Path

EXPECTED_APK_SHA256 = "2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0"
TARGET_SYMBOLS = (
    "JNI_OnLoad",
    "Java_com_epicgames_ue4_GameActivity_nativeResumeMainInit",
    "nativeResumeMainInit",
)
TARGET_STRINGS = TARGET_SYMBOLS + (
    "com/epicgames/ue4/GameActivity",
    "com.epicgames.ue4.GameActivity",
    "()V",
    "RegisterNatives",
)


def u16(data, offset):
    return struct.unpack_from("<H", data, offset)[0]


def u32(data, offset):
    return struct.unpack_from("<I", data, offset)[0]


def u64(data, offset):
    return struct.unpack_from("<Q", data, offset)[0]


def cstring(data, offset):
    if offset < 0 or offset >= len(data):
        return ""
    end = data.find(b"\0", offset)
    if end < 0:
        return ""
    return data[offset:end].decode("utf-8", "replace")


def vaddr_to_offset(loads, value):
    for load in loads:
        if load[0] <= value < load[0] + load[2]:
            return load[1] + value - load[0]
    return None


def gnu_symbol_count(data, offset):
    nbuckets, symoffset, bloom_size, _ = struct.unpack_from("<IIII", data, offset)
    buckets_offset = offset + 16 + bloom_size * 8
    buckets = struct.unpack_from(f"<{nbuckets}I", data, buckets_offset)
    chains_offset = buckets_offset + nbuckets * 4
    maximum = symoffset
    for bucket in buckets:
        if bucket < symoffset:
            continue
        index = bucket
        while True:
            chain = u32(data, chains_offset + (index - symoffset) * 4)
            index += 1
            if chain & 1:
                break
        maximum = max(maximum, index)
    return maximum


def parse_elf(data):
    if data[:4] != b"\x7fELF" or data[4] != 2 or data[5] != 1:
        raise ValueError("not ELF64 little-endian")
    machine = u16(data, 18)
    phoff, shoff = u64(data, 32), u64(data, 40)
    phentsize, phnum = u16(data, 54), u16(data, 56)
    shentsize, shnum, shstrndx = u16(data, 58), u16(data, 60), u16(data, 62)

    loads = []
    for index in range(phnum):
        base = phoff + index * phentsize
        p_type = u32(data, base)
        if p_type == 1:
            p_offset, p_vaddr = u64(data, base + 8), u64(data, base + 16)
            p_filesz = u64(data, base + 32)
            loads.append((p_vaddr, p_offset, p_filesz))

    raw_sections = []
    for index in range(shnum):
        base = shoff + index * shentsize
        raw_sections.append({
            "name_offset": u32(data, base),
            "type": u32(data, base + 4),
            "flags": u64(data, base + 8),
            "address": u64(data, base + 16),
            "offset": u64(data, base + 24),
            "size": u64(data, base + 32),
            "link": u32(data, base + 40),
            "entry_size": u64(data, base + 56),
        })
    shstr = raw_sections[shstrndx]
    shstr_data = data[shstr["offset"]:shstr["offset"] + shstr["size"]]
    sections = []
    for section in raw_sections:
        section = dict(section)
        section["name"] = cstring(shstr_data, section.pop("name_offset"))
        sections.append(section)

    dynamic_section = next((s for s in sections if s["type"] == 6), None)
    tags = {}
    if dynamic_section:
        for offset in range(dynamic_section["offset"],
                            dynamic_section["offset"] + dynamic_section["size"], 16):
            tag, value = u64(data, offset), u64(data, offset + 8)
            if tag == 0:
                break
            tags.setdefault(tag, []).append(value)

    strtab_vaddr = tags.get(5, [None])[0]
    symtab_vaddr = tags.get(6, [None])[0]
    strtab_offset = vaddr_to_offset(loads, strtab_vaddr) if strtab_vaddr is not None else None
    symtab_offset = vaddr_to_offset(loads, symtab_vaddr) if symtab_vaddr is not None else None
    symbol_count = 0
    if 4 in tags:
        hash_offset = vaddr_to_offset(loads, tags[4][0])
        symbol_count = u32(data, hash_offset + 4)
    elif 0x6FFFFEF5 in tags:
        hash_offset = vaddr_to_offset(loads, tags[0x6FFFFEF5][0])
        symbol_count = gnu_symbol_count(data, hash_offset)

    symbol_matches = {name: [] for name in TARGET_SYMBOLS}
    dynamic_names = set()
    if strtab_offset is not None and symtab_offset is not None:
        for index in range(symbol_count):
            base = symtab_offset + index * 24
            name = cstring(data, strtab_offset + u32(data, base))
            if name:
                dynamic_names.add(name)
            if name in symbol_matches:
                symbol_matches[name].append({
                    "index": index,
                    "value": f"0x{u64(data, base + 8):x}",
                    "size": u64(data, base + 16),
                    "bind": u16(data[base + 4:base + 5] + b"\0", 0) >> 4,
                    "type": data[base + 4] & 0xF,
                    "section_index": u16(data, base + 6),
                })

    dynstr = b""
    if strtab_offset is not None:
        strsz = tags.get(10, [0])[0]
        dynstr = data[strtab_offset:strtab_offset + strsz]
    needed = [cstring(dynstr, value) for value in tags.get(1, [])]
    soname_values = tags.get(14, [])
    soname = cstring(dynstr, soname_values[0]) if soname_values else None

    witnesses = []
    for target in TARGET_STRINGS:
        needle = target.encode() + b"\0"
        start = 0
        found = []
        while True:
            position = data.find(needle, start)
            if position < 0:
                break
            section = next((s for s in sections if s["type"] != 8
                            and s["offset"] <= position < s["offset"] + s["size"]), None)
            found.append({
                "file_offset": f"0x{position:x}",
                "section": section["name"] if section else "PT_LOAD_OR_UNKNOWN",
                "occurrence_type": "NULL_TERMINATED_EXACT_STRING",
            })
            start = position + 1
        if found:
            witnesses.append({"value": target, "count": len(found), "occurrences": found[:32]})

    init_value = tags.get(12, [None])[0]
    init_array_value = tags.get(25, [None])[0]
    init_array_size = tags.get(27, [0])[0]
    return {
        "elf_class": "ELF64",
        "machine": machine,
        "machine_name": "AARCH64" if machine == 183 else f"EM_{machine}",
        "soname": soname,
        "needed": needed,
        "dynamic_symbol_count": symbol_count,
        "dynamic_symbol_matches": symbol_matches,
        "string_witnesses": witnesses,
        "loader_metadata": {
            "dt_init": f"0x{init_value:x}" if init_value is not None else None,
            "dt_init_array": f"0x{init_array_value:x}" if init_array_value is not None else None,
            "dt_init_array_size": init_array_size,
            "dt_strtab": f"0x{strtab_vaddr:x}" if strtab_vaddr is not None else None,
            "dt_symtab": f"0x{symtab_vaddr:x}" if symtab_vaddr is not None else None,
            "has_dt_hash": 4 in tags,
            "has_gnu_hash": 0x6FFFFEF5 in tags,
        },
        "dynamic_loader_symbols": {
            "dlopen": "dlopen" in dynamic_names,
            "android_dlopen_ext": "android_dlopen_ext" in dynamic_names,
        },
    }


def main():
    if len(sys.argv) != 3:
        raise SystemExit("usage: exporter.py BASE_APK OUTPUT_JSON")
    apk_path, output_path = Path(sys.argv[1]), Path(sys.argv[2])
    apk_hash = hashlib.sha256(apk_path.read_bytes()).hexdigest().upper()
    if apk_hash != EXPECTED_APK_SHA256:
        raise SystemExit(f"unexpected APK SHA256: {apk_hash}")
    libraries = []
    with zipfile.ZipFile(apk_path) as archive:
        entries = sorted((entry for entry in archive.infolist()
                          if entry.filename.startswith("lib/arm64-v8a/")
                          and entry.filename.endswith(".so")), key=lambda item: item.filename)
        if len(entries) != 17:
            raise SystemExit(f"expected 17 ARM64 libraries, got {len(entries)}")
        for entry in entries:
            with archive.open(entry) as stream:
                data = stream.read()
            parsed = parse_elf(data)
            parsed.update({
                "filename": Path(entry.filename).name,
                "zip_path": entry.filename,
                "compressed_size": entry.compress_size,
                "uncompressed_size": entry.file_size,
                "sha256": hashlib.sha256(data).hexdigest().upper(),
            })
            libraries.append(parsed)
    result = {
        "phase": "Phase13",
        "apk": {"size": apk_path.stat().st_size, "sha256": apk_hash},
        "native_library_count": len(libraries),
        "libraries": libraries,
    }
    output_path.parent.mkdir(parents=True, exist_ok=True)
    output_path.write_text(json.dumps(result, indent=2, ensure_ascii=True) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
