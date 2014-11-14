package eg.edu.guc.micro;

import java.util.ArrayList;
import java.util.HashMap;

public class Cache {

	private int size;
	private int blockSize;
	private int associativity;
	private int hitTime;
	private WritingPolicy writingPolicy;
	private HashMap<Integer, CacheEntry> cacheEntries;

	public Cache(int size, int blockSize, int associativity, int hitTime,
			WritingPolicy writingPolicy) {
		this.size = size;
		this.blockSize = blockSize;
		this.associativity = associativity;
		this.hitTime = hitTime;
		this.writingPolicy = writingPolicy;
	}

	public int checkInstructionCache(Instruction instruction) {
		int instructionLatency = 0;
		// check instruction in cacheEntries
		return instructionLatency;
	}

	public int checkDataCache(ArrayList<Integer> registersData) {
		int dataLatency = 0;
		// check data in cacheEntries
		return dataLatency;
	}
}
