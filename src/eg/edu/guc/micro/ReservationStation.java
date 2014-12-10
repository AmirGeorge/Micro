package eg.edu.guc.micro;

import java.util.HashMap;

public class ReservationStation {
	// Different functional units:
	// Load
	// Store
	// Mul (multiply and divide)
	// Add (add, sub, jump, beq)
	// Logic (logical operations)

	private static ReservationStation _instance;

	HashMap<String, String>[] rs; // array of hashmaps, rob entry is hashmap
	private int numberOfLoads;
	private int numberOfStores;
	private int numberOfMultiply;
	private int numberOfAdd;
	private int numberOfLogic;

	public static ReservationStation getInstance() {
		if (_instance == null) {
			_instance = new ReservationStation();
		}
		return _instance;
	}

	private ReservationStation() {

	}

	@SuppressWarnings("unchecked")
	public void init(int numberOfLoads, int numberOfStores,
			int numberOfMultiply, int numberOfAdd, int numberOfLogic) {
		rs = new HashMap[numberOfLoads + numberOfStores + numberOfMultiply
				+ numberOfAdd + numberOfLogic];
		int index = 0;
		for (int i = 0; i < numberOfLoads; i++) {
			rs[index] = new HashMap<String, String>();
			rs[index].put("Name", "Load");
			rs[index].put("Busy", "false");
			rs[index].put("InstrcutionIndex", "-1"); // index of the instruction
														// in Instruction
														// ArrayList in Engine
														// class
			rs[index].put("Vj", "");// register name to be put here
			rs[index].put("Vk", "");
			rs[index].put("Qj", "");
			rs[index].put("Qk", "");
			rs[index].put("Dest", "");
			rs[index].put("A", "");
			rs[index].put("State", "NONE");
			rs[index].put("ExecCyclesLeft", "");
			rs[index].put("Fresh", "false");// handling 7etet wa7d fi nafs el
											// cycle fadda el entry bas ana
											// m2drsh aktb fel entry dih fi nafs
											// el clock cycle 7asab el sheet
			rs[index].put("OperandsFresh", "false");// handling 7ettet wa7d fi
													// nafs el cycle 3aml write
													// lel operands ely kan wa2f
													// 3leeha, el mfrood 7asab
													// el sheet mabtedeesh
													// execute fel cycle dih
			index++;
		}

		for (int i = 0; i < numberOfStores; i++) {
			rs[index] = new HashMap<String, String>();
			rs[index].put("Name", "Store");
			rs[index].put("Busy", "false");
			rs[index].put("InstrcutionIndex", "-1");
			rs[index].put("Vj", "");
			rs[index].put("Vk", "");
			rs[index].put("Qj", "");
			rs[index].put("Qk", "");
			rs[index].put("Dest", "");
			rs[index].put("A", "");
			rs[index].put("State", "NONE");
			rs[index].put("ExecCyclesLeft", "");
			rs[index].put("Fresh", "false");
			rs[index].put("OperandsFresh", "false");
			index++;
		}
		for (int i = 0; i < numberOfMultiply; i++) {
			rs[index] = new HashMap<String, String>();
			rs[index].put("Name", "Mul");
			rs[index].put("Busy", "false");
			rs[index].put("InstrcutionIndex", "-1");
			rs[index].put("Vj", "");
			rs[index].put("Vk", "");
			rs[index].put("Qj", "");
			rs[index].put("Qk", "");
			rs[index].put("Dest", "");
			rs[index].put("A", "");
			rs[index].put("State", "NONE");
			rs[index].put("ExecCyclesLeft", "");
			rs[index].put("Fresh", "false");
			rs[index].put("OperandsFresh", "false");
			index++;
		}

		for (int i = 0; i < numberOfAdd; i++) {
			rs[index] = new HashMap<String, String>();
			rs[index].put("Name", "Add");
			rs[index].put("Busy", "false");
			rs[index].put("InstrcutionIndex", "-1");
			rs[index].put("Vj", "");
			rs[index].put("Vk", "");
			rs[index].put("Qj", "");
			rs[index].put("Qk", "");
			rs[index].put("Dest", "");
			rs[index].put("A", "");
			rs[index].put("State", "NONE");
			rs[index].put("ExecCyclesLeft", "");
			rs[index].put("Fresh", "false");
			rs[index].put("OperandsFresh", "false");
			index++;
		}

		for (int i = 0; i < numberOfLoads; i++) {
			rs[index] = new HashMap<String, String>();
			rs[index].put("Name", "Logic");
			rs[index].put("Busy", "false");
			rs[index].put("InstrcutionIndex", "-1");
			rs[index].put("Vj", "");
			rs[index].put("Vk", "");
			rs[index].put("Qj", "");
			rs[index].put("Qk", "");
			rs[index].put("Dest", "");
			rs[index].put("A", "");
			rs[index].put("State", "NONE");
			rs[index].put("ExecCyclesLeft", "");
			rs[index].put("Fresh", "false");
			rs[index].put("OperandsFresh", "false");
			index++;
		}
	}

	public boolean insertInstruction(Instruction instr) {
		if (instr.getInstructionName().equals("LW")) {
			return insertIntoRS(instr, "Load");
		}
		if (instr.getInstructionName().equals("SW")) {
			return insertIntoRS(instr, "Store");
		}
		if (instr.getInstructionName().equals("JMP")) {
			return insertIntoRS(instr, "Add");
		}
		if (instr.getInstructionName().equals("BEQ")) {
			return insertIntoRS(instr, "Add");
		}
		if (instr.getInstructionName().equals("JALR")) {
			return insertIntoRS(instr, "Add");
		}
		if (instr.getInstructionName().equals("RET")) {
			return insertIntoRS(instr, "Add");
		}
		if (instr.getInstructionName().equals("ADD")) {
			return insertIntoRS(instr, "Add");
		}
		if (instr.getInstructionName().equals("SUB")) {
			return insertIntoRS(instr, "Add");
		}
		if (instr.getInstructionName().equals("ADDI")) {
			return insertIntoRS(instr, "Add");
		}
		if (instr.getInstructionName().equals("NAND")) {
			return insertIntoRS(instr, "Logic");
		}
		if (instr.getInstructionName().equals("MUL")) {
			return insertIntoRS(instr, "Mul");
		}
		return false;
	}

	private boolean insertIntoRS(Instruction instr, String rsName) {
		for (int i = 0; i < rs.length; i++) {
			if (rs[i].get("Name").equals(rsName)
					&& rs[i].get("Busy").equals("false")
					&& rs[i].get("Fresh").equals("false")) {
				// TODO insert instruction
				return true;
			}
		}
		return false;
	}

	public void deleteFromRS(int instructionIndex) { // index of instruction
														// in
														// ArrayList<Instruction>
		for (int i = 0; i < rs.length; i++) {
			if (rs[i].get("InstrcutionIndex").equals(instructionIndex + "")) {
				rs[i].put("Busy", "false");
				rs[i].put("InstrcutionIndex", "-1");
				rs[i].put("Vj", "");
				rs[i].put("Vk", "");
				rs[i].put("Qj", "");
				rs[i].put("Qk", "");
				rs[i].put("Dest", "");
				rs[i].put("A", "");
				rs[i].put("Fresh", "true");
				break;
			}
		}
	}

	public void updateRSentry(String instrIndex, String busy, String vj,
			String vk, String qj, String qk, String dest, String a,
			String state, String execCyclesLeft, String fresh,
			String operandsFresh) {
		for (int i = 0; i < rs.length; i++) {
			if (rs[i].get("InstrcutionIndex").equals(instrIndex + "")) {
				if (busy != null) {
					rs[i].put("Busy", busy);
				}
				if (vj != null) {
					rs[i].put("Vj", vj);
				}
				if (vk != null) {
					rs[i].put("Vk", vk);
				}
				if (qj != null) {
					rs[i].put("Qj", qj);
				}
				if (qk != null) {
					rs[i].put("Qk", qk);
				}
				if (dest != null) {
					rs[i].put("Dest", dest);
				}
				if (a != null) {
					rs[i].put("A", a);
				}
				if (state != null) {
					rs[i].put("State", state);
				}
				if (execCyclesLeft != null) {
					rs[i].put("ExecCyclesLeft", execCyclesLeft);
				}
				if (fresh != null) {
					rs[i].put("Fresh", fresh);
				}
				if (operandsFresh != null) {
					rs[i].put("OperandsFresh", operandsFresh);
				}
				break;
			}
		}
	}

	public HashMap<String, String> getRSentryOfInstruction(int instrIndex) {
		for (int i = 0; i < rs.length; i++) {
			if (rs[i].get("InstrcutionIndex").equals(instrIndex + "")) {
				return rs[i];
			}
		}
		return null;
	}

	public void falsifyFreshes() {
		for (int i = 0; i < rs.length; i++) {
			rs[i].put("Fresh", "false");
			rs[i].put("OperandsFresh", "false");
		}
	}

	public void executeAllPossible() {
		// TODO check if issued instructions have operands ready and operands
		// not fresh then start execution
		// TODO check if executing instructions have execution cycles left, then
		// decrement it, if reaches zero make state EXECUTED
		// TODO append to guiConsoleOutput
	}

	public int writeIfPossible() {// returns index of written instruction, -1 if
									// no write
		// TODO loop on the ROB from head to tail-1 and write first instruction
		// that is ready for write (WRITE not COMMIT, you will know if ready for
		// write from the RS entry).Do the loop on ROB thing to always write the
		// instruction earlier in program order if more than one instruction can
		// write
		// TODO when write is finished pass operands to other RS entries waiting
		// for it and set OperandsFresh for them to true
		// TODO handle store stage where latency spent in write
		return -1;
	}
}
