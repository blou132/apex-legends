# Thread-name assignment

The bounded producer path does not reach `pthread_setname_np`, `prctl`, or an
engine naming helper. Five simultaneous tasks sharing `Thread-10` prove that
the name is not role-specific even without resolving the assignment site.

- `THREAD_NAME_ASSIGNMENT_SITE = UNKNOWN`
- `THREAD_NAME_ASSIGNMENT_TIMING = UNKNOWN`
- `THREAD10_NAME_IS_ROLE_SPECIFIC = NO`
