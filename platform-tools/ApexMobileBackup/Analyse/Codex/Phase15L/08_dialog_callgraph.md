# Dialog callgraph

Targeted DEX disassembly resolves the Java dialog mechanism:

- `MessageBox01.createAlert()` begins at DEX `0x5a1698`;
- it calls `AlertDialog.Builder.setTitle(Caption)` at `0x5a16d2`;
- it calls `setMessage(Text)` at `0x5a16e0`;
- it installs the dynamic positive button at `0x5a1710`;
- `MessageBox01$2.run()` calls `Builder.create()` at `0x5a14ec`;
- `MessageBox01.show()` begins at `0x5a18d0`, resets the selected button, and
  invokes `createAlert()` at `0x5a18e6`;
- `MessageBox01$1.run()` calls `Alert.show()` at `0x5a14b0`;
- `show()` then polls while no button is selected, sleeping 100 ms at
  `0x5a1920`, and returns the selected value at `0x5a1928`;
- `onClick()` stores the selected button and dismisses the alert at
  `0x5a180c` and `0x5a1810`;
- the alert is non-cancelable at `0x5a1776` and cannot be canceled by touching
  outside at `0x5a1780`.

The implementation proves that this `MessageBox01` mechanism requires explicit
user acknowledgement. The matching runtime content and native method-name
strings make `MessageBox01.createAlert()/show()` the probable exact creator
pipeline. The native trigger caller is still unknown, so the exact bootstrap
edge remains probable rather than confirmed.
