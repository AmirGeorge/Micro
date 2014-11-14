package eg.edu.guc.micro;

import java.io.IOException;

public class Instruction {
	private InstructionType type;
	private String instructionName;
	private short regA;
	private short regB;
	private short regC;
	private short imm;

	public InstructionType getType() {
		return type;
	}

	public void setType(InstructionType type) {
		this.type = type;
	}

	public short getRegA() {
		return regA;
	}

	public void setRegA(short regA) {
		this.regA = regA;
	}

	public short getRegB() {
		return regB;
	}

	public void setRegB(short regB) {
		this.regB = regB;
	}

	public short getRegC() {
		return regC;
	}

	public void setRegC(short regC) {
		this.regC = regC;
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
}
