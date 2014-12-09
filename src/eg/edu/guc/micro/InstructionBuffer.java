package eg.edu.guc.micro;

import java.util.ArrayList;

public class InstructionBuffer {
	ArrayList<Integer> buffer; // array element is instruction index in
								// Instruction ArrayList
	int nextIndex;

	public InstructionBuffer(int size) {
		buffer = new ArrayList<Integer>(size);
		for (int i = 0; i < buffer.size(); i++) {
			buffer.add(-1);
		}
		nextIndex = 0;
	}

	public int GetNumberOfAvailablePlaces() {
		return buffer.size() - nextIndex;
	}

	public void putInstructions(ArrayList<Integer> instructions) {
		buffer.addAll(instructions);
	}

	public int getFirstInstructionIndexToRemove() {
		return buffer.get(0);
	}

	public void removeInstruction() {
		buffer.remove(0);
	}

}