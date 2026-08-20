# Package, OBB, cache, and network preflight

The installed package matched the expected client:

- package: `com.ea.gp.apexlegendsmobilefps`;
- version name: `1.3.672.546`;
- version code: `64003140`;
- main OBB: expected name, 1,942,013,346 bytes;
- patch OBB: expected name, 1,837,582,506 bytes;
- validation cache: present, two matching rows, valid.

The guest network initially had airplane mode disabled, Wi-Fi enabled, and
mobile data enabled. Before launch it was isolated with airplane mode enabled,
Wi-Fi disabled, and mobile data disabled. There was no active default network
and no default route. No ping, DNS, curl, account, or authentication operation
was performed.

The launch followed the established cache fast path: `DownloaderActivity` was
seen, state `4` returned result `1`, no full validation marker appeared, and
`GameActivity` became resumed. The explicit historical cache marker was absent,
so cache reuse is classified from the independently valid cache plus this fast
state/result transition.
