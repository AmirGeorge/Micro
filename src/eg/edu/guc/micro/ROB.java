package eg.edu.guc.micro;

import java.util.HashMap;

public class ROB {
<<<<<<< HEAD
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
=======

	private static ROB _instance;

	private boolean initialState;

	HashMap<String, String>[] table;
	int headIndex;
	int tailIndex;

	public static ROB getInstance() {
		if (_instance == null) {
			_instance = new ROB();
		}
		return _instance;
	}

	private ROB() {

	}

	@SuppressWarnings("unchecked")
	public void init(int size) {
		table = new HashMap[size];
		for (int i = 0; i < table.length; i++) {
			table[i] = new HashMap<String, String>();
			table[i].put("InstructionIndex", "");
			table[i].put("Destination", "");
			table[i].put("Value", "");
			table[i].put("Ready", "N");
			table[i].put("Fresh", "false"); // handling 7etet wa7d fi nafs el
											// cycle fadda el entry bas ana
											// m2drsh aktb fel entry dih fi nafs
											// el clock cycle 7asab el sheet
		}
		headIndex = 0;
		tailIndex = 0;
		initialState = true;
	}

	public void insertInstruction(Instruction instr) {
		// TODO put "Fresh" be "true" hna, handling 7etet wa7d fi nafs el cycle
		// fadda el entry bas ana m2drsh aktb fel entry dih fi nafs el clock
		// cycle 7asab el sheet
>>>>>>> 6cbca245d955db0b4af9161c573303e2ee0e032b
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
<<<<<<< HEAD
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
=======
		initialState = false;
	}

	public boolean commitInstruction() {
		if (table[headIndex].get("Ready").equals("Y")) {
			table[headIndex].put("Fresh", "true");
			// TODO commit: deleteRSentry, deleteROBentry, update actual
			// register or memory value
			headIndex = (headIndex + 1) % table.length;
			return true;
		}
		return false;
	}

	public void flushROBstartingFromInstruction(int instrIndex) {
		// TODO test
		// TODO flush RS too
		for (int i = 0; i < table.length; i++) {
			if (table[i].get("InstructionIndex").equals(instrIndex + "")) {
				if (headIndex == i) {
					initialState = true;// handles when we will flush all the
										// ROB, in that case initial state is
										// restored when ROB is empty
				}
				for (int j = i; j != headIndex; j = (j + 1) % table.length) {
					table[j].put("Destination", "");
					table[j].put("InstructionIndex", "");
					table[j].put("Value", "");
					table[j].put("Ready", "N");
					table[j].put("Fresh", "false");
				}
				tailIndex = i;
				break;
			}
>>>>>>> 6cbca245d955db0b4af9161c573303e2ee0e032b
		}

<<<<<<< HEAD
		return out.toString();
	}
=======
	public boolean isFull() {
		return headIndex == tailIndex && !initialState;
	}

	public void falsifyFreshes() {
		for (int i = 0; i < table.length; i++) {
			table[i].put("Fresh", "false");
		}
	}

	public int getInstructionIndexAtHead() {
		return Integer.parseInt(table[headIndex].get("InstructionIndex"));
	}

>>>>>>> 6cbca245d955db0b4af9161c573303e2ee0e032b
}
