package eg.edu.guc.micro;

import java.io.IOException;
import java.util.HashMap;

public class ReservationStation {
	RSentry[] rs;
	private int numberOfLoads;
	private int numberOfStores;
	private int numberOfMultiply;
	private int numberOfAdd;
	private int numberOfLogic;
	private int busyOfLoads;
	private int busyOfStores;
	private int busyOfMultiply;
	private int busyOfAdd;
	private int busyOfLogic;
	HashMap<String, Integer> registers;

	public ReservationStation(int numberOfLoads, int numberOfStores,
			int numberOfMultiply, int numberOfAdd, int numberOfLogic) {
		rs = new RSentry[numberOfLoads + numberOfStores + numberOfMultiply
				+ numberOfAdd + numberOfLogic];
		this.numberOfLoads = numberOfLoads;
		this.numberOfStores = numberOfStores;
		this.numberOfMultiply = numberOfMultiply;
		this.numberOfAdd = numberOfAdd;
		this.numberOfLogic = numberOfLogic;
		for (int i = 0; i < rs.length; i++) {
			rs[i] = new RSentry();
		}
		registers = new HashMap<String, Integer>();
	}

	public void issue(Instruction instr, int time, int numberOfInstruction)
			throws NumberFormatException, IOException {
		RSentry entry = new RSentry();
		for (int i = 0; i < rs.length; i++) {
			if (!rs[i].busy) {
				entry = rs[i];
				break;
			}
		}
		// System.out.printSln("ISSUE INSRUCTION " + numberOfInstruction +
		// " time "
		// + time);
		Engine.getInstance().sb.append("ISSUE INSRUCTION "
				+ numberOfInstruction + " time " + time);
		entry.busy = true;
		entry.startIssue = time;
		entry.dest = numberOfInstruction;
		String name = instr.getInstructionName().toLowerCase();
		if (!name.equals("sw") && !name.equals("beq") && (!name.equals("jmp")))
			registers.put(instr.getRegA(), numberOfInstruction);
		entry.status = "ISSUED";
		switch (name) {
		case "lw":
			if (registers.containsKey(instr.getRegB()))
				entry.qj = registers.get(instr.getRegB());
			else
				entry.vj = instr.getRegB();
			entry.op = "LOAD";
			entry.A = instr.getImm() + "";
			busyOfLoads++;
			break;
		case "sw":
			if (registers.containsKey(instr.getRegA()))
				entry.qj = registers.get(instr.getRegA());
			else
				entry.vj = instr.getRegA();
			if (registers.containsKey(instr.getRegB()))
				entry.qk = registers.get(instr.getRegB());
			else
				entry.vk = instr.getRegB();
			entry.op = "STORE";
			entry.A = instr.getImm() + "";
			busyOfStores++;
			break;
		case "beq":
			entry.op = "BEQ";
			entry.vj = instr.getRegA() + "";
			entry.vk = instr.getRegB() + "";
			busyOfAdd++;
			break;
		case "jmp":
			entry.op = "JMP";
			entry.vj = instr.getRegB() + "";
			entry.A = instr.getImm() + "";
			busyOfAdd++;
			break;
		case "add":
		case "sub":
		case "addi":
		case "jalr":
		case "ret":
			if (registers.containsKey(instr.getRegB())
					&& (!instr.getRegB().equals(instr.getRegA()))) {
				entry.qj = registers.get(instr.getRegB());
			} else
				entry.vj = instr.getRegB();
			if (registers.containsKey(instr.getRegC())
					&& (!instr.getRegC().equals(instr.getRegA())))
				entry.qk = registers.get(instr.getRegC());
			else
				entry.vk = instr.getRegC();
			entry.op = "ADD";
			busyOfAdd++;
			break;
		case "mul":
		case "div":
			if (registers.containsKey(instr.getRegB()))
				entry.qj = registers.get(instr.getRegB());
			else
				entry.vj = instr.getRegB();
			if (registers.containsKey(instr.getRegC()))
				entry.qk = registers.get(instr.getRegC());
			else
				entry.vk = instr.getRegC();
			entry.op = "MULT/DIV";
			busyOfMultiply++;
			break;
		case "nand":
			if (registers.containsKey(instr.getRegB()))
				entry.qj = registers.get(instr.getRegB());
			else
				entry.vj = instr.getRegB();
			if (registers.containsKey(instr.getRegC()))
				entry.qk = registers.get(instr.getRegC());
			else
				entry.vk = instr.getRegC();
			entry.op = "LOGIC";
			busyOfLogic++;
			break;

		default:
			System.out
					.println("No defined instruction class reserverion stations");
		}

	}

	public void deleteFromRS(int instructionIndex)
			throws NumberFormatException, IOException {
		for (int i = 0; i < rs.length; i++) {
			if (rs[i].dest == instructionIndex) {
				rs[i].busy = false;
				rs[i].dest = -1;
				rs[i].vj = "";
				rs[i].vk = "";
				rs[i].qj = -1;
				rs[i].qk = -1;
				rs[i].A = "";
				rs[i].hat5lsExecuting = 0;
				rs[i].op = "";
				break;
			}
		}
		String type = Engine.getInstance().instructions.get(instructionIndex)
				.getInstructionName().toLowerCase();
		switch (type) {
		case "lw":
			busyOfLoads--;
			break;
		case "sw":
			busyOfStores--;
			break;
		case "add":
		case "sub":
		case "addi":
		case "jalr":
		case "ret":
		case "beq":
		case "jmp":
			busyOfAdd--;
			break;
		case "mul":
		case "div":
			busyOfMultiply--;
			break;
		case "nand":
			busyOfLogic--;
			break;
		}
	}

	public boolean hasFree(Instruction inst) {
		String type = inst.getInstructionName().toLowerCase();
		switch (type) {
		case "lw":
			return busyOfLoads < numberOfLoads;
		case "sw":
			return busyOfStores < numberOfStores;
		case "add":
		case "sub":
		case "addi":
		case "jalr":
		case "ret":
		case "beq":
		case "jmp":
			return busyOfAdd < numberOfAdd;
		case "mul":
		case "div":
			return busyOfMultiply < numberOfMultiply;
		case "nand":
			return busyOfLogic < numberOfLogic;
		default:
			System.out
					.println("No defined instruction class reserverion stations");
		}
		return false;
	}

	public void al3bExecute(int time) throws NumberFormatException, IOException {
		for (int i = 0; i < rs.length; i++) {
			RSentry entry = rs[i];

			if (entry.op.equals("LOAD") && entry.startIssue + 2 == time) {
				entry.status = "EXECUTE";
				entry.hat5lsExecuting = time
						+ Engine.getInstance().loadExecuteLatanecy;

			} else if (entry.op.equals("JMP") && time >= entry.startIssue + 1
					&& entry.status.equals("ISSUED")) {
				entry.status = "EXECUTE";
				entry.hat5lsExecuting = time
						+ Engine.getInstance().addExecuteLatency;
				Engine.getInstance().instructions.get(entry.dest).execute();

			} else if (entry.op.equals("BEQ") && time >= entry.startIssue + 1
					&& entry.status.equals("ISSUED")) {
				entry.status = "EXECUTE";
				entry.hat5lsExecuting = time
						+ Engine.getInstance().addExecuteLatency;
			} else if (entry.op.equals("STORE") && time >= entry.startIssue + 1
					&& entry.vj != "" && entry.status.equals("ISSUED")) {
				entry.op = "STORE";
				entry.status = "EXECUTE";
				entry.hat5lsExecuting = time + 1;
				entry.hat5lsWriting = time
						+ Engine.getInstance().storeExecuteLatanecy;
			} else {

				if (entry.vk == null && !entry.op.equals("BEQ")) {
					// imm
					if (!entry.vj.equals("") && entry.startIssue + 1 == time
							&& entry.status.equals("ISSUED")) {
						entry.hat5lsExecuting = time
								+ Engine.getInstance().addExecuteLatency;
						entry.status = "EXECUTE";

					}
				} else if (!entry.op.equals("BEQ")) {
					if (!entry.vj.equals("") && !entry.vk.equals("")) {
						if (!entry.op.equals("LOAD")
								&& time >= entry.startIssue + 1
								&& entry.status.equals("ISSUED")) {
							String op = entry.op;
							entry.status = "EXECUTE";
							switch (op) {
							case "ADD":
								entry.hat5lsExecuting = time
										+ Engine.getInstance().addExecuteLatency;
								break;
							case "MULT/DIV":
								entry.hat5lsExecuting = time
										+ Engine.getInstance().multExecuteLatency;
								break;
							case "LOGIC":
								entry.hat5lsExecuting = time
										+ Engine.getInstance().logicExecuteLatency;
								break;
							default:
								System.out.println("Undefined 7amada opCode");
							}
						}
					}
				}
			}
		}

	}

	public void al3bWrite(int time) throws NumberFormatException, IOException {
		for (RSentry entry : rs) {
			if ((time == entry.hat5lsExecuting + 1 && time != 0 && entry.status
					.equals("EXECUTE"))
					|| (entry.op.equals("STORE") && entry.hat5lsWriting == time && entry.op
							.equals("EXECUTE"))) {
				// TODO el writing buffer
				entry.status = "WRITING";
				// al3b f el ROB
				for (int i = 0; i < Engine.getInstance().rob.table.length; i++) {
					if (Engine.getInstance().rob.table[i].instructionIndex == entry.dest) {
						if (entry.op.equals("STORE")) {
							Engine.getInstance().rob.table[i].khalstExecute = entry.hat5lsWriting;
							// System.out
							// .println("WRITE INSTRUCTION "
							// + entry.dest
							// + " time "
							// + (time
							// + Engine.getInstance().storeExecuteLatanecy -
							// 1));
							Engine.getInstance().sb
									.append("WRITE INSTRUCTION "
											+ entry.dest
											+ " time "
											+ (time
													+ Engine.getInstance().storeExecuteLatanecy - 1)
											+ "\n");
						} else {
							Engine.getInstance().sb.append("WRITE INSTRUCTION "
									+ entry.dest + " time " + time);
							// System.out.println("WRITE INSTRUCTION "
							// + entry.dest + " time " + time);
							// Engine.getInstance().rob.table[i].khalstExecute =
							// entry.hat5lsExecuting;
						}
						Engine.getInstance().rob.table[i].ready = true;
						Engine.getInstance().rob.table[i].value = Engine
								.getInstance().instructions
								.get(Engine.getInstance().rob.table[i].instructionIndex)
								.execute();
						break;
					}
				}
				int dest = entry.dest;
				for (RSentry ent : rs) {
					if (ent.busy && ent.dest != entry.dest) {
						// if (ent.qj != null && ent.qj == dest) {
						if (ent.qj != null && ent.qj == dest) {
							ent.vj = Engine.getInstance().instructions.get(
									entry.dest).getRegA();
						} else if (ent.qk != null && ent.qk == dest) {
							ent.vk = Engine.getInstance().instructions.get(
									entry.dest).getRegA();
						}
					}
				}
				registers.remove(Engine.getInstance().instructions.get(
						entry.dest).getRegA());
				deleteFromRS(entry.dest);
			}
		}
	}

	public void al3bCommit(int time) throws NumberFormatException, IOException {
		for (int i = 0; i < Engine.getInstance().rob.table.length; i++) {
			ROBentry entry = Engine.getInstance().rob.table[i];
			if (time > entry.khalstExecute + 1 && entry.ready
					&& entry.tailWa2f3alya) {
				// 7ark head we delete from ROB
				// delete from resgisters
				// System.out.println("COMMIT INSTRUCTION "
				// + entry.instructionIndex + " at time " + time);
				Engine.getInstance().sb.append("COMMIT INSTRUCTION "
						+ entry.instructionIndex + " at time " + time + "\n");
				RegisterFile.getInstance().setValueAt(
						Engine.getInstance().instructions.get(
								entry.instructionIndex).getRegA(), entry.value);
				entry.free = true;
				entry.dest = "";
				entry.ready = false;
				entry.type = "";
				entry.instructionIndex = -1;
				entry.value = -1;
				entry.tailWa2f3alya = false;
				Engine.getInstance().numberOfCommitedInstructions++;
				Engine.getInstance().rob.table[(i + 1)
						% Engine.getInstance().rob.table.length].tailWa2f3alya = true;
				break;
			}
		}
	}

	public String toString() {
		StringBuilder out = new StringBuilder();
		for (RSentry entry : rs) {
			out.append("Busy " + entry.busy + " op " + entry.op + " VJ "
					+ entry.vj + " QJ " + entry.qj + " VK " + entry.vk + " QK "
					+ entry.qk + " hat5ls emta " + entry.hat5lsExecuting
					+ " start issue " + entry.startIssue + " dest "
					+ entry.dest);
			out.append("\n");
		}

		return out.toString();

	}
}
