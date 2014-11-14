public class Parser {

	private static Parser _instance;

	public static Parser getInstance() {
		if (_instance == null)
			_instance = new Parser();
		return _instance;
	}

	public Instruction parse(String x) {
		Instruction instruction = null;
		return instruction;
	}

}
