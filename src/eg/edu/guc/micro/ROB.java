package eg.edu.guc.micro;

import java.util.HashMap;

public class ROB {
	ROBentry[] table;
	int headIndex = 0;
	int size;
	int busy;
	int tailIndex = 0;

	public ROB(int size) {
		table = new ROBentry[size];
		this.size = size;
		busy = 0;
		headIndex = 0;
		tailIndex = 0;
		for (int i = 0; i < table.length; i++) {
			table[i] = new ROBentry();
			table[i].index = i;
		}
		table[0].tailWa2f3alya = true;
	}

	public void write(Instruction instr, int index) {
		table[tailIndex].dest = instr.getRegA();
		if (instr.getInstructionName().equals("LW")) {
			table[tailIndex].type = "LOAD";
		} else if (instr.getInstructionName().equals("SW")) {
			table[tailIndex].type = "STORE";
		} else if (instr.getInstructionName().equals("JMP")) {
			table[tailIndex].type = "JMP";
		} else if (instr.getInstructionName().equals("BEQ")) {
			table[tailIndex].type = "BEQ";
		} else if (instr.getInstructionName().equals("JALR")) {
			table[tailIndex].type = "JALR";
		} else if (instr.getInstructionName().equals("RET")) {
			table[tailIndex].type = "RET";
		} else if (instr.getInstructionName().equals("ADD")) {
			table[tailIndex].type = "ADD";
		} else if (instr.getInstructionName().equals("SUB")) {
			table[tailIndex].type = "SUB";
		} else if (instr.getInstructionName().equals("ADDI")) {
			table[tailIndex].type = "ADDI";
		} else if (instr.getInstructionName().equals("NAND")) {
			table[tailIndex].type = "NAND";
		} else if (instr.getInstructionName().equals("MUL")) {
			table[tailIndex].type = "MULT";
		}
		table[tailIndex].instructionIndex = index;
		table[tailIndex].free = false;
		tailIndex = (tailIndex + 1) % table.length;
	}

	// public boolean commitInstruction() {
	// if (table[headIndex].get("Ready").equals("Y")) {
	// // commit
	// headIndex = (headIndex + 1) % table.length;
	// return true;
	// }
	// return false;
	// }
	//
	// public void flushROBstartingFromInstruction(int instrIndex) {
	// // test
	// for (int i = 0; i < table.length; i++) {
	// if (table[i].get("InstructionIndex").equals(instrIndex + "")) {
	// for (int j = i; j != headIndex; j = (j + 1) % table.length) {
	// table[j].put("Destination", "");
	// table[j].put("InstructionIndex", "");
	// table[j].put("Value", "");
	// table[j].put("Ready", "N");
	// }
	// tailIndex = i;
	// break;
	// }
	// }
	// }

	public boolean hasFree() {
		return (table[tailIndex].free);
	}

	public String toString() {
		StringBuilder out = new StringBuilder();
		for (ROBentry entry : table) {
			out.append("Free " + entry.free + " instruction index "
					+ entry.instructionIndex + " ready  " + entry.ready
					+ " value " + entry.value);
			out.append("\n");
		}

		return out.toString();
	}
}
