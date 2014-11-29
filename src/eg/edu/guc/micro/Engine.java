package eg.edu.guc.micro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

import eg.edu.guc.parser.Parser;

public class Engine {

	private static Engine _instance;

	private Parser parser;
	private Memory memory;
	private LinkedList<Cache> caches;
	private ArrayList<Instruction> instructions;
	private int pc;
	private int numberOfExecutedInstructions = 0;
	private int numberOfCycles = 0;
	private int instructionsStartingAddress;
	private StringBuilder sb;

	private Engine() {

	}

	public static Engine getInstance() throws NumberFormatException,
			IOException {
		if (_instance == null) {
			_instance = new Engine();
			_instance.init();
		}
		return _instance;
	}

	private void init() throws NumberFormatException, IOException {
		parser = Parser.getInstance();
		instructions = parser.getParsedCode();
		for (Instruction ins : instructions) {
			System.out.println(ins);
		}
		caches = new LinkedList<Cache>();
		caches.add(new Cache(8, 2, 1, WritingPolicyHit.WRITE_BACK,
				WritingPolicyMiss.WRITE_ALLOCATE, 1, 0));
		// caches.add(new Cache(16, 2, 2, WritingPolicyHit.WRITE_THROUGH,
		// WritingPolicyMiss.WRITE_AROUND, 2, 1));
		// caches.add(new Cache(16, 2, 2, WritingPolicyHit.WRITE_BACK,
		// WritingPolicyMiss.WRITE_ALLOCATE, 20, 1));
		// caches.add(new Cache(32, 4, 2, WritingPolicyHit.WRITE_BACK,
		// WritingPolicyMiss.WRITE_ALLOCATE, 43, 2));
		// // TODO test
		memory = new Memory();
		sb = new StringBuilder();
		// memory.setData(0, (short) 10);
		// memory.setData(1, (short) 20);
		// memory.setData(100, (short) 100);
		// memory.setData(101, (short) 101);
		// readCacheInputs();
		// readInstructions();
		initPC();
	}

	public LinkedList<Cache> getCaches() {
		return this.caches;
	}

	private void initPC() throws NumberFormatException, IOException {
		BufferedReader bfr = new BufferedReader(
				new InputStreamReader(System.in));
		System.out.println("Enter starting address of instructions in memory");
		setInstructionsStartingAddress(Integer.parseInt(bfr.readLine()));
		pc = instructionsStartingAddress;
	}

	// add Instruction
	// public void readInstructions() throws IOException {
	// BufferedReader bfr = new BufferedReader(
	// new InputStreamReader(System.in));
	// String line;
	// System.out.println("Enter instructions, followed by END in a new line");
	//
	// }

	public void run() throws NumberFormatException, IOException {
		int previousPC = pc;
		while ((pc - instructionsStartingAddress) / 2 < instructions.size()) {
			getInstructionFromCaches(pc);
			instructions.get((pc - instructionsStartingAddress) / 2).execute();
			numberOfExecutedInstructions++;
			if (pc == previousPC) { // no branch or jump occured
				pc += 2;// each instruction occupies 2 places in memory
				previousPC += 2;
			} else { // a branch or jump occured
				previousPC = pc;
			}
		}

		// for (int i = 0; i < this.caches.size(); i++) {
		// boolean[] flags = caches.get(i).getDirtyFlags();
		// for (int j = 0; j < flags.length; j++) {
		// if(flags[j]){
		// // int location = caches.get(i).getData()[j].ge;
		// writeData(i+1, newValue, memLocation);
		// }
		// }
		// }
		System.out.println(numberOfExecutedInstructions);
		System.out.println("AMAT (Number of cycels) " + numberOfCycles);

	}

	private void getInstructionFromCaches(int location) {
		int index = caches.size();
		for (int c = 0; c < caches.size(); c++) {
			if (caches.get(c).existsInstructionAtMemoryLocation(location)) {
				index = c;
				numberOfCycles += caches.get(c).getNoOfCycles();
				caches.get(c).setNoOfAccesses(
						caches.get(c).getNoOfAccesses() + 1);
				break;
			}
		}
		for (int up = index - 1; up >= 0; up--) {
			numberOfCycles += caches.get(up).getNoOfCycles();
			caches.get(up).cacheTheInstructionAtMemoryLocation(location);
			caches.get(up)
					.setNoOfAccesses(caches.get(up).getNoOfAccesses() + 1);
			caches.get(up).setNoOfMisses(caches.get(up).getNoOfMisses() + 1);
		}
		if (index == caches.size()) {
			numberOfCycles += memory.getAccessTime();
		}
	}

	public short loadDataFromCaches(int memLocation, int startCache)
			throws NumberFormatException, IOException {
		// # of cycels
		// short noOfCycels = 0;
		int cacheIndex = -1;
		int dataIndex = -1;
		short data = Short.MIN_VALUE;
		for (int i = startCache; i < caches.size(); i++) {
			numberOfCycles += caches.get(i).getNoOfCycles();
			caches.get(i).setNoOfAccesses(caches.get(i).getNoOfAccesses() + 1);
			dataIndex = caches.get(i).existsDataAtMemoryLocation(memLocation);
			if (dataIndex != -1) {
				cacheIndex = i;
				break;
			} else {
				caches.get(i).setNoOfMisses(caches.get(i).getNoOfMisses() + 1);
			}
		}
		if (cacheIndex == -1) {
			// not found in the caches,check the memory
			numberOfCycles += memory.getAccessTime();
			boolean found = false;
			if (memory.getData(memLocation) == null)
				System.out.println("No such data");
			else {
				data = memory.getData(memLocation);
				found = true;
			}
			if (found) {
				for (int i = 0; i < caches.size(); i++) {
					caches.get(i).cacheTheDataAtMemoryLocation(memLocation,
							Short.MIN_VALUE);
				}
			}
		} else {
			for (int i = 0; i < cacheIndex; i++) {
				caches.get(i).cacheTheDataAtMemoryLocation(memLocation,
						Short.MIN_VALUE);
			}
		}
		return data;
	}

	public void writeData(int index, short newValue, int memLocation)
			throws NumberFormatException, IOException {
		// memory-level
		if (index == caches.size()) {
			numberOfCycles += memory.getAccessTime();
			memory.setData(memLocation, newValue);
			return;
		}
		int dataIndex = -1;
		dataIndex = caches.get(index).existsDataAtMemoryLocation(memLocation);
		Cache cache = caches.get(index);
		if (dataIndex != -1) {
			if (cache.getWritingPolicyHit().equals(
					WritingPolicyHit.WRITE_THROUGH)) {
				caches.get(index).getData()[dataIndex].put(memLocation,
						newValue);
				writeData(index + 1, newValue, memLocation);
			} else {// Write back
				caches.get(index).getData()[dataIndex].put(memLocation,
						newValue);
				caches.get(index).setDirtyFlag(dataIndex);
			}
		} else {
			// Cache miss
			caches.get(index).setNoOfAccesses(
					caches.get(index).getNoOfAccesses() + 1);
			caches.get(index).setNoOfMisses(
					caches.get(index).getNoOfMisses() + 1);
			if (cache.getWritingPolicyMiss().equals(
					WritingPolicyMiss.WRITE_AROUND)
					&& cache.getWritingPolicyHit().equals(
							WritingPolicyHit.WRITE_THROUGH)) {
				// System.out.println(Arrays.toString(caches.get(index).getData())+" mi");
				caches.get(index).cacheTheDataAtMemoryLocation(memLocation,
						newValue);
				// System.out.println(Arrays.toString(caches.get(index).getData()));
				// System.out.println("MImiMimiMimi");
				writeData(index + 1, newValue, memLocation);
			} else // if (cache.getWritingPolicyMiss().equals(
			// WritingPolicyMiss.WRITE_ALLOCATE)
			// && cache.getWritingPolicyHit().equals(
			// WritingPolicyHit.WRITE_THROUGH))
			if (cache.getWritingPolicyMiss().equals(
					WritingPolicyMiss.WRITE_ALLOCATE)) {
				loadDataFromCaches(memLocation, index + 1);
				// caches.get(index).cacheTheDataAtMemoryLocation(memLocation,
				// newValue);
				writeData(index, newValue, memLocation);
				// } else if (cache.getWritingPolicyMiss().equals(
				// WritingPolicyMiss.WRITE_ALLOCATE)
				// && cache.getWritingPolicyHit().equals(
				// WritingPolicyHit.WRITE_BACK)) {
				// int data = loadDataFromCaches(memLocation, index + 1);
				// caches.get(index).cacheTheDataAtMemoryLocation(memLocation,
				// newValue);
				// writeData(index + 1, newValue, memLocation);
			}
		}
	}

	public void readCacheInputs() throws NumberFormatException, IOException {
		BufferedReader bfr = new BufferedReader(
				new InputStreamReader(System.in));

		System.out.println("Enter the Memory access time");
		int hitTimeMemory = Integer.parseInt(bfr.readLine());
		this.memory.setAccessTime(hitTimeMemory);
		System.out.println("Enter the number of cache levels");
		int cacheLevels = Integer.parseInt(bfr.readLine());
		for (int i = 1; i <= cacheLevels; i++) {
			System.out
					.println("Enter cache level "
							+ i
							+ "'s by Size of cache, Block size, Associativity, Writing policy in hit, writing policy in miss and number of cycles to access data ex: 32 12 1 1 WRITE_BACK WRITE_ALLOCATE 7");
			// ex: 32 12 1 1 WRITE_BACK
			StringTokenizer tkn = new StringTokenizer(bfr.readLine());
			int size = Integer.parseInt(tkn.nextToken());
			int blockSize = Integer.parseInt(tkn.nextToken());
			int associativity = Integer.parseInt(tkn.nextToken());
			WritingPolicyHit writingPolicyHit = WritingPolicyHit.valueOf(tkn
					.nextToken());
			WritingPolicyMiss writingPolicyMiss = WritingPolicyMiss.valueOf(tkn
					.nextToken());
			int numberOfCycles = Integer.parseInt(tkn.nextToken());
			Cache cache = new Cache(size, blockSize, associativity,
					writingPolicyHit, writingPolicyMiss, numberOfCycles, i - 1);
			this.caches.add(cache);
		}

	}

	public void displayMemory() {
		// TODO
	}

	public void displayCaches() {
		// TODO
	}

	public int getPC() {
		return pc;
	}

	public void setPC(int pc) {
		this.pc = pc;
	}

	public int getInstructionsStartingAddress() {
		return instructionsStartingAddress;
	}

	private void setInstructionsStartingAddress(int instructionsStartingAddress)
			throws IOException {
		if (instructionsStartingAddress % 2 != 0) {
			throw new IOException("Instruction starting address must be even");
		}
		this.instructionsStartingAddress = instructionsStartingAddress;
	}

	public int getNumberOfExecutedInstructions() {
		return numberOfExecutedInstructions;
	}

	public int getNumberOfCycles() {
		return numberOfCycles;
	}

	public void AddToNumberOfCycles(int n) {
		this.numberOfCycles = this.numberOfCycles + n;
	}

	public Memory getMemory() {
		return this.memory;
	}

	public int getAMAT() {
		return this.numberOfCycles;
	}

	public StringBuilder getSb() {
		return sb;
	}

	public void AppendTOSb(String s) {
		sb.append(s);
	}

}
