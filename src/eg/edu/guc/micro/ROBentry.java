package eg.edu.guc.micro;

public class ROBentry {

	String dest;
	short value;
	boolean ready;
	String type;
	int instructionIndex;
	boolean free;
	boolean tailWa2f3alya;
	int khalstExecute;
	int index;

	public ROBentry() {
		free = true;
		dest = "";
		ready = false;
		type = "";
		instructionIndex = -1;
		tailWa2f3alya = false;
	}
}
