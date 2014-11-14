public class Memory {
	private static final int MEMORY_SIZE = 64;
	private int instructionsPointer;
	private int dataPointer;
	private int hitTime;

	public Memory(int hitTime) {
		this.hitTime = hitTime;
	}

	public Memory() {

	}

	public void setHitTime(int hitTime) {
		this.hitTime = hitTime;
	}

	public int getHitTime() {
		return this.hitTime;
	}
}
