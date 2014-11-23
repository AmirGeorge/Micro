package eg.edu.guc.parser;

public enum Regex {

	// Variables
	SEPARATOR("[\\s]*,[\\s]*"), SPACE("[\\s]*"), SPLITTER("(\\s)|(,)|(:)"),

	// Instruction name
	LOAD_STORE(SPACE.value + "(lw|sw)" + SPACE.value), UNCONDITIONAL_BRANCH(
			SPACE.value + "(jmp)" + SPACE.value), CONDITIONAL_BRANCH(
			SPACE.value + "(beq)" + SPACE.value), ARTHIMATIC_R(SPACE.value
			+ "(add|sub|nand|mul)" + SPACE.value), ARTHIMATIC_I(SPACE.value
			+ "(addi)" + SPACE.value), CALL_RETURN(SPACE.value + "(ret)"
			+ SPACE.value), JUMP_AND_LINK(SPACE.value + "(jalr)" + SPACE.value), LABEL(
			"[\\s]*[a-zA-Z0-9]*[\\s]*:[\\s]*"), ALL_INSTRUCTIONS(SPACE.value
			+ "(lw|sw|jmp|add|sub|nand|mul|beq|addi|ret|jalr|)" + SPACE.value), LABELJUMP(
			"[a-zA-Z0-9]*"), DATA("[\\s]*(.data)[\\s]*"),

	// REGISTERs names
	REGISTER("([$]([Rr][0-7]))"), IMMEDIATE(
			"([-]([0-5][\\d]|[1-9]|[6][0-4])|[+]?([0-5]?[\\d]|[6][0-3]))");

	private final String value;

	// Supported Instruction Bodies
	private static final String[] bodies = {
			REGISTER.value + SEPARATOR.value + REGISTER.value + SEPARATOR.value
					+ IMMEDIATE.value + SPACE.value,
			REGISTER.value + SEPARATOR.value + REGISTER.value + SEPARATOR.value
					+ REGISTER.value + SPACE.value,
			REGISTER.value + SEPARATOR.value + IMMEDIATE.value + SPACE.value,
			REGISTER.value + SEPARATOR.value + REGISTER.value + SPACE.value,
			REGISTER.value + SPACE.value,
			SPACE.value + LABELJUMP.value + SPACE.value,
			REGISTER.value + SEPARATOR.value + REGISTER.value + SEPARATOR.value
					+ LABELJUMP.value + SPACE.value };

	// Instructions
	protected static final String[] _instructions = {
			LOAD_STORE.value + bodies[0], CONDITIONAL_BRANCH.value + bodies[0],
			UNCONDITIONAL_BRANCH.value + bodies[2],
			JUMP_AND_LINK.value + bodies[3], CALL_RETURN.value + bodies[4],
			ARTHIMATIC_R.value + bodies[1], ARTHIMATIC_I.value + bodies[0],
			LABEL.value(), CONDITIONAL_BRANCH.value + bodies[6],
			UNCONDITIONAL_BRANCH.value + bodies[5] };

	// Constructor
	Regex(String regex) {
		this.value = regex;
	}

	protected String value() {
		return value;
	}

}
