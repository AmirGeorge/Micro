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
	private int pc = 0;
	private int numberOfExecutedInstructions = 0;
	private int numberOfCycles = 0;
	private int InstructionsStartingAddress;

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
		for (int i = 0; i < instructions.size(); i++) {
			getInstructionFromCaches(InstructionsStartingAddress + (i * 2));
			instructions.get(i).execute();
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
		// TODO mimi
		// loop on caches from L1 to Ln until you find data, if not found
		// get it from memory.
		// if you find data in one of the caches then cache this
		// data in all upper caches,
		// if you get the data from memory then cache it in all caches
		// from Ln to L1
		// you will want to implement and use methods
		// existsDataAtMemoryLocation(int location)
		// and cacheTheDataAtMemoryLocation(int location) in Cache class
		return 0;
	}

	public void writeData(short data, int memLocation) {
		// TODO mimi
		// write data to caches and memory according to write policies in cases
		// of hit and miss
		// note that data is 16 bits so it will need to occupy 2 memory
		// location: memLocation and memLocation+1
		// you will want to implement and use method writeDataToThisCache(short
		// data, int memLocation) in Cache class
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
		return InstructionsStartingAddress;
	}

	private void setInstructionsStartingAddress(int instructionsStartingAddress) {
		InstructionsStartingAddress = instructionsStartingAddress;
	}
}
