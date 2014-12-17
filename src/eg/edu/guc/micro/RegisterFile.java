package eg.edu.guc.micro;

import java.util.HashMap;

public class RegisterFile {

	private HashMap<String, Short> regFile;
	private static RegisterFile _instance;

	private RegisterFile() {
		regFile = new HashMap<String, Short>();
		regFile.put("$R0", (short) 0);
		regFile.put("$R1", (short) 0);
		regFile.put("$R2", (short) 0);
		regFile.put("$R3", (short) 0);
		regFile.put("$R4", (short) 0);
		regFile.put("$R5", (short) 0);
		regFile.put("$R6", (short) 0);
		regFile.put("$R7", (short) 0);
	}

	public static RegisterFile getInstance() {
		if (_instance == null)
			_instance = new RegisterFile();
		return _instance;
	}

	public short getValueAt(String regName) {
		return regFile.get(regName);
	}

	public void setValueAt(String regName, short value) {
		if (!regName.equals("R0")) {
			regFile.put(regName, value);
		}
	}

	public HashMap<String, Short> getRegFile() {
		return regFile;
	}

	public void write(Instruction inst) {
		// TODO Auto-generated method stub

	}

}
