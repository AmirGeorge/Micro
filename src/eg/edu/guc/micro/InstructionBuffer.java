package eg.edu.guc.micro;

import java.util.ArrayList;

public class InstructionBuffer {

	private static InstructionBuffer _instance;

	ArrayList<Integer> buffer; // array element is instruction index in
								// Instruction ArrayList
	private int size;
	int nextIndex;

	public static InstructionBuffer getInstance() {
		if (_instance == null) {
			_instance = new InstructionBuffer();
		}
		return _instance;
	}

	private InstructionBuffer() {

	}

	public void init(int size) {
		this.size = size;
		buffer = new ArrayList<Integer>(size);
		nextIndex = 0;
	}

	public int GetNumberOfAvailablePlaces() {
		return size - nextIndex;
	}

	public void putInstruction(int instructionIndex) {
		buffer.add(instructionIndex);
	}

	public int getFirstInstructionIndexToRemove() {
		return buffer.get(0);
	}

	public void removeInstruction() {
		buffer.remove(0);
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return buffer.size() == 0;
	}

}
