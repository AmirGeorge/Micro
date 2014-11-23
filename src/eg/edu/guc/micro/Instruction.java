package eg.edu.guc.micro;

import java.io.IOException;

public class Instruction {
	private InstructionType type;
	private String instructionName;
	private String regA;
	private String regB;
	private String regC;
	private short imm;

	public Instruction() {

	}

	public void execute() throws NumberFormatException, IOException {
		RegisterFile regFile = RegisterFile.getInstance();
		if (type == InstructionType.MEMORY_ACCESS) {
			Engine eng = Engine.getInstance();
			if (instructionName.equals("LW")) {
				regFile.setValueAt(
						regA,
						eng.loadDataFromCaches((short) (regFile
								.getValueAt(regB)) + imm));
			} else if (instructionName.equals("SW")) {
				eng.writeData(0,regFile.getValueAt(regA),
						(short) (regFile.getValueAt(regB) + imm));
			}
		} else if (type == InstructionType.CONTROL) {
			Engine eng = Engine.getInstance();
			if (instructionName.equals("JMP")) {
				eng.setPC(eng.getPC() + 1 + regFile.getValueAt(regA) + imm);
			} else if (instructionName.equals("BEQ")) {
				if (regFile.getValueAt(regA) == regFile.getValueAt(regB)) {
					eng.setPC(eng.getPC() + 1 + imm);
				}
			} else if (instructionName.equals("JALR")) {
				regFile.setValueAt(regA, (short) (eng.getPC() + 1));
				eng.setPC(regFile.getValueAt(regB));
			} else if (instructionName.equals("RET")) {
				eng.setPC(regFile.getValueAt(regA));
			}
		} else if (type == InstructionType.ALU) {
			if (instructionName.equals("ADD")) {
				regFile.setValueAt(regA,
						(short) (regFile.getValueAt(regB) + regFile
								.getValueAt(regC)));
			} else if (instructionName.equals("SUB")) {
				regFile.setValueAt(regA,
						(short) (regFile.getValueAt(regB) - regFile
								.getValueAt(regC)));
			} else if (instructionName.equals("ADDI")) {
				regFile.setValueAt(regA,
						(short) (regFile.getValueAt(regB) + imm));
			} else if (instructionName.equals("NAND")) {
				regFile.setValueAt(regA,
						(short) (~(regFile.getValueAt(regB) & regFile
								.getValueAt(regC))));
			} else if (instructionName.equals("MUL")) {
				regFile.setValueAt(regA,
						(short) (regFile.getValueAt(regB) * regFile
								.getValueAt(regC)));
			}
		}
	}

	public InstructionType getType() {
		return type;
	}

	public void setType(InstructionType type) {
		this.type = type;
	}

	public short getImm() {
		return imm;
	}

	public void setImm(short imm) throws IOException {
		if (imm < -64 || imm > 63) {
			throw new IOException("Imm value must range from -64 to 63");
		}
		this.imm = imm;
	}

	public String getInstructionName() {
		return instructionName;
	}

	public void setInstructionName(String instructionName) {
		this.instructionName = instructionName;
	}

	public String getRegA() {
		return regA;
	}

	public void setRegA(String regA) {
		this.regA = regA;
	}

	public String getRegB() {
		return regB;
	}

	public void setRegB(String regB) {
		this.regB = regB;
	}

	public String getRegC() {
		return regC;
	}

	public void setRegC(String regC) {
		this.regC = regC;
	}
}
