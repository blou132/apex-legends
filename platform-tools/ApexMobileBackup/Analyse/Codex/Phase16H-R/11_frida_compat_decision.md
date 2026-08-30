# Frida compatibility decision

Upstream Frida issue 3707 describes post-17.6 Android helper failures where
enumeration can work while attach fails under enforcing SELinux. The upstream
fix commit is `23f288c5e5de333e36dcbbba2be80abe1f63b389`.

The Frida 17.17.0 source gitlink for frida-core is
`c812943c457376c906802f7bb79d13c67a6e4d22`. Local Git ancestry verification
confirmed that the fix commit is an ancestor of that exact source revision.

Therefore the fix is included in 17.17.0. The PRA-LX1 failure must not be
automatically identified as issue 3707. Its current signature is a reproducible
ART/helper startup SIGSEGV with no current-run AVC.

```text
UPSTREAM_HELPER_FIX_INCLUDED_IN_17_17 = YES
FRIDA_17_17_FAILURE_EQUALS_ISSUE_3707 = NOT_ESTABLISHED
```

Primary source: https://github.com/frida/frida/issues/3707
