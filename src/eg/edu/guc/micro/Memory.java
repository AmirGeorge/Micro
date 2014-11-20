package eg.edu.guc.micro;

public class Memory {
	private static final int MEMORY_SIZE = 64;
	private static Short data[];
	private int instructionsPointer;
	private int dataPointer;
	private int accessTime;

	public Memory(int accessTime) {
		this.accessTime = accessTime;
		data = new Short[64];
	}

	public Memory() {

	}

	public void setAccessTime(int accessTime) {
		this.accessTime = accessTime;
	}

	public int getAccessTime() {
		return this.accessTime;
	}

	public static Short getData(int location) {
		return data[location];
	}
}
