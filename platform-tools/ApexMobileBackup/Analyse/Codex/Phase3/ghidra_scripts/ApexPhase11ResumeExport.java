// Targeted Phase11 nativeResumeMainInit callgraph export for Apex Mobile libUE4.so.
// @category ApexMobile

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileOptions;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.symbol.FlowType;
import ghidra.program.model.symbol.RefType;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.program.model.symbol.Symbol;
import ghidra.program.model.symbol.SymbolIterator;
import ghidra.util.task.TaskMonitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ApexPhase11ResumeExport extends GhidraScript {
    private static final long IMAGE_BASE = 0x100000L;
    private static final String JNI_NAME =
        "Java_com_epicgames_ue4_GameActivity_nativeResumeMainInit";
    private static final int DEFAULT_DEPTH = 5;
    private static final int MAX_FUNCTIONS = 160;
    private static final int MAX_DECOMPILE_LINES = 500;
    private static final long[] KNOWN_LUA_TARGETS = {
        0x49a8b54L, 0x49a9694L, 0x48ab4d0L, 0x6be427cL, 0x6be3f4cL
    };
    private static final String[] STARTUP_TERMS = {
        "pthread_create", "pthread_cond_wait", "pthread_cond_timedwait",
        "pthread_mutex_lock", "sem_wait", "futex", "event", "wait",
        "sleep", "runnable", "taskgraph", "gamethread", "renderthread",
        "app_cmd_resume", "app_cmd_init_window", "egl", "surface", "window",
        "gameinstance", "gameengine", "startgameinstance", "loadmap", "browse",
        "onlineSubsystem", "clientlaunch", "requestavatarserverlist",
        "openserverlist", "loginmgr"
    };

    private DecompInterface decompiler;
    private PrintWriter out;
    private Memory memory;

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("Phase11 local output directory is required");
        }
        if (currentProgram.getImageBase().getOffset() != IMAGE_BASE) {
            throw new IllegalStateException("Unexpected image base: " + currentProgram.getImageBase());
        }

        int maxDepth = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_DEPTH;
        File outputDir = new File(args[0]).getCanonicalFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Unable to create Phase11 output directory");
        }
        File outputFile = new File(outputDir, "phase11_resume_export.txt");
        out = new PrintWriter(outputFile, StandardCharsets.UTF_8.name());
        memory = currentProgram.getMemory();
        decompiler = new DecompInterface();
        decompiler.setOptions(new DecompileOptions());
        decompiler.openProgram(currentProgram);

        try {
            line("PROGRAM", currentProgram.getName());
            line("IMAGE_BASE", currentProgram.getImageBase().toString());
            line("JNI_NAME", JNI_NAME);
            line("DEX_DECLARATION", "public native nativeResumeMainInit()V");
            line("EXPECTED_JNI_SIGNATURE", "void(JNIEnv*, jobject)");

            List<Symbol> exactSymbols = findExactSymbols();
            line("EXACT_SYMBOL_COUNT", Integer.toString(exactSymbols.size()));
            for (Symbol symbol : exactSymbols) {
                line("EXACT_SYMBOL", symbol.getName(true), symbol.getAddress().toString(),
                    symbol.getSymbolType().toString(), symbol.getSource().toString(),
                    Boolean.toString(symbol.isExternal()));
            }
            Function root = resolveRoot(exactSymbols);
            String resolutionMethod = "EXACT_SYMBOL_FUNCTION";
            if (root == null) {
                root = resolveFromElfDynamicSymbolTable();
                resolutionMethod = "ELF_DYNSYM_ENTRY";
            }
            if (root == null) {
                root = resolveFromRelaNativeMethodTable();
                resolutionMethod = "ELF_RELA_JNI_NATIVE_METHOD";
            }
            if (root == null) {
                root = resolveFromNativeMethodTable();
                resolutionMethod = "JNI_NATIVE_METHOD_TABLE";
            }
            if (root == null) {
                Set<Function> registrationOwners = findJniNameCodeReferences();
                line("JNI_NAME_CODE_OWNER_COUNT", Integer.toString(registrationOwners.size()));
                for (Function owner : registrationOwners) {
                    exportFunction(owner, -1);
                }
                line("JNI_RESOLUTION", "NOT_RESOLVED");
                return;
            }

            line("JNI_RESOLUTION", resolutionMethod);
            line("JNI_FUNCTION", root.getName(true), root.getEntryPoint().toString(),
                elf(root.getEntryPoint()), root.getSignature().getPrototypeString());

            Map<Function, Integer> graph = buildGraph(root, maxDepth);
            line("CALLGRAPH_DEPTH", Integer.toString(maxDepth));
            line("CALLGRAPH_FUNCTION_COUNT", Integer.toString(graph.size()));
            writeEdges(graph);
            writeKnownTargetReachability(graph);

            List<Map.Entry<Function, Integer>> ordered = new ArrayList<>(graph.entrySet());
            ordered.sort(Comparator
                .comparingInt((Map.Entry<Function, Integer> entry) -> entry.getValue())
                .thenComparingLong(entry -> entry.getKey().getEntryPoint().getOffset()));
            for (Map.Entry<Function, Integer> entry : ordered) {
                exportFunction(entry.getKey(), entry.getValue());
            }
            println("PHASE11_RESUME_EXPORT_OK");
            println(outputFile.getAbsolutePath());
        } finally {
            out.close();
            decompiler.dispose();
        }
    }

    private List<Symbol> findExactSymbols() {
        List<Symbol> result = new ArrayList<>();
        SymbolIterator symbols = currentProgram.getSymbolTable().getSymbols(JNI_NAME);
        while (symbols.hasNext()) {
            Symbol symbol = symbols.next();
            if (JNI_NAME.equals(symbol.getName())) {
                result.add(symbol);
            }
        }
        return result;
    }

    private void writeContainingSymbolsAndFunctions() {
        String needle = "nativeresumemaininit";
        int symbolCount = 0;
        SymbolIterator symbols = currentProgram.getSymbolTable().getAllSymbols(true);
        while (symbols.hasNext()) {
            Symbol symbol = symbols.next();
            if (symbol.getName(true).toLowerCase(Locale.ROOT).contains(needle)) {
                symbolCount++;
                line("CONTAINING_SYMBOL", symbol.getName(true), symbol.getAddress().toString(),
                    symbol.getSymbolType().toString(), symbol.getSource().toString());
            }
        }
        line("CONTAINING_SYMBOL_COUNT", Integer.toString(symbolCount));

        int functionCount = 0;
        FunctionIterator functions = currentProgram.getFunctionManager().getFunctions(true);
        while (functions.hasNext()) {
            Function function = functions.next();
            if (function.getName(true).toLowerCase(Locale.ROOT).contains(needle)) {
                functionCount++;
                line("CONTAINING_FUNCTION", function.getName(true),
                    function.getEntryPoint().toString(), elf(function.getEntryPoint()),
                    function.getSignature().getPrototypeString());
            }
        }
        line("CONTAINING_FUNCTION_COUNT", Integer.toString(functionCount));
    }

    private Function resolveRoot(List<Symbol> symbols) {
        Set<Function> candidates = new LinkedHashSet<>();
        for (Symbol symbol : symbols) {
            Function function = currentProgram.getFunctionManager().getFunctionAt(symbol.getAddress());
            if (function == null) {
                function = currentProgram.getFunctionManager().getFunctionContaining(symbol.getAddress());
            }
            if (function != null) {
                candidates.add(function);
            }
        }
        if (candidates.size() == 1) {
            return candidates.iterator().next();
        }

        FunctionIterator functions = currentProgram.getFunctionManager().getFunctions(true);
        while (functions.hasNext()) {
            Function function = functions.next();
            if (JNI_NAME.equals(function.getName()) || JNI_NAME.equals(function.getName(true))) {
                candidates.add(function);
            }
        }
        return candidates.size() == 1 ? candidates.iterator().next() : null;
    }

    private Function resolveFromNativeMethodTable() throws Exception {
        byte[] nameBytes = ("nativeResumeMainInit\0").getBytes(StandardCharsets.US_ASCII);
        List<Address> nameAddresses = findBytes(nameBytes);
        line("JNI_METHOD_NAME_STRING_COUNT", Integer.toString(nameAddresses.size()));
        Set<Function> candidates = new LinkedHashSet<>();
        for (Address nameAddress : nameAddresses) {
            MemoryBlock nameBlock = memory.getBlock(nameAddress);
            line("JNI_METHOD_NAME_STRING", nameAddress.toString(),
                nameBlock == null ? "NO_BLOCK" : nameBlock.getName());
            ReferenceIterator references = currentProgram.getReferenceManager()
                .getReferencesTo(nameAddress);
            while (references.hasNext()) {
                Reference reference = references.next();
                Function owner = currentProgram.getFunctionManager()
                    .getFunctionContaining(reference.getFromAddress());
                line("JNI_METHOD_NAME_REF", reference.getFromAddress().toString(),
                    reference.getReferenceType().toString(),
                    owner == null ? "NO_FUNCTION" : owner.getName(true),
                    owner == null ? "" : owner.getEntryPoint().toString());
            }
            Set<Address> pointerHits = new LinkedHashSet<>();
            pointerHits.addAll(findBytes(littleEndian(nameAddress.getOffset(), 8)));
            pointerHits.addAll(findBytes(littleEndian(nameAddress.getOffset() - IMAGE_BASE, 8)));
            pointerHits.addAll(findBytes(littleEndian(nameAddress.getOffset(), 4)));
            pointerHits.addAll(findBytes(littleEndian(nameAddress.getOffset() - IMAGE_BASE, 4)));
            for (Address pointerAddress : pointerHits) {
                line("JNI_NAME_POINTER", pointerAddress.toString(), nameAddress.toString());
                try {
                    long namePointer = memory.getLong(pointerAddress);
                    long signaturePointer = memory.getLong(pointerAddress.add(8));
                    long functionPointer = memory.getLong(pointerAddress.add(16));
                    String signature = readCString(toAddr(signaturePointer), 64);
                    Address functionAddress = toAddr(functionPointer);
                    MemoryBlock block = memory.getBlock(functionAddress);
                    boolean executable = block != null && block.isExecute();
                    Function function = currentProgram.getFunctionManager()
                        .getFunctionAt(functionAddress);
                    if (function == null) {
                        function = currentProgram.getFunctionManager()
                            .getFunctionContaining(functionAddress);
                    }
                    line("JNI_TABLE_ROW", pointerAddress.toString(), hx(namePointer),
                        hx(signaturePointer), clean(signature), hx(functionPointer),
                        Boolean.toString(executable),
                        function == null ? "NO_FUNCTION" : function.getName(true),
                        function == null ? "" : function.getEntryPoint().toString());
                    if ("()V".equals(signature) && executable && function != null &&
                            function.getEntryPoint().equals(functionAddress)) {
                        candidates.add(function);
                    }
                } catch (Exception exception) {
                    line("JNI_TABLE_ROW_ERROR", pointerAddress.toString(),
                        clean(exception.getMessage()));
                }
            }
        }
        line("JNI_TABLE_FUNCTION_CANDIDATE_COUNT", Integer.toString(candidates.size()));
        for (Function candidate : candidates) {
            line("JNI_TABLE_FUNCTION_CANDIDATE", candidate.getName(true),
                candidate.getEntryPoint().toString(), elf(candidate.getEntryPoint()));
        }
        return candidates.size() == 1 ? candidates.iterator().next() : null;
    }

    private Function resolveFromRelaNativeMethodTable() throws Exception {
        MemoryBlock rela = findBlock(".rela.dyn");
        MemoryBlock dynstr = findBlock(".dynstr");
        if (rela == null || dynstr == null) {
            line("JNI_RELA_STATUS", "BLOCK_MISSING");
            return null;
        }

        Address shortMethodName = findCStringInBlock(dynstr, "nativeResumeMainInit");
        Address fullMethodName = findCStringInBlock(dynstr, JNI_NAME);
        if (shortMethodName == null || fullMethodName == null) {
            line("JNI_RELA_STATUS", "METHOD_NAME_MISSING");
            return null;
        }
        Set<Long> methodNameValues = new HashSet<>();
        methodNameValues.add(shortMethodName.getOffset() - IMAGE_BASE);
        methodNameValues.add(shortMethodName.getOffset());
        methodNameValues.add(fullMethodName.getOffset() - IMAGE_BASE);
        methodNameValues.add(fullMethodName.getOffset());
        long entrySize = 24;
        long byteLength = rela.getEnd().subtract(rela.getStart()) + 1;
        List<Long> rowOffsets = new ArrayList<>();
        for (long offset = 0; offset + entrySize <= byteLength; offset += entrySize) {
            Address entry = rela.getStart().add(offset);
            long addend = memory.getLong(entry.add(16));
            if (methodNameValues.contains(addend)) {
                long rowOffset = memory.getLong(entry);
                rowOffsets.add(rowOffset);
                line("JNI_RELA_NAME_MATCH", entry.toString(), hx(rowOffset), hx(addend),
                    addend == shortMethodName.getOffset() ||
                        addend == shortMethodName.getOffset() - IMAGE_BASE
                        ? shortMethodName.toString() : fullMethodName.toString());
            }
        }
        line("JNI_RELA_NAME_MATCH_COUNT", Integer.toString(rowOffsets.size()));
        if (rowOffsets.isEmpty()) {
            return null;
        }

        Set<Long> wantedOffsets = new HashSet<>();
        for (long rowOffset : rowOffsets) {
            wantedOffsets.add(rowOffset + 8);
            wantedOffsets.add(rowOffset + 16);
        }
        Map<Long, Long> neighborAddends = new HashMap<>();
        for (long offset = 0; offset + entrySize <= byteLength; offset += entrySize) {
            Address entry = rela.getStart().add(offset);
            long targetOffset = memory.getLong(entry);
            if (wantedOffsets.contains(targetOffset)) {
                long addend = memory.getLong(entry.add(16));
                neighborAddends.put(targetOffset, addend);
                line("JNI_RELA_NEIGHBOR", entry.toString(), hx(targetOffset), hx(addend));
            }
        }

        Set<Function> candidates = new LinkedHashSet<>();
        for (long rowOffset : rowOffsets) {
            Long signatureRaw = neighborAddends.get(rowOffset + 8);
            Long functionRaw = neighborAddends.get(rowOffset + 16);
            if (signatureRaw == null || functionRaw == null) {
                line("JNI_RELA_ROW", hx(rowOffset), "INCOMPLETE_NEIGHBORS");
                continue;
            }
            Address signatureAddress = toAddr(signatureRaw + IMAGE_BASE);
            String signature = readCString(signatureAddress, 64);
            Address functionAddress = toAddr(functionRaw + IMAGE_BASE);
            MemoryBlock functionBlock = memory.getBlock(functionAddress);
            boolean executable = functionBlock != null && functionBlock.isExecute();
            Function function = currentProgram.getFunctionManager().getFunctionAt(functionAddress);
            if (function == null) {
                function = currentProgram.getFunctionManager().getFunctionContaining(functionAddress);
            }
            line("JNI_RELA_ROW", hx(rowOffset), signatureAddress.toString(), clean(signature),
                functionAddress.toString(), Boolean.toString(executable),
                function == null ? "NO_FUNCTION" : function.getName(true),
                function == null ? "" : function.getEntryPoint().toString());
            if ("()V".equals(signature) && executable && function != null &&
                    function.getEntryPoint().equals(functionAddress)) {
                candidates.add(function);
            }
        }
        line("JNI_RELA_FUNCTION_CANDIDATE_COUNT", Integer.toString(candidates.size()));
        return candidates.size() == 1 ? candidates.iterator().next() : null;
    }

    private Set<Function> findJniNameCodeReferences() throws Exception {
        MemoryBlock dynstr = findBlock(".dynstr");
        Set<Function> owners = new LinkedHashSet<>();
        if (dynstr == null) {
            return owners;
        }
        Set<Long> targets = new HashSet<>();
        Address fullName = findCStringInBlock(dynstr, JNI_NAME);
        Address shortName = findCStringInBlock(dynstr, "nativeResumeMainInit");
        if (fullName != null) {
            targets.add(fullName.getOffset());
        }
        if (shortName != null) {
            targets.add(shortName.getOffset());
        }

        InstructionIterator instructions = currentProgram.getListing().getInstructions(true);
        long adrpCount = 0;
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            Address address = instruction.getAddress();
            MemoryBlock block = memory.getBlock(address);
            if (block == null || !block.isExecute() || instruction.getLength() != 4) {
                continue;
            }
            long word = memory.getInt(address) & 0xffffffffL;
            if ((word & 0x9f000000L) == 0x10000000L) {
                long immlo = (word >>> 29) & 0x3L;
                long immhi = (word >>> 5) & 0x7ffffL;
                long immediate = (immhi << 2) | immlo;
                if ((immediate & 0x100000L) != 0) {
                    immediate |= ~0x1fffffL;
                }
                long target = address.getOffset() + immediate;
                if (targets.contains(target)) {
                    Function owner = currentProgram.getFunctionManager().getFunctionContaining(address);
                    line("JNI_NAME_CODE_REF_ADR", address.toString(), hx(target),
                        owner == null ? "NO_FUNCTION" : owner.getName(true),
                        owner == null ? "" : owner.getEntryPoint().toString());
                    if (owner != null) {
                        owners.add(owner);
                    }
                }
            }
            if ((word & 0x9f000000L) != 0x90000000L) {
                continue;
            }
            adrpCount++;
            long immlo = (word >>> 29) & 0x3L;
            long immhi = (word >>> 5) & 0x7ffffL;
            long immediate = (immhi << 2) | immlo;
            if ((immediate & 0x100000L) != 0) {
                immediate |= ~0x1fffffL;
            }
            long page = (address.getOffset() & ~0xfffL) + (immediate << 12);
            int destinationRegister = (int) (word & 0x1fL);
            for (int distance = 1; distance <= 5; distance++) {
                Address addAddress = address.add(distance * 4L);
                if (!block.contains(addAddress)) {
                    break;
                }
                long addWord = memory.getInt(addAddress) & 0xffffffffL;
                if ((addWord & 0x7f000000L) != 0x11000000L) {
                    continue;
                }
                int baseRegister = (int) ((addWord >>> 5) & 0x1fL);
                if (baseRegister != destinationRegister) {
                    continue;
                }
                long addImmediate = (addWord >>> 10) & 0xfffL;
                if (((addWord >>> 22) & 1L) != 0) {
                    addImmediate <<= 12;
                }
                long target = page + addImmediate;
                if (!targets.contains(target)) {
                    continue;
                }
                Function owner = currentProgram.getFunctionManager().getFunctionContaining(address);
                line("JNI_NAME_CODE_REF", address.toString(), addAddress.toString(), hx(target),
                    owner == null ? "NO_FUNCTION" : owner.getName(true),
                    owner == null ? "" : owner.getEntryPoint().toString());
                if (owner != null) {
                    owners.add(owner);
                }
            }
        }
        line("JNI_NAME_ADRP_SCANNED", Long.toString(adrpCount));
        return owners;
    }

    private Function resolveFromElfDynamicSymbolTable() throws Exception {
        Function dynamicTagCandidate = resolveFromElfDynamicTags();
        if (dynamicTagCandidate != null) {
            return dynamicTagCandidate;
        }
        MemoryBlock dynstr = findBlock(".dynstr");
        MemoryBlock dynsym = findBlock(".dynsym");
        if (dynstr == null || dynsym == null) {
            line("ELF_DYNSYM_STATUS", "BLOCK_MISSING",
                dynstr == null ? "NO_DYNSTR" : dynstr.getStart().toString(),
                dynsym == null ? "NO_DYNSYM" : dynsym.getStart().toString());
            return null;
        }
        line("ELF_DYNSTR_BLOCK", dynstr.getStart().toString(), dynstr.getEnd().toString());
        line("ELF_DYNSYM_BLOCK", dynsym.getStart().toString(), dynsym.getEnd().toString());
        for (MemoryBlock block : memory.getBlocks()) {
            String lowerName = block.getName().toLowerCase(Locale.ROOT);
            if (lowerName.contains("sym") || lowerName.contains("str")) {
                line("ELF_SYMBOL_RELATED_BLOCK", block.getName(), block.getStart().toString(),
                    block.getEnd().toString(), Long.toString(block.getSize()));
            }
        }

        Set<Function> candidates = new LinkedHashSet<>();
        long entrySize = 24;
        long byteLength = dynsym.getEnd().subtract(dynsym.getStart()) + 1;
        Address expectedNameAddress = findCStringInBlock(dynstr, JNI_NAME);
        long expectedNameOffset = expectedNameAddress == null
            ? -1
            : expectedNameAddress.subtract(dynstr.getStart());
        line("ELF_DYNSYM_LAYOUT", Long.toString(byteLength), Long.toString(entrySize),
            Long.toString(byteLength / entrySize), hx(expectedNameOffset));
        int sampleCount = 0;
        for (long offset = 0; offset + entrySize <= byteLength; offset += entrySize) {
            Address entry = dynsym.getStart().add(offset);
            long nameOffset = memory.getInt(entry) & 0xffffffffL;
            if (sampleCount < 16 && nameOffset < dynstr.getSize()) {
                line("ELF_DYNSYM_SAMPLE", entry.toString(), Long.toString(nameOffset),
                    readCString(dynstr.getStart().add(nameOffset), 96));
                sampleCount++;
            }
            if (nameOffset == 0 || nameOffset >= dynstr.getSize()) {
                continue;
            }
            String name = readCString(dynstr.getStart().add(nameOffset), JNI_NAME.length() + 8);
            if (name.contains("ResumeMainInit") || name.contains("nativeResume") ||
                name.contains("GameActivity")) {
                line("ELF_DYNSYM_RELATED_NAME", entry.toString(), Long.toString(nameOffset), name);
            }
            if (!JNI_NAME.equals(name)) {
                continue;
            }
            int info = memory.getByte(entry.add(4)) & 0xff;
            int other = memory.getByte(entry.add(5)) & 0xff;
            int sectionIndex = memory.getShort(entry.add(6)) & 0xffff;
            long rawValue = memory.getLong(entry.add(8));
            long size = memory.getLong(entry.add(16));
            Address ghidraAddress = toAddr(rawValue + IMAGE_BASE);
            MemoryBlock targetBlock = memory.getBlock(ghidraAddress);
            boolean executable = targetBlock != null && targetBlock.isExecute();
            Function function = currentProgram.getFunctionManager().getFunctionAt(ghidraAddress);
            if (function == null) {
                function = currentProgram.getFunctionManager().getFunctionContaining(ghidraAddress);
            }
            line("ELF_DYNSYM_MATCH", entry.toString(), Long.toString(nameOffset),
                Integer.toString(info), Integer.toString(other), Integer.toString(sectionIndex),
                hx(rawValue), hx(size), ghidraAddress.toString(), Boolean.toString(executable),
                function == null ? "NO_FUNCTION" : function.getName(true),
                function == null ? "" : function.getEntryPoint().toString());
            if (executable && function != null && function.getEntryPoint().equals(ghidraAddress)) {
                candidates.add(function);
            }
        }
        int rawNameOffsetMatches = 0;
        for (long offset = 0; offset + 4 <= byteLength; offset++) {
            Address address = dynsym.getStart().add(offset);
            long value = memory.getInt(address) & 0xffffffffL;
            if (value == expectedNameOffset) {
                line("ELF_DYNSYM_RAW_NAME_OFFSET_MATCH", address.toString(), Long.toString(offset));
                rawNameOffsetMatches++;
            }
        }
        line("ELF_DYNSYM_RAW_NAME_OFFSET_MATCH_COUNT", Integer.toString(rawNameOffsetMatches));
        line("ELF_DYNSYM_FUNCTION_CANDIDATE_COUNT", Integer.toString(candidates.size()));
        return candidates.size() == 1 ? candidates.iterator().next() : null;
    }

    private Function resolveFromElfDynamicTags() throws Exception {
        MemoryBlock dynamic = findBlock(".dynamic");
        if (dynamic == null) {
            line("ELF_DYNAMIC_STATUS", "BLOCK_MISSING");
            return null;
        }
        Map<Long, Long> tags = new HashMap<>();
        long byteLength = dynamic.getEnd().subtract(dynamic.getStart()) + 1;
        for (long offset = 0; offset + 16 <= byteLength; offset += 16) {
            Address entry = dynamic.getStart().add(offset);
            long tag = memory.getLong(entry);
            long value = memory.getLong(entry.add(8));
            if (tag == 0) {
                break;
            }
            tags.put(tag, value);
            if (tag == 4 || tag == 5 || tag == 6 || tag == 10 || tag == 11 ||
                    tag == 0x6ffffef5L) {
                line("ELF_DYNAMIC_TAG", hx(tag), hx(value));
            }
        }
        Long stringTableRaw = tags.get(5L);
        Long symbolTableRaw = tags.get(6L);
        Long hashTableRaw = tags.get(4L);
        Long symbolEntrySize = tags.get(11L);
        if (stringTableRaw == null || symbolTableRaw == null || hashTableRaw == null ||
                symbolEntrySize == null || symbolEntrySize != 24) {
            line("ELF_DYNAMIC_STATUS", "REQUIRED_TAG_MISSING");
            return null;
        }
        Address stringTable = toAddr(stringTableRaw + IMAGE_BASE);
        Address symbolTable = toAddr(symbolTableRaw + IMAGE_BASE);
        Address hashTable = toAddr(hashTableRaw + IMAGE_BASE);
        long bucketCount = memory.getInt(hashTable) & 0xffffffffL;
        long symbolCount = memory.getInt(hashTable.add(4)) & 0xffffffffL;
        line("ELF_DYNAMIC_LAYOUT", stringTable.toString(), symbolTable.toString(),
            hashTable.toString(), Long.toString(bucketCount), Long.toString(symbolCount));
        if (symbolCount == 0 || symbolCount > 1000000) {
            line("ELF_DYNAMIC_STATUS", "INVALID_SYMBOL_COUNT");
            return null;
        }

        Set<Function> candidates = new LinkedHashSet<>();
        for (long index = 0; index < symbolCount; index++) {
            Address entry = symbolTable.add(index * 24);
            long nameOffset = memory.getInt(entry) & 0xffffffffL;
            if (nameOffset == 0) {
                continue;
            }
            String name = readCString(stringTable.add(nameOffset), JNI_NAME.length() + 8);
            if (!JNI_NAME.equals(name)) {
                continue;
            }
            long rawValue = memory.getLong(entry.add(8));
            long size = memory.getLong(entry.add(16));
            Address functionAddress = toAddr(rawValue + IMAGE_BASE);
            MemoryBlock block = memory.getBlock(functionAddress);
            boolean executable = block != null && block.isExecute();
            Function function = currentProgram.getFunctionManager().getFunctionAt(functionAddress);
            if (function == null) {
                function = currentProgram.getFunctionManager().getFunctionContaining(functionAddress);
            }
            line("ELF_DYNAMIC_SYMBOL_MATCH", Long.toString(index), entry.toString(),
                hx(rawValue), hx(size), functionAddress.toString(), Boolean.toString(executable),
                function == null ? "NO_FUNCTION" : function.getName(true),
                function == null ? "" : function.getEntryPoint().toString());
            if (executable && function != null && function.getEntryPoint().equals(functionAddress)) {
                candidates.add(function);
            }
        }
        line("ELF_DYNAMIC_FUNCTION_CANDIDATE_COUNT", Integer.toString(candidates.size()));
        return candidates.size() == 1 ? candidates.iterator().next() : null;
    }

    private MemoryBlock findBlock(String name) {
        for (MemoryBlock block : memory.getBlocks()) {
            if (name.equals(block.getName())) {
                return block;
            }
        }
        return null;
    }

    private Address findCStringInBlock(MemoryBlock block, String value) throws Exception {
        byte[] pattern = (value + "\0").getBytes(StandardCharsets.US_ASCII);
        Address cursor = block.getStart();
        while (cursor != null && cursor.compareTo(block.getEnd()) <= 0) {
            Address match = memory.findBytes(cursor, block.getEnd(), pattern, null, true, monitor);
            if (match == null) {
                return null;
            }
            return match;
        }
        return null;
    }

    private List<Address> findBytes(byte[] pattern) {
        List<Address> matches = new ArrayList<>();
        for (MemoryBlock block : memory.getBlocks()) {
            if (!block.isInitialized()) {
                continue;
            }
            Address cursor = block.getStart();
            while (cursor != null && cursor.compareTo(block.getEnd()) <= 0) {
                Address match = memory.findBytes(cursor, block.getEnd(), pattern, null,
                    true, TaskMonitor.DUMMY);
                if (match == null) {
                    break;
                }
                matches.add(match);
                if (match.equals(block.getEnd())) {
                    break;
                }
                cursor = match.next();
            }
        }
        return matches;
    }

    private byte[] littleEndian(long value, int size) {
        byte[] bytes = new byte[size];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)((value >>> (8 * i)) & 0xff);
        }
        return bytes;
    }

    private String readCString(Address address, int maxLength) throws Exception {
        if (!memory.contains(address)) {
            return "";
        }
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            byte current = memory.getByte(address.add(i));
            if (current == 0) {
                break;
            }
            if (current < 0x20 || current > 0x7e) {
                return "";
            }
            value.append((char)current);
        }
        return value.toString();
    }

    private Map<Function, Integer> buildGraph(Function root, int maxDepth) {
        Map<Function, Integer> depths = new HashMap<>();
        ArrayDeque<Function> queue = new ArrayDeque<>();
        depths.put(root, 0);
        queue.add(root);
        while (!queue.isEmpty() && depths.size() < MAX_FUNCTIONS) {
            Function current = queue.removeFirst();
            int depth = depths.get(current);
            if (depth >= maxDepth) {
                continue;
            }
            for (Function callee : current.getCalledFunctions(TaskMonitor.DUMMY)) {
                if (!depths.containsKey(callee)) {
                    depths.put(callee, depth + 1);
                    queue.addLast(callee);
                    if (depths.size() >= MAX_FUNCTIONS) {
                        break;
                    }
                }
            }
        }
        return depths;
    }

    private void writeEdges(Map<Function, Integer> graph) {
        for (Map.Entry<Function, Integer> entry : graph.entrySet()) {
            Function caller = entry.getKey();
            for (Function callee : caller.getCalledFunctions(TaskMonitor.DUMMY)) {
                Integer calleeDepth = graph.get(callee);
                line("EDGE", Integer.toString(entry.getValue()), caller.getName(true),
                    caller.getEntryPoint().toString(), callee.getName(true),
                    callee.getEntryPoint().toString(),
                    calleeDepth == null ? "OUTSIDE_LIMIT" : Integer.toString(calleeDepth));
            }
        }
    }

    private void writeKnownTargetReachability(Map<Function, Integer> graph) {
        for (long target : KNOWN_LUA_TARGETS) {
            Function function = currentProgram.getFunctionManager().getFunctionAt(toAddr(target));
            Integer depth = function == null ? null : graph.get(function);
            line("KNOWN_TARGET", hx(target), function == null ? "NO_FUNCTION" : function.getName(true),
                depth == null ? "NO_EDGE_FOUND" : "DIRECT_GRAPH_DEPTH_" + depth);
        }
    }

    private void exportFunction(Function function, int depth) {
        line("FUNCTION_BEGIN", Integer.toString(depth), function.getName(true),
            function.getEntryPoint().toString(), elf(function.getEntryPoint()),
            function.getSignature().getPrototypeString());

        Set<String> callers = new LinkedHashSet<>();
        for (Function caller : function.getCallingFunctions(TaskMonitor.DUMMY)) {
            callers.add(caller.getName(true) + "@" + caller.getEntryPoint());
        }
        line("CALLERS", String.join(",", callers));

        Set<String> callees = new LinkedHashSet<>();
        for (Function callee : function.getCalledFunctions(TaskMonitor.DUMMY)) {
            callees.add(callee.getName(true) + "@" + callee.getEntryPoint());
        }
        line("CALLEES", String.join(",", callees));

        InstructionIterator instructions = currentProgram.getListing()
            .getInstructions(function.getBody(), true);
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            FlowType flow = instruction.getFlowType();
            if (flow.isCall() || flow.isJump()) {
                Address[] flows = instruction.getFlows();
                line("FLOW", instruction.getAddress().toString(), instruction.toString(),
                    flow.toString(), joinAddresses(flows));
            }
            for (Reference reference : instruction.getReferencesFrom()) {
                RefType type = reference.getReferenceType();
                if (type.isData()) {
                    line("DATA_REF", instruction.getAddress().toString(),
                        reference.getToAddress().toString(), type.toString());
                }
            }
        }

        DecompileResults results = decompiler.decompileFunction(function, 90, TaskMonitor.DUMMY);
        if (!results.decompileCompleted()) {
            line("DECOMPILE_ERROR", clean(results.getErrorMessage()));
        } else {
            String text = results.getDecompiledFunction().getC();
            line("STARTUP_TERMS", String.join(",", matchedTerms(text)));
            out.println("DECOMPILE_BEGIN");
            String[] lines = text.split("\\R", -1);
            int limit = Math.min(lines.length, MAX_DECOMPILE_LINES);
            for (int i = 0; i < limit; i++) {
                out.println(clean(lines[i]));
            }
            if (limit < lines.length) {
                out.println("/* TRUNCATED */");
            }
            out.println("DECOMPILE_END");
        }
        line("FUNCTION_END", function.getName(true), function.getEntryPoint().toString());
    }

    private List<String> matchedTerms(String text) {
        String lower = text.toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (String term : STARTUP_TERMS) {
            if (lower.contains(term.toLowerCase(Locale.ROOT))) {
                matches.add(term);
            }
        }
        return matches;
    }

    private String joinAddresses(Address[] addresses) {
        List<String> values = new ArrayList<>();
        for (Address address : addresses) {
            values.add(address.toString());
        }
        return String.join(",", values);
    }

    private String elf(Address address) {
        return hx(address.getOffset() - IMAGE_BASE);
    }

    private String hx(long value) {
        return String.format("0x%x", value);
    }

    private void line(String... values) {
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                out.print('\t');
            }
            out.print(clean(values[i]));
        }
        out.println();
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
    }
}
