import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Engine {

	private Memory memory;
	private LinkedList<Cache> caches;
	private ArrayList<Instruction> instructions;

	public Engine() throws NumberFormatException, IOException {
		caches = new LinkedList<Cache>();
		memory = new Memory();
		instructions = new ArrayList<Instruction>();
		readCacheInputs();
		readInstructions();
	}

	public LinkedList<Cache> getCaches() {
		return this.caches;
	}

	// add Instruction
	public void readInstructions() throws IOException {
		BufferedReader bfr = new BufferedReader(
				new InputStreamReader(System.in));
		String line;
		System.out.println("Enter instructions, followed by END in a new line");
		while (!(line = bfr.readLine()).equals("END")) {
			instructions.add(Parser.getInstance().parse(line));
		}
	}

	public void run() {
		int instructionLatency;
		int dataLatency;
		for (Instruction instruction : instructions) {
			// check Instruction Cache
			// check Data Cache
		}
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
							+ "'s by Size of cache, Block size, Associativity, Hit time, and Writing policy ex: 32 12 1 1 WRITE_BACK");
			// ex: 32 12 1 1 WRITE_BACK
			StringTokenizer tkn = new StringTokenizer(bfr.readLine());
			int size = Integer.parseInt(tkn.nextToken());
			int blockSize = Integer.parseInt(tkn.nextToken());
			int associativity = Integer.parseInt(tkn.nextToken());
			int hitTime = Integer.parseInt(tkn.nextToken());
			WritingPolicy writingPolicy = WritingPolicy
					.valueOf(tkn.nextToken());
			Cache cache = new Cache(size, blockSize, associativity, hitTime,
					writingPolicy);
			this.caches.add(cache);
		}

	}

	public void displayMemory() {

	}
}
