package eg.edu.guc.micro;

import java.io.IOException;

public class Instruction {
	private InstructionType type;
	private String instType;
	private String instructionName;
	private String regA;
	private String regB;
	private String regC;
	private short imm;
	private int label = -1;

	public String getInstType() {
		return this.instType;
	}

	public Instruction() {
	}

	public short execute() throws NumberFormatException, IOException {
		short result = 0;
		RegisterFile regFile = RegisterFile.getInstance();
		if (type == InstructionType.MEMORY_ACCESS) {
			Engine eng = Engine.getInstance();
			if (instructionName.equals("LW")) {
				result = eng.loadDataFromCaches(
						(short) (regFile.getValueAt(regB)) + imm, 0);
				// regFile.setValueAt(regA, (short) result);
			} else if (instructionName.equals("SW")) {
				result = (short) (regFile.getValueAt(regB) + imm);
				eng.writeData(0, regFile.getValueAt(regA),
						(short) (regFile.getValueAt(regB) + imm));
			}
		} else if (type == InstructionType.CONTROL) {
			Engine eng = Engine.getInstance();
			if (instructionName.equals("JMP")) {
				Engine.getInstance().hasl = true;
				Engine.getInstance().jmp = true;
				// if (label == -1)
				// eng.setPC(eng.getPC() + 1 + regFile.getValueAt(regA) + imm);
				// else
				eng.setPC(eng.getPC() + imm * 2);
				// System.out.println(eng.getPC());
			} else if (instructionName.equals("BEQ")) {
				Engine.getInstance().hasl = true;
				if (regFile.getValueAt(regA) == regFile.getValueAt(regB)) {
					if (label == -1)
						eng.setPC(eng.getPC() + 1 + imm);
					else
						eng.setPC(eng.getInstructionsStartingAddress() + label
								* 2);
				}
			} else if (instructionName.equals("JALR")) {
				// regFile.setValueAt(regA, (short) (eng.getPC() + 1));
				eng.setPC(regFile.getValueAt(regB));
			} else if (instructionName.equals("RET")) {
				eng.setPC(regFile.getValueAt(regA));
			}
		} else if (type == InstructionType.ALU) {

			if (instructionName.equals("ADD")) {
				result = (short) (regFile.getValueAt(regB) + regFile
						.getValueAt(regC));
				// regFile.setValueAt(regA,
				// (short) (regFile.getValueAt(regB) + regFile
				// .getValueAt(regC)));
			} else if (instructionName.equals("SUB")) {
				result = (short) (regFile.getValueAt(regB) - regFile
						.getValueAt(regC));
				// regFile.setValueAt(regA,
				// (short) (regFile.getValueAt(regB) - regFile
				// .getValueAt(regC)));
			} else if (instructionName.equals("ADDI")) {

				result = (short) (regFile.getValueAt(regB) + imm);
				// regFile.setValueAt(regA,
				// (short) (regFile.getValueAt(regB) + imm));
			} else if (instructionName.equals("NAND")) {
				result = (short) ~(regFile.getValueAt(regB) & regFile
						.getValueAt(regC));
				// regFile.setValueAt(regA,
				// (short) (~(regFile.getValueAt(regB) & regFile
				// .getValueAt(regC))));
			} else if (instructionName.equals("MUL")) {
				result = (short) (regFile.getValueAt(regB) * regFile
						.getValueAt(regC));
				// regFile.setValueAt(regA,
				// (short) (regFile.getValueAt(regB) * regFile
				// .getValueAt(regC)));
			}
		}
		return result;
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

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public String toString() {
		String instruction = "Instruction name : " + getInstructionName();
		instruction += "\nReg A : " + getRegA();
		if (getRegB() != null)
			instruction += "\nReg B : " + getRegB();
		if (getRegC() != null)
			instruction += "\nReg C : " + getRegC();
		instruction += "\nImmediate : " + getImm();
		instruction += "\nType : " + getType();
		instruction += "\nLabel : " + getLabel();
		return instruction;
	}
}
