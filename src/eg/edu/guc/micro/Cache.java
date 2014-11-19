package eg.edu.guc.micro;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Cache {

	private int size;
	private int blockSize;
	private int associativity;
	private int hitTime;
	private WritingPolicyHit writingPolicyHit;
	private WritingPolicyMiss writingPolicyMiss;
	private int noOfCycles;
	private int sets;

	// Instruction cache
	private int[] instruction; // array index is cache address, array element is
								// memory address of first cached instruction in
								// this block(ex: if element is 5 and block size
								// is 4 then block contains instructions whose
								// addresses in memory are 5,6,7,8 )

	// Data cache
	private HashMap<Integer, Short>[] data; // array index is cache address,
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
		this.sets = size / associativity;
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

	public int existsDataAtMemoryLocation(int location) {
		for (int i = 0; i < this.data.length; i++) {
			if (data[i].containsKey(location))
				return i;
		}
		return -1;
	}

	public Short loadDataFromCache(int dataIndex, int location) {
		return this.data[dataIndex].get(location);
	}

	public void cacheTheDataAtMemoryLocation(int location) {
		// TODO mimi
		// put the data having memory location (int location) in its
		// appropriate place in this cache
		// Note that you will cache a whole block not this data only, so handle
		// this according to block size
		int index = getCacheEntryIndex(location);
		for (int i = 0; i < blockSize; i++) {
			if (location < 64) {
				Short data = Memory.getData(location);
				this.data[index].put(location, data);
				location++;
			}
		}
	}

	public int getCacheEntryIndex(int location) {
		int blockNumber = location / this.blockSize;
		return blockNumber % this.sets;
	}

	public void printCache() {
		System.out.println("Set numbers " + sets);
		for (int i = 0; i < this.data.length; i++) {
			System.out.println("Entry " + i);
			Iterator it = this.data[i].entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				System.out.println(pairs.getKey() + " = " + pairs.getValue());
				it.remove(); // avoids a ConcurrentModificationException
			}
		}
		System.out.println();
	}
}
