package eg.edu.guc.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import eg.edu.guc.micro.Instruction;
import eg.edu.guc.micro.InstructionType;

public class Parser {

	private static Parser _instance;
	private static ArrayList<ParserError> errors = new ArrayList<ParserError>();
	private static ArrayList<Instruction> _parsedcode;
	private static int lineNumber = 0;
	private static Hashtable<String, String> labels = new Hashtable<String, String>();

	public static Parser getInstance() {
		if (_instance == null)
			_instance = new Parser();
		return _instance;
	}

	// If syntax is right --> returns Arraylist<Insrtuction>
	// else return null
	public ArrayList<Instruction> parse(String submittedCode)
			throws NumberFormatException, IOException {
		ArrayList<Instruction> parsedCode = new ArrayList<Instruction>();
		for (String instruction : submittedCode.split("\n")) {
			Object newInstruction = instructionSyntax(instruction);
			lineNumber++;
			if (newInstruction == null)
				continue;
			if (newInstruction instanceof ParserError)
				errors.add((ParserError) newInstruction);
			else
				parsedCode.add((Instruction) newInstruction);
		}
		if (errors.isEmpty()) {
			_parsedcode = parsedCode;
			return _parsedcode;
		}
		return null;
	}

	private Object instructionSyntax(String instruction)
			throws NumberFormatException, IOException {
		if (instruction.isEmpty())
			return formErrors(instruction);

		instruction = instruction.toLowerCase();
		for (String regex : Regex._instructions)
			if (instruction.matches(regex)) {
				return formatInstruction(instruction);
			}
		// handles syntax errors + writing to reg $0
		return formErrors(instruction);
	}

	private Object formErrors(String instruction) {
		if (instruction.isEmpty())
			return null;
		return new ParserError(instruction, lineNumber);
	}

	private Instruction formatInstruction(String inst)
			throws NumberFormatException, IOException {
		ArrayList<String> codeLine = new ArrayList<String>();
		if (!inst.matches(Regex._instructions[7])) {
			splitter(inst, codeLine);
			return formInstruction(codeLine);
		} else {
			if (inst.matches(Regex._instructions[7])) {
				// Label handling
				splitter(inst, codeLine);
				labels.put(codeLine.get(0), String.valueOf(lineNumber));
				lineNumber--;
			} else if (false) {
				// .data .word handling
			}
		}
		return null;
	}

	private static void splitter(String inst, ArrayList<String> codeLine) {
		for (String string : inst.split(Regex.SPLITTER.value()))
			if (!string.isEmpty())
				codeLine.add(string);
	}

	private Instruction formInstruction(ArrayList<String> codeLine)
			throws NumberFormatException, IOException {
		Instruction instruction = new Instruction();
		switch (codeLine.get(0)) {
		case "lw":
		case "sw":
			loadStoreBody(instruction, codeLine);
			break;
		case "add":
		case "sub":
		case "nand":
		case "mul":
			arthimaticBody_R(instruction, codeLine);
			break;
		case "addi":
			arthimaticBody_I(instruction, codeLine);
			break;
		case "jmp":
		case "beq":
		case "jalr":
		case "ret":
			controlBody(instruction, codeLine);
			break;
		default:
			break;
		}
		return instruction;
	}

	private static void instructionName(Instruction instruction, String insName) {
		instruction.setInstructionName(insName.toUpperCase());
	}

	private static void regA(Instruction instruction, String regName) {
		instruction.setRegA(regName.toUpperCase());
	}

	private static void regB(Instruction instruction, String regName) {
		instruction.setRegB(regName.toUpperCase());
	}

	private static void regC(Instruction instruction, String regName) {
		instruction.setRegC(regName.toUpperCase());
	}

	private static void immediate(Instruction instruction, String value)
			throws NumberFormatException, IOException {
		instruction.setImm(Short.parseShort(value));
	}

	private static void label(Instruction instruction, int label) {
		instruction.setLabel(label);
	}

	private static void loadStoreBody(Instruction instruction,
			ArrayList<String> codeLine) throws NumberFormatException,
			IOException {
		instructionName(instruction, codeLine.get(0));
		regA(instruction, codeLine.get(1));
		regB(instruction, codeLine.get(2));
		immediate(instruction, codeLine.get(3));
		instruction.setType(InstructionType.MEMORY_ACCESS);
	}

	private static void arthimaticBody_R(Instruction instruction,
			ArrayList<String> codeLine) {
		instructionName(instruction, codeLine.get(0));
		regA(instruction, codeLine.get(1));
		regB(instruction, codeLine.get(2));
		regC(instruction, codeLine.get(3));
		instruction.setType(InstructionType.ALU);
	}

	private static void arthimaticBody_I(Instruction instruction,
			ArrayList<String> codeLine) throws NumberFormatException,
			IOException {
		instructionName(instruction, codeLine.get(0));
		regA(instruction, codeLine.get(1));
		regB(instruction, codeLine.get(2));
		immediate(instruction, codeLine.get(3));
		instruction.setType(InstructionType.ALU);
	}

	private static void controlBody(Instruction instruction,
			ArrayList<String> codeLine) throws NumberFormatException,
			IOException {
		instructionName(instruction, codeLine.get(0));
		instruction.setType(InstructionType.CONTROL);
		switch (codeLine.get(0)) {
		case "jmp":
			if (codeLine.size() == 3) {
				regA(instruction, codeLine.get(1));
				immediate(instruction, codeLine.get(2));
			} else {
				if (labels.containsKey(codeLine.get(1).toLowerCase()))
					label(instruction, Integer.parseInt(labels.get(codeLine
							.get(1).toLowerCase())));
				else
					errors.add(new ParserError(buildInstruction(codeLine),
							lineNumber, codeLine.get(1)));
			}
			break;
		case "beq":
			regA(instruction, codeLine.get(1));
			regB(instruction, codeLine.get(2));
			if (!codeLine.get(3).matches(Regex.LABELJUMP.value()))
				immediate(instruction, codeLine.get(3));
			else {
				if (labels.containsKey(codeLine.get(3).toLowerCase()))
					label(instruction, Integer.parseInt(labels.get(codeLine
							.get(3).toLowerCase())));
				else
					errors.add(new ParserError(buildInstruction(codeLine),
							lineNumber, codeLine.get(3)));
			}
			break;
		case "jalr":
			regA(instruction, codeLine.get(1));
			regB(instruction, codeLine.get(2));
			break;
		case "ret":
			regA(instruction, codeLine.get(1));
			break;
		}
	}

	public int getNoLines() {
		return lineNumber;
	}

	public Hashtable<String, String> getLabels() {
		return labels;
	}

	public ArrayList<Instruction> getParsedCode() {
		return _parsedcode;
	}

	private static String buildInstruction(ArrayList<String> codeLine) {
		String instruction = codeLine.get(0) + " ";
		for (int i = 1; i < codeLine.size(); i++) {
			instruction += codeLine.get(i);
			if (!(i + 1 == codeLine.size()))
				instruction += ", ";
		}
		return instruction;
	}
}
