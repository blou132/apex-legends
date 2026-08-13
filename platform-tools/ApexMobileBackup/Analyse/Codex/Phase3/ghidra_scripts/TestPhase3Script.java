// Minimal Phase3B headless Java script test.
// @category ApexMobile

import ghidra.app.script.GhidraScript;

public class TestPhase3Script extends GhidraScript {
	@Override
	public void run() throws Exception {
		println("PHASE3_SCRIPT_OK");
		println(currentProgram.getName());
		println(currentProgram.getLanguageID().toString());
	}
}
