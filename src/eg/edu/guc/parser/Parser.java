package eg.edu.guc.parser;

import eg.edu.guc.micro.Instruction;

public class Parser {

	private static Parser _instance;

	private Parser() {

	}

	public static Parser getInstance() {
		if (_instance == null)
			_instance = new Parser();
		return _instance;
	}

	public Instruction parse(String x) {
		Instruction instruction = null;
		// TODO assiuty
		return instruction;
	}

}
