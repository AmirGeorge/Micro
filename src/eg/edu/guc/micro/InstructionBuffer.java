package eg.edu.guc.micro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class InstructionBuffer {

	Queue<Integer> buffer; // array element is instruction index in
	int size; // Instruction ArrayList
	int nextIndex;

	public InstructionBuffer() {
		buffer = new LinkedList<Integer>();
	}

	// public int GetNumberOfAvailablePlaces() {
	// return buffer.length - nextIndex;
	// }
	//
	// public void putInstructions(ArrayList<Integer> instructions) {
	// buffer.addAll(instructions);
	// }
	//
	// public int getFirstInstructionIndexToRemove() {
	// return buffer.get(0);
	// }
	//
	// public void removeInstruction() {
	// buffer.remove(0);
	// }

	public int getSize() {
		return buffer.size();
	}

	public boolean isEmpty() {
		return buffer.size() == 0;
	}

}
