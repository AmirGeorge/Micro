package eg.edu.guc.micro;

import java.io.IOException;
import java.util.Arrays;

import eg.edu.guc.parser.Parser;

public class Main {

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		//
		Parser parser = Parser.getInstance();
		if (parser
				.parse("addi $R1, $r2, 10 \n addi $R2, $r2, 20  \n sub $R2, $R2, $R1 \n beq $R1, $R2, end \n addi $r5, $r6,5 \n sw $R2, $R2, 40 \n end:") != null) {
			Engine x = Engine.getInstance();
			x.getMemory().setData(20, (short) 2);
			x.run();
			for (Cache c : Engine.getInstance().getCaches()) {
				System.out.println(Arrays.toString(c.getInstructions()));
				c.printCache();
			}
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

		//
		// x.run();

	}
}
