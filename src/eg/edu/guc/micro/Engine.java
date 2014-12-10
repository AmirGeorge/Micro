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
	private StringBuilder guiConsoleOutput;

	private int pipelineWidth;
	private int loadLatency;
	private int storeLatency;
	private int mulLatency;
	private int addLatency;
	private int logicLatency;
	private int currentCycle = 0;

	private boolean fetchFinished; // denotes no more instructions to fetch till
									// program ends
	private boolean issueFinished; // denotes no more instructions to issue till
									// program ends
	private boolean executeFinished; // denotes no more instructions to execute
										// till program ends
	private boolean writeFinished; // denotes no more instructions to write till
									// program ends
	private boolean commitFinished; // denotes no more instructions to commit
									// till program ends

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
		this.caches = new LinkedList<Cache>();
		// for (Instruction ins : instructions) {
		// System.out.println(ins);
		// }
		// caches = new LinkedList<Cache>();
		// caches.add(new Cache(8, 2, 1, WritingPolicyHit.WRITE_BACK,
		// WritingPolicyMiss.WRITE_ALLOCATE, 1, 0));
		// caches.add(new Cache(16, 2, 2, WritingPolicyHit.WRITE_THROUGH,
		// WritingPolicyMiss.WRITE_AROUND, 2, 1));
		// caches.add(new Cache(16, 2, 2, WritingPolicyHit.WRITE_THROUGH,
		// WritingPolicyMiss.WRITE_AROUND, 2, 1));
		// caches.add(new Cache(16, 2, 2, WritingPolicyHit.WRITE_BACK,
		// WritingPolicyMiss.WRITE_ALLOCATE, 20, 1));
		// caches.add(new Cache(32, 4, 2, WritingPolicyHit.WRITE_BACK,
		// WritingPolicyMiss.WRITE_ALLOCATE, 43, 2));
		// // TODO test
		memory = new Memory();
		guiConsoleOutput = new StringBuilder();
		// memory.setData(0, (short) 10);
		// memory.setData(1, (short) 20);
		// memory.setData(100, (short) 100);
		// memory.setData(101, (short) 101);
		// readCacheInputs();
		// readInstructions();
	}

	public LinkedList<Cache> getCaches() {
		return this.caches;
	}

	private void initPC() throws NumberFormatException, IOException {
		BufferedReader bfr = new BufferedReader(
				new InputStreamReader(System.in));
		// System.out.println("Enter starting address of instructions in memory");
		// setInstructionsStartingAddress(Integer.parseInt(bfr.readLine()));
		// pc = instructionsStartingAddress;
	}

	// add Instruction
	// public void readInstructions() throws IOException {
	// BufferedReader bfr = new BufferedReader(
	// new InputStreamReader(System.in));
	// String line;
	// System.out.println("Enter instructions, followed by END in a new line");
	//
	// }

	public void run() throws IOException {
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

	public void runNew() throws IOException {
		while (!commitFinished) {
			currentCycle++;
			guiConsoleOutput.append("Cycle " + currentCycle + ":\n");
			guiConsoleOutput.append("==========================\n");
			// Note: called bel 3aks 3shan maslan myb2ash wa7d fetched y7slo
			// issue fi nafs el cycle

			// commit
			guiConsoleOutput.append("Commit stage: \n");
			commit();
			guiConsoleOutput.append("------------------------\n");
			// write
			guiConsoleOutput.append("Write stage: \n");
			if (!writeFinished) {
				write();
			} else {
				guiConsoleOutput
						.append("all writing finished in previous cycles\n");
			}
			guiConsoleOutput.append("------------------------\n");
			// execute
			guiConsoleOutput.append("Execute stage: \n");
			if (!executeFinished) {
				execute();
			} else {
				guiConsoleOutput
						.append("all executing finished in previous cycles\n");
			}
			guiConsoleOutput.append("------------------------\n");
			// issue
			guiConsoleOutput.append("Issue stage: \n");
			if (!issueFinished) {
				issue();
			} else {
				guiConsoleOutput
						.append("all issuing finished in previous cycles\n");
			}
			guiConsoleOutput.append("------------------------\n");
			// fetch
			guiConsoleOutput.append("Fetch stage: \n");
			if (!fetchFinished) {
				fetch();
			} else {
				guiConsoleOutput
						.append("all fetching finished in previous cycles\n");
			}
			guiConsoleOutput.append("------------------------\n");
		}
		guiConsoleOutput.append("==========================\n");
		guiConsoleOutput.append("Program finished at cycle: " + currentCycle
				+ "\n");

		// TODO
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

	private void fetch() {
		if (getInstructionIndexFromPC() >= instructions.size()
				|| getInstructionIndexFromPC() < 0) {
			fetchFinished = true;
			guiConsoleOutput
					.append("all fetching finished in previous cycles\n");
			return;
		}
		if (InstructionBuffer.getInstance().GetNumberOfAvailablePlaces() == 0) {
			guiConsoleOutput
					.append("nothing fetched as there is no space in instruction buffer\n");
			return;
		}
		for (int i = 0; i < pipelineWidth
				&& InstructionBuffer.getInstance().GetNumberOfAvailablePlaces() > 0
				&& getInstructionIndexFromPC() < instructions.size()
				&& getInstructionIndexFromPC() >= 0; i++) {
			InstructionBuffer.getInstance().putInstruction(
					getInstructionIndexFromPC());
			guiConsoleOutput.append("Fetched instruction of index "
					+ getInstructionIndexFromPC() + "\n");
			if (instructions.get(getInstructionIndexFromPC()).getType() == InstructionType.CONTROL) {
				// TODO handle branch prediction and update PC accordingly
			} else {
				pc += 2;
			}
		}

	}

	private void issue() {
		if (fetchFinished && InstructionBuffer.getInstance().isEmpty()) {
			issueFinished = true;
			guiConsoleOutput
					.append("all issuing happened in previous cycles\n");
			return;
		}
		if (InstructionBuffer.getInstance().isEmpty()) {
			guiConsoleOutput
					.append("no issuing since instruction buffer is empty\n");
			return;
		}
		for (int i = 0; i < pipelineWidth; i++) {
			if (InstructionBuffer.getInstance().isEmpty()) {
				return;// instruction buffer got empty after issuing one or more
						// instructions
			}
			boolean ROBentryAvailable = !ROB.getInstance().isFull();
			Instruction nxtInstructionToIssue = instructions
					.get(InstructionBuffer.getInstance()
							.getFirstInstructionIndexToRemove());
			if (ROBentryAvailable
					&& ReservationStation.getInstance().insertInstruction(
							nxtInstructionToIssue)) {
				guiConsoleOutput.append("Instruction of index "
						+ InstructionBuffer.getInstance()
								.getFirstInstructionIndexToRemove()
						+ " issued successfully\n");
				InstructionBuffer.getInstance().removeInstruction();
				ReservationStation.getInstance().insertInstruction(
						nxtInstructionToIssue);
				ROB.getInstance().insertInstruction(nxtInstructionToIssue);
			} else {
				guiConsoleOutput.append("Instruction of index "
						+ InstructionBuffer.getInstance()
								.getFirstInstructionIndexToRemove()
						+ " not issued due to RS or ROB not available\n");
				break;
			}
		}

	}

	private void execute() {
		// TODO Auto-generated method stub
	}

	private void write() {
		// TODO Auto-generated method stub
		// TODO when write is finished don't change operands in other RS
		// entries, just modify InstructionState to WRITTEN
		// execute checks on instruction state of elly m3atalo if it is
		// written get operand from its rob entry

	}

	private void commit() {
		// TODO Auto-generated method stub

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

	public void readCacheInputs(String hierarchy) throws IOException {
		StringTokenizer tkn = new StringTokenizer(hierarchy);
		// start addres
		setInstructionsStartingAddress(Integer.parseInt(tkn.nextToken()));
		// Memoery Access Time
		// System.out.println("Enter the Memory access time");
		int hitTimeMemory = Integer.parseInt(tkn.nextToken());
		this.memory.setAccessTime(hitTimeMemory);
		// System.out.println("Enter the number of cache levels");
		// cache levels
		int cacheLevels = Integer.parseInt(tkn.nextToken());
		for (int i = 1; i <= cacheLevels; i++) {
			System.out
					.println("Enter cache level "
							+ i
							+ "'s by Size of cache, Block size, Associativity, Writing policy in hit, writing policy in miss and number of cycles to access data ex: 32 12 1 1 WRITE_BACK WRITE_ALLOCATE 7");
			// ex: 32 12 1 1 WRITE_BACK WRITE_ALLOCATE
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
			// caches.get(i - 1).printCache();
		}

	}

	public void readHardwareInputs(String hardware) {
		int pipelineWidth = 0;
		int instrBufferSize = 0;
		int numberOfLoads = 0;
		int numberOfStores = 0;
		int numberOfMultiply = 0;
		int numberOfAdd = 0;
		int numberOfLogic = 0;
		int robSize = 0;
		// Following are latencies of execute stage
		// Assumption: the data cache execute stage will be this input + latency
		// of data cache access
		int loadLatency = 0;
		int storeLatency = 0;
		int mulLatency = 0;
		int addLatency = 0;
		int logicLatency = 0;
		// TODO get all the above from GUI
		setPipelineWidth(pipelineWidth);
		setLoadLatency(loadLatency);
		setStoreLatency(storeLatency);
		setMulLatency(mulLatency);
		setAddLatency(addLatency);
		setLogicLatency(logicLatency);
		InstructionBuffer.getInstance().init(instrBufferSize);
		ReservationStation.getInstance().init(numberOfLoads, numberOfStores,
				numberOfMultiply, numberOfAdd, numberOfLogic);
		ROB.getInstance().init(robSize);
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

	public StringBuilder getGUIConsoleOutput() {
		return guiConsoleOutput;
	}

	public void AppendTOGUIConsoleOutput(String s) {
		guiConsoleOutput.append(s);
	}

	public int getLoadLatency() {
		return loadLatency;
	}

	public void setLoadLatency(int loadLatency) {
		this.loadLatency = loadLatency;
	}

	public int getStoreLatency() {
		return storeLatency;
	}

	public void setStoreLatency(int storeLatency) {
		this.storeLatency = storeLatency;
	}

	public int getMulLatency() {
		return mulLatency;
	}

	public void setMulLatency(int mulLatency) {
		this.mulLatency = mulLatency;
	}

	public int getAddLatency() {
		return addLatency;
	}

	public void setAddLatency(int addLatency) {
		this.addLatency = addLatency;
	}

	public int getLogicLatency() {
		return logicLatency;
	}

	public void setLogicLatency(int logicLatency) {
		this.logicLatency = logicLatency;
	}

	public int getPipelineWidth() {
		return pipelineWidth;
	}

	public void setPipelineWidth(int pipelineWidth) {
		this.pipelineWidth = pipelineWidth;
	}

	public int getInstructionIndexFromPC() {
		return (pc - instructionsStartingAddress) / 2;
	}

}
