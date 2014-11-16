package eg.edu.guc.micro;

import java.util.HashMap;

public class Cache {

	private int size;
	private int blockSize;
	private int associativity;
	private int hitTime;
	private WritingPolicyHit writingPolicyHit;
	private WritingPolicyMiss writingPolicyMiss;
	private int noOfCycles;

	// Instruction cache
	private int[] instruction; // array index is cache address, array element is
								// memory address of first cached instruction in
								// this block(ex: if element is 5 and block size
								// is 4 then block contains instructions whose
								// addresses in memory are 5,6,7,8 )

	// Data cache
	private HashMap<Integer, Integer>[] data; // array index is cache address,
												// hashmap key is memory address
												// of cached data, hashmap value
												// is data value(ex: if block
												// size is 4 then each hashmap
												// must have 4 entries w ykoono
												// wara ba3d fel memory)

	private int noOfAccesses = 0;
	private int noOfMisses = 0;

	// private HashMap<Integer, CacheEntry> cacheEntries;

	public Cache(int size, int blockSize, int associativity, int hitTime,
			WritingPolicyHit writingPolicyHit,
			WritingPolicyMiss writingPolicyMiss, int noOfCycles) {
		this.size = size;
		this.blockSize = blockSize;
		this.associativity = associativity;
		this.hitTime = hitTime;
		this.writingPolicyHit = writingPolicyHit;
		this.writingPolicyMiss = writingPolicyMiss;
		this.noOfCycles = noOfCycles;

		this.instruction = new int[size / blockSize];
		this.data = new HashMap[size / blockSize];

	}

	public boolean existsInstructionAtMemoryLocation(int location) {
		// TODO shary
		// check if instruction having memory location (int location) is present
		// in this cache
		return false;
	}

	public void cacheTheInstructionAtMemoryLocation(int location) {
		// TODO shary
		// put the instruction having memory location (int location) in its
		// appropriate place in this cache
	}

	public boolean existsDataAtMemoryLocation(int location) {
		// TODO mimi
		// check if data having memory location (int location) is present
		// in this cache
		return false;
	}

	public void cacheTheDataAtMemoryLocation(int location) {
		// TODO mimi
		// put the datqa having memory location (int location) in its
		// appropriate place in this cache
		// Note that you will cache a whole block not this data only, so handle
		// this according to block size
	}

	public void writeDataToThisCache(short data, int memLocation) {
		// TODO mimi
	}
}
