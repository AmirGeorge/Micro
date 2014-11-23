package eg.edu.guc.parser;

import java.util.ArrayList;

public class ParserError {
	private String instruction;
	private int lineNumber;
	private ArrayList<String> errorMessages;

	protected ParserError(String instruction, int lineNumber) {
		this.instruction = instruction;
		this.lineNumber = lineNumber + 1;
		errorMessages = new ArrayList<String>();
		run();
		printError();
	}

	protected ParserError(String instruction, int lineNumber, String label) {
		this.instruction = instruction;
		this.lineNumber = lineNumber + 1;
		errorMessages = new ArrayList<String>();
		errorMessages.add(notFoundLabel(label));
		printError();
	}

	private void run() {
		instructionName();
		regName();
		format();
	}

	private void instructionName() {
		String[] splittedInstruction = instruction.split("(\\s)");
		String name = "";
		for (String tem : splittedInstruction) {
			if (!tem.isEmpty()) {
				name = tem;
				break;
			}

		}
		if (!name.matches(Regex.ALL_INSTRUCTIONS.value()))
			errorMessages.add(notFoundInstruction(splittedInstruction[0]));

	}

	private void regName() {
		String[] splittedInstruction = instruction
				.split(Regex.SPLITTER.value());
		String regName = "";
		for (int i = 1; i < splittedInstruction.length; i++) {
			regName = splittedInstruction[i];
			if (!regName.isEmpty()
					&& !regName.matches(Regex.REGISTER.value() + "|"
							+ Regex.IMMEDIATE.value() + "|("
							+ Regex.LABELJUMP.value() + ")"))
				errorMessages.add(notFoundRegister(regName));
		}

	}

	private void format() {
		if (errorMessages.isEmpty())
			notWellFormatted();
	}

	private String notFoundInstruction(String insName) {
		return insName + " is not supported";
	}

	private String notFoundRegister(String regName) {
		return regName + " not found";
	}

	private String notWellFormatted() {
		return instruction + " is not well formated";
	}

	private String notFoundLabel(String label) {
		return "Label " + label + " not Found";
	}

	public String getInstruction() {
		return instruction;
	}

	public int getLineNumber() {
		return lineNumber + 1;
	}

	public ArrayList<String> getErrors() {
		return errorMessages;
	}

	public void printError() {
		System.out.println("Error in \" " + instruction + " \" at line :: "
				+ lineNumber);
		for (String error : errorMessages) {
			System.out.println(error);
		}
	}
}
