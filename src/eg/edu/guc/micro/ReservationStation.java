package eg.edu.guc.micro;

import java.util.HashMap;

public class ReservationStation {
	// Different functional units:
	// Load
	// Store
	// Mul (multiply and divide)
	// Add (add, sub, jump, beq)
	// Logic (logical operations)
	HashMap<String, String>[] rs; // array of hashmaps, rob entry is hashmap
	private int numberOfLoads;
	private int numberOfStores;
	private int numberOfMultiply;
	private int numberOfAdd;
	private int numberOfLogic;

	@SuppressWarnings("unchecked")
	public ReservationStation(int numberOfLoads, int numberOfStores,
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
			index++;
		}
	}

	public boolean insertInstruction(Instruction instr) {
		/*
		 * for (int i = 0; i < rs.length; i++) { if
		 * (rs[i].get("Name").equals("")) {
		 * 
		 * return true; } } return false;
		 */
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
					&& rs[i].get("Busy").equals("false")) {
				// insert instruction
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
				break;
			}
		}
	}

	public void updateRSentry(String instrIndex, String busy, String vj,
			String vk, String qj, String qk, String dest, String a) {
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
}
