# Next step

Do not retry `-gpu host` on `ApexPhase9Lab` without a new technical reason that
addresses the repeated prelaunch SystemUI instability. Phase15O observed the
same class of stop at 60 seconds; Phase15P reproduced it by 30 seconds under a
stricter checkpoint schedule.

Do not infer an Apex graphics result from either stopped run. Phase15N remains
the clean-room graphics-capability result, while actual host-mode Apex graphics,
version/update, login, and server-list behavior remains unknown.
