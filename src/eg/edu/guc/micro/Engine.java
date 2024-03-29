package eg.edu.guc.micro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

import eg.edu.guc.parser.Parser;

public class Engine {

	private static Engine _instance;

	private Parser parser;
	private Memory memory;
	private LinkedList<Cache> caches;
	public ArrayList<Instruction> instructions;
	private int pc;
	private int numberOfExecutedInstructions = 0;
	private int numberOfCycles = 0;
	public int instructionsStartingAddress;
	public StringBuilder sb;
	private int time = 0;
	private InstructionBuffer instructionBuffer;
	public int mWay;
	private ReservationStation RS;
	public ROB rob;
	public int robSize;
	public int loadExecuteLatanecy;
	public int storeExecuteLatanecy;
	public int addExecuteLatency;
	public int multExecuteLatency;
	public int logicExecuteLatency;
	public boolean jmp;
	public int numberOfCommitedInstructions;
	public boolean hasl = false;

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
		memory = new Memory();
		sb = new StringBuilder();
		caches = new LinkedList<Cache>();
		instructionBuffer = new InstructionBuffer();
		// load store mult add logic
		// RS = new ReservationStation(1, 1, 0, 2, 0);
		// RS = new ReservationStation(0, 0, 0, 8, 0);
		// RS = new ReservationStation(0, 0, 0, 10, 0);

		// rob = new ROB(1);
		instructionsStartingAddress = 200;
		pc = instructionsStartingAddress;
		// guiConsoleOutput = new StringBuilder();
		// readCacheInputs();
		// readInstructions();
	}

	public LinkedList<Cache> getCaches() {
		return this.caches;
	}

	private int x = 0;

	public void runNew() throws NumberFormatException, IOException {
		while (true) {
			// Fetching
			while (instructionBuffer.buffer.size() < mWay
					&& pc < instructionsStartingAddress
							+ (instructions.size() * 2)) {
				instructionBuffer.buffer.add(pc);
				pc += 2;
			}
			// Issuing
			int countIssue = 0;
			boolean flag = true;
			while (countIssue < mWay && flag
					&& instructionBuffer.buffer.peek() != null) {
				int index = instructionBuffer.buffer.peek();
				if (jmp) {
					instructionBuffer.buffer.peek();
					index = pc - 2;
					jmp = false;
				}
				index = (index - instructionsStartingAddress) / 2;
				Instruction inst = instructions.get(index);
				if (canIssue(inst)) {
					instructionBuffer.buffer.poll();
					countIssue++;
					// System.out.println(inst.toString());
					inst = instructions.get(index);
					RS.issue(inst, time, index);
					rob.write(inst, index);
				} else {
					flag = false;
				}
			}
			RS.al3bCommit(time);
			RS.al3bWrite(time);
			RS.al3bExecute(time);
			if (hasl) {
				if (pc >= instructionsStartingAddress
						+ (instructions.size() * 2))
					break;
			} else if (numberOfCommitedInstructions >= instructions.size())
				break;

			if (x == 100)
				break;
			x++;
			time++;
			sb.append("Time " + time + "\n");
			sb.append(RS.toString() + "\n");
			sb.append(rob.toString() + "\n");
			sb.append(RS.registers + "\n");
			sb.append("========================================================="
					+ "\n");
		}
		System.out.println("Time " + time);
		System.out.println(RS.toString());
		System.out.println(rob.toString());
		System.out.println(RS.registers);
		System.out
				.println("=========================================================");

		sb.append("Number of commited " + numberOfCommitedInstructions + "\n");
		sb.append("Time without calculating caches " + time);
		// System.out
		// .println("number of commited  = " + numberOfCommitedInstructions);
		// System.out.println("==================END===================");
		// System.out.println("Time without calculating caches is " + time);
		// check on pc same as original run and in the loop:
		// 1)if there is space in instruction buffer fetch a max of m
		// instructions
		// 2)if instructions can be issued (ROB entry available, RS entry
		// available) issue them one by one until you fetch m instructions or
		// you encounter an instruction that can't be issued
		// 3) if instructions can be executed(operands are ready) start
		// executing them (no limit on number of instructions you can execute
		// here)
		// 4) if instructions can be written (finished execution) write only one
		// instruction(oldest in program order)
		// 5) if an instruction can be committed, commit it
	}

	private boolean canIssue(Instruction inst) {
		// if (x > 35) {
		// System.out.println("RS " + RS.hasFree(inst));
		// System.out.println("rob " + rob.hasFree());
		// }
		if (RS.hasFree(inst) && rob.hasFree()) {
			return true;
		} else
			return false;
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
		this.caches = new LinkedList<Cache>();

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

	public void readCacheInputs(String hierarchy) throws IOException {
		StringTokenizer tkn = new StringTokenizer(hierarchy);
		// start addres
		setInstructionsStartingAddress(Integer.parseInt(tkn.nextToken()));
		// Memoery Access Time
		memory.setAccessTime(Integer.parseInt(tkn.nextToken()));
		mWay = Integer.parseInt(tkn.nextToken());
		robSize = Integer.parseInt(tkn.nextToken());
		rob = new ROB(robSize);
		int load = Integer.parseInt(tkn.nextToken());
		int store = Integer.parseInt(tkn.nextToken());
		int mult = Integer.parseInt(tkn.nextToken());
		int add = Integer.parseInt(tkn.nextToken());
		int logic = Integer.parseInt(tkn.nextToken());
		RS = new ReservationStation(load, store, mult, add, logic);
		loadExecuteLatanecy = Integer.parseInt(tkn.nextToken());
		storeExecuteLatanecy = Integer.parseInt(tkn.nextToken());
		multExecuteLatency = Integer.parseInt(tkn.nextToken());
		addExecuteLatency = Integer.parseInt(tkn.nextToken());
		logicExecuteLatency = Integer.parseInt(tkn.nextToken());
		// System.out.println("Enter the number of cache levels");
		// cache levels
		int cacheLevels = Integer.parseInt(tkn.nextToken());
		for (int i = 1; i <= cacheLevels; i++) {
			System.out
					.println("Enter cache level "
							+ i
							+ "'s by Size of cache, Block size, Associativity, Writing policy in hit, writing policy in miss and number of cycles to access data ex: 32 12 1 1 WRITE_BACK WRITE_ALLOCATE 7");
			// ex: 32 12 1 WRITE_BACK WRITE_ALLOCATE
			// StringTokenizer tkn = new StringTokenizer(bfr.readLine());
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

	public int getInstructionIndexFromPC() {
		return (pc - instructionsStartingAddress) / 2;
	}

}
