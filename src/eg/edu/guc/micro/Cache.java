package eg.edu.guc.micro;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cache {

	// in write policies mynf3sh back w around, y
	private int size;
	private int blockSize;
	private int associativity;
	private int hitTime;
	private WritingPolicyHit writingPolicyHit;
	private WritingPolicyMiss writingPolicyMiss;
	private int noOfCycles;
	private int sets;
	private int index;

	// Instruction cache
	private int[] instruction; // array index is cache address, array element is
								// memory address of first cached instruction in
								// this block(ex: if element is 5 and block size
								// is 4 then block contains instructions whose
								// addresses in memory are 5,6,7,8 )

	// Data cache
	private LinkedHashMap<Integer, Short>[] data; // array index is cache
													// address,
													// hashmap key is memory
													// address
													// of cached data, hashmap
													// value
													// is data value(ex: if
													// block
													// size is 4 then each
													// hashmap
													// must have 4 entries w
													// ykoono
													// wara ba3d fel memory)

	private boolean[] dirtyFlags;
	private boolean[] blocksFlags;
	private int noOfAccesses = 0;
	private int noOfMisses = 0;

	// private HashMap<Integer, CacheEntry> cacheEntries;

	public Cache(int size, int blockSize, int associativity,
			WritingPolicyHit writingPolicyHit,
			WritingPolicyMiss writingPolicyMiss, int noOfCycles, int index) {
		this.size = size;
		this.blockSize = blockSize;
		this.associativity = associativity;
		this.writingPolicyHit = writingPolicyHit;
		this.writingPolicyMiss = writingPolicyMiss;
		this.noOfCycles = noOfCycles;
		this.sets = (size / blockSize) / associativity;
		this.instruction = new int[size / blockSize];
		this.data = new LinkedHashMap[size / blockSize];
		Arrays.fill(instruction, -1);
		this.blocksFlags = new boolean[size / blockSize];
		this.dirtyFlags = new boolean[size / blockSize];
		this.index = index;
		for (int i = 0; i < data.length; i++)
			data[i] = new LinkedHashMap<Integer, Short>();
	}

	public boolean existsInstructionAtMemoryLocation(int location) {
		int blockNumber = location / blockSize;
		int setIndex = blockNumber % sets;
		int instCapacity = blockSize / 2;
		int startAddress = location - (location % blockSize);
		for (int index = setIndex * associativity; index < (setIndex + 1)
				* associativity; index++) {
			if (instruction[index] != -1 && startAddress >= instruction[index]
					&& instruction[index] + (instCapacity) - 1 >= startAddress)
				return true;
		}
		return false;
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

	public void cacheTheInstructionAtMemoryLocation(int location) {
		int blockNumber = location / blockSize;
		int setIndex = blockNumber % sets;
		boolean cached = false;
		int startAddress = location - (location % blockSize);
		for (int index = setIndex * associativity; index < (setIndex + 1)
				* associativity; index++) {
			if (instruction[index] == -1 && !cached) {
				cached = true;
				instruction[index] = startAddress;
				break;
			}
		}
		if (!cached) {
			instruction[setIndex * associativity] = startAddress;
		}
	}

	public void cacheTheDataAtMemoryLocation(int location, short newData)
			throws NumberFormatException, IOException {
		if (existsDataAtMemoryLocation(location) == -1) {
			int setIndex = getCacheEntryIndex(location);
			int start = setIndex * associativity;
			int end = (setIndex + 1) * associativity;
			boolean cached = false;
			for (int i = start; i <= end; i++) {
				if (!blocksFlags[i]) {
					int startAddress = location - (location % blockSize);
					for (int j = 0; j < blockSize; j++) {
						if (newData != Short.MIN_VALUE) {
							this.data[i].put(startAddress, newData);
						} else {
							this.data[i].put(startAddress, Engine.getInstance()
									.getMemory().getData(startAddress));
						}
						startAddress++;
					}
					blocksFlags[i] = true;
					cached = true;
					break;
				}
			}
			if (!cached) {
				int startAddress = location - (location % blockSize);
				if (dirtyFlags[start]) {
					// WRITE IN memory before replace
					if (index == Engine.getInstance().getCaches().size())
						for (int key : data[start].keySet()) {
							Engine.getInstance().getMemory()
									.setData(key, data[start].get(key));
						}
					else {
						Engine.getInstance().writeData(this.index + 1, newData,
								location);
					}
				}
				this.data[start].clear();
				for (int j = 0; j < blockSize; j++) {
					if (newData != Short.MIN_VALUE)
						this.data[start].put(startAddress, newData);
					else
						this.data[start].put(startAddress, Engine.getInstance()
								.getMemory().getData(startAddress));
					startAddress++;
					blocksFlags[start] = true;
				}
			}
		}
	}

	public int getCacheEntryIndex(int location) {
		int blockNumber = location / this.blockSize;
		return blockNumber % this.sets;
	}

	public void printCache() {
		System.out.println("Data Cache");
		System.out.println("Access" + noOfAccesses);
		System.out.println("Misses" + noOfMisses);
		System.out.println("Hit Ratio " + getHitRatio());
		int blocks = 0;
		int entries = 0;
		for (int i = 0; i < sets; i++) {
			System.out.println("Set " + i);
			for (int j = 0; j < associativity; j++) {
				System.out.println(" Block " + blocks);
				Iterator it = this.data[blocks].entrySet().iterator();
				for (int k = 0; k < blockSize; k++) {
					while (it.hasNext()) {
						System.out.println("  Entry " + entries);
						Map.Entry pairs = (Map.Entry) it.next();
						System.out.println("   data of " + pairs.getKey()
								+ " = " + pairs.getValue() + " ");
						it.remove();
						entries++;

					}

				}
				blocks++;
			}
		}
		System.out.println("====================");

	}

	public int getNoOfCycles() {
		return noOfCycles;
	}

	public int getNoOfAccesses() {
		return noOfAccesses;
	}

	public void setNoOfAccesses(int noOfAccesses) {
		this.noOfAccesses = noOfAccesses;
	}

	public int getNoOfMisses() {
		return noOfMisses;
	}

	public void setNoOfMisses(int noOfMisses) {
		this.noOfMisses = noOfMisses;
	}

	public WritingPolicyHit getWritingPolicyHit() {
		return this.writingPolicyHit;
	}

	public WritingPolicyMiss getWritingPolicyMiss() {
		return this.writingPolicyMiss;
	}

	public LinkedHashMap<Integer, Short>[] getData() {
		return this.data;
	}

	public void setDirtyFlag(int index) {
		this.dirtyFlags[index] = true;
	}

	public void cachData(short newValue, int memLocation) {
		// TODO Auto-generated method stub

	}

	public double getHitRatio() {
		return (noOfAccesses - noOfMisses + 0.0) / noOfAccesses;
	}

	public int[] getInstructions() {
		return instruction;
	}

	public boolean[] getDirtyFlags() {
		return this.dirtyFlags;
	}
}
