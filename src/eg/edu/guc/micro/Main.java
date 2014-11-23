package eg.edu.guc.micro;

import java.io.IOException;

import eg.edu.guc.parser.Parser;

public class Main {

	public static void main(String[] args) throws NumberFormatException,
			IOException {

		Parser parser = Parser.getInstance();
		if (parser.parse(" AsSD:  \n"
				+ "addi $R1, $R2, 12 \n beq $R1, $R1, AsSD") != null) {
			Engine x = Engine.getInstance();
			x.run();
		}
		// System.out.println(parser.getLabels().toString());
		// System.out.println(parser.getNoLines());

		// System.out.println(8%8);
		// Engine x = Engine.getInstance();
		// Engine.getInstance().loadDataFromCaches(0);
		// Engine.getInstance().loadDataFromCaches(3);
		// Engine.getInstance().loadDataFromCaches(5);
		// Engine.getInstance().loadDataFromCaches(7);
		// Engine.getInstance().loadDataFromCaches(9);
		// Engine.getInstance().loadDataFromCaches(11);
		// x.getInstance().loadDataFromCaches(13);
		// x.getInstance().loadDataFromCaches(15);
		// x.getInstance().loadDataFromCaches(17);
		//
		// for (Cache c : Engine.getInstance().getCaches())
		// c.printCache();
		// x.run();

	}
}
