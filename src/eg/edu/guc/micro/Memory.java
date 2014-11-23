package eg.edu.guc.micro;

import java.util.Arrays;

public class Memory {
	private static final int MEMORY_SIZE = 64 * 1024;
	private static Short data[];
	private int instructionsPointer;
	private int dataPointer;
	private int accessTime;

	public Memory(int accessTime) {
		this.accessTime = accessTime;
		data = new Short[64 * 1024];
	}

	public Memory() {
		data = new Short[64 * 1024];
	}

	public void setAccessTime(int accessTime) {
		this.accessTime = accessTime;
	}

	public int getAccessTime() {
		return this.accessTime;
	}

	// consider empty value by Short.minValue


	public Short getData(int location) {
		if (data[location] == null)
			return null;
		return data[location];
	}

	public void setData(int index, short value) {
		data[index] = value;
	}
}
