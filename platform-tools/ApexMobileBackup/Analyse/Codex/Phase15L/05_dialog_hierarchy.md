# Dialog hierarchy

The valid compressed hierarchy contains five nodes, all owned by
`com.ea.gp.apexlegendsmobilefps`:

| Class | Resource ID | Content |
| --- | --- | --- |
| `android.widget.FrameLayout` | none | container |
| `android.widget.TextView` | `android:id/alertTitle` | title |
| `android.widget.TextView` | `android:id/message` | message |
| `android.widget.ScrollView` | `android:id/buttonPanel` | button panel |
| `android.widget.Button` | `android:id/button1` | `OK` |

The button is marked clickable by Android, but it was never clicked. The
standard alert resource IDs and the matching Java creator support
`android.app.AlertDialog` as the probable dialog class; the hierarchy root
itself is a `FrameLayout`.
