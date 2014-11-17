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

	private Memory memory;
	private LinkedList<Cache> caches;
	private ArrayList<Instruction> instructions;
	private int pc;
	private int numberOfExecutedInstructions = 0;
	private int numberOfCycles = 0;
	private int instructionsStartingAddress;

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
		caches = new LinkedList<Cache>();
		memory = new Memory();
		instructions = new ArrayList<Instruction>();
		readCacheInputs();
		readInstructions();
		readData();
	}

	public LinkedList<Cache> getCaches() {
		return this.caches;
	}

	// add Instruction
	public void readInstructions() throws IOException {
		BufferedReader bfr = new BufferedReader(
				new InputStreamReader(System.in));
		System.out.println("Enter starting address of instructions in memory");
		setInstructionsStartingAddress(Integer.parseInt(bfr.readLine()));
		pc = instructionsStartingAddress;
		String line;
		System.out.println("Enter instructions, followed by END in a new line");
		while (!(line = bfr.readLine()).equals("END")) {
			instructions.add(Parser.getInstance().parse(line));
		}
	}

	public void readData() {
		// TODO
	}

	public void run() throws NumberFormatException, IOException {
		// int instructionLatency;
		// int dataLatency;
		int previousPC = pc;
		// TODO test
		while ((pc - instructionsStartingAddress) / 2 < instructions.size()) {
			getInstructionFromCaches(pc);
			instructions.get(pc - instructionsStartingAddress).execute();
			numberOfExecutedInstructions++;
			if (pc == previousPC) { // no branch or jump occured
				pc += 2;// each instruction occupies 2 places in memory
				previousPC += 2;
			} else { // a branch or jump occured
				previousPC = pc;
			}
		}
		for (int i = 0; i < instructions.size(); i++) {
			getInstructionFromCaches(instructionsStartingAddress + (i * 2));
			instructions.get(i).execute();
			numberOfExecutedInstructions++;
		}
	}

	private void getInstructionFromCaches(int location) {
		// TODO shary
		// loop on caches from L1 to Ln until you find instruction, if not found
		// get it from memory.
		// if you find instruction in one of the caches then cache this
		// instruction in all upper caches,
		// if you get the instruction from memory then cache it in all caches
		// from Ln to L1
		// you will want to implement and use methods
		// existsInstructionAtMemoryLocation(int location)
		// and cacheTheInstructionAtMemoryLocation(int location) in Cache class
		// Note that according to the pdf each instruction occupies two memory
		// locations
	}

	public short loadDataFromCaches(int memLocation) {
		// # of cycels
		short noOfCycels = 0;
		int cacheIndex = -1;
		int dataIndex = -1;
		for (int i = 0; i < caches.size(); i++) {
			dataIndex = caches.get(i).existsDataAtMemoryLocation(memLocation);
			if (dataIndex != -1) {
				cacheIndex = i;
				break;
			}
		}
		if (cacheIndex == -1) {
			// not found in the caches
		} else {
			// found in cache index;
			int data = caches.get(cacheIndex).loadDataFromCache(dataIndex,
					memLocation);
			for (int i = 0; i < cacheIndex; i++) {
				caches.get(i).writeDataToThisCache(memLocation, data);
			}
		}
		return noOfCycels;
	}

	public void readCacheInputs() throws NumberFormatException, IOException {
		BufferedReader bfr = new BufferedReader(
				new InputStreamReader(System.in));

		System.out.println("Enter the Memory latency");
		int hitTimeMemory = Integer.parseInt(bfr.readLine());
		this.memory.setAccessTime(hitTimeMemory);
		System.out.println("Enter the number of cache levels");
		int cacheLevels = Integer.parseInt(bfr.readLine());
		for (int i = 1; i <= cacheLevels; i++) {
			System.out
					.println("Enter cache level "
							+ i
							+ "'s by Size of cache, Block size, Associativity, Hit time, Writing policy in hit, writing policy in miss and number of cycles to access data ex: 32 12 1 1 WRITE_BACK WRITE_ALLOCATE 7");
			// ex: 32 12 1 1 WRITE_BACK
			StringTokenizer tkn = new StringTokenizer(bfr.readLine());
			int size = Integer.parseInt(tkn.nextToken());
			int blockSize = Integer.parseInt(tkn.nextToken());
			int associativity = Integer.parseInt(tkn.nextToken());
			int hitTime = Integer.parseInt(tkn.nextToken());
			WritingPolicyHit writingPolicyHit = WritingPolicyHit.valueOf(tkn
					.nextToken());
			WritingPolicyMiss writingPolicyMiss = WritingPolicyMiss.valueOf(tkn
					.nextToken());
			int numberOfCycles = Integer.parseInt(tkn.nextToken());
			Cache cache = new Cache(size, blockSize, associativity, hitTime,
					writingPolicyHit, writingPolicyMiss, numberOfCycles);
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
}
