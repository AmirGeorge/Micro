package eg.edu.guc.micro;

import java.util.HashMap;

public class ROB {

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
		}
	}

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

}
