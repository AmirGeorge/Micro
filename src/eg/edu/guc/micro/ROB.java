package eg.edu.guc.micro;

import java.util.HashMap;

public class ROB {
	HashMap<String, String>[] table;
	int headIndex = 0;
	int tailIndex = 0;

	@SuppressWarnings("unchecked")
	public ROB(int size) {
		table = new HashMap[size];
		for (int i = 0; i < table.length; i++) {
			table[i] = new HashMap<String, String>();
			table[i].put("InstructionIndex", "");
			table[i].put("Destination", "");
			table[i].put("Value", "");
			table[i].put("Ready", "N");
		}
		headIndex = 0;
		tailIndex = 0;
	}

	public boolean insertInstruction(Instruction instr) {
		if (headIndex == tailIndex && headIndex != 0) {
			return false;
		}
		if (instr.getInstructionName().equals("LW")) {
			// TODO
		} else if (instr.getInstructionName().equals("SW")) {
			// TODO
		} else if (instr.getInstructionName().equals("JMP")) {
			// TODO
		} else if (instr.getInstructionName().equals("BEQ")) {
			// TODO
		} else if (instr.getInstructionName().equals("JALR")) {
			// TODO
		} else if (instr.getInstructionName().equals("RET")) {
			// TODO
		} else if (instr.getInstructionName().equals("ADD")) {
			// TODO
		} else if (instr.getInstructionName().equals("SUB")) {
			// TODO
		} else if (instr.getInstructionName().equals("ADDI")) {
			// TODO
		} else if (instr.getInstructionName().equals("NAND")) {
			// TODO
		} else if (instr.getInstructionName().equals("MUL")) {
			// TODO
		}
		tailIndex = (tailIndex + 1) % table.length;
		return true;
	}

	public boolean commitInstruction() {
		if (table[headIndex].get("Ready").equals("Y")) {
			// TODO commit
			headIndex = (headIndex + 1) % table.length;
			return true;
		}
		return false;
	}

	public void flushROBstartingFromInstruction(int instrIndex) {
		for (int i = 0; i < table.length; i++) {
			if (table[i].get("InstructionIndex").equals(instrIndex + "")) {
				for (int j = i; j != headIndex; j = (j + 1) % table.length) {
					table[j].put("Destination", "");
					table[j].put("InstructionIndex", "");
					table[j].put("Value", "");
					table[j].put("Ready", "N");
				}
				tailIndex = i;
				break;
			}
		}
	}

}
