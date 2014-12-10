package eg.edu.guc.micro;

import java.io.IOException;
import java.util.Arrays;

import eg.edu.guc.parser.Parser;

public class Main {

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		// String a = "abc \n ab";
		// StringTokenizer tkn = new StringTokenizer(a);
		// while (tkn.hasMoreTokens()) {
		// System.out.println(tkn.nextToken());
		// }

		//
		Parser parser = Parser.getInstance();
		if (parser
		// .parse("addi $R1, $r2, 10 \n addi $R2, $r2, 20  \n sub $R2, $R2, $R1 \n beq $R1, $R2, end \n addi $r5, $r6,5 \n end: \n sw $R2, $R2, 40")
		// != null) {
		// .parse("addi $R2, $r2, 10 \n lw $R1, $r2, 10  \n addi $R3, $R1, 10 \n mul $R5, $R1, $R3\n sw $R5, $R2, 10")
		// != null) {
				.parse("addi $R4,$R4, 8 \n JALR $R3, $r4  \n addi $r5,$r5,23 \n beq $r5,$r5,end \n addi $r6,$r6,17 \n Ret $r3 \n end: \n addi $r6,$r6, 5") != null) {

			// 200 ADDI R4, R4, #208
			// 202 JALR R3,R4
			// 204 ADDI R5,R5,#23
			// 206 BEQ R5,R5,End
			// 208 AddI R6,R6,#17
			// Ret R3
			// End:
			// ADDI R6,R6,#5
			//
			Engine x = Engine.getInstance();
			x.getMemory().setData(20, (short) 10);
			x.getInstance().getMemory().setAccessTime(130);
			System.out.println("Location 20 before execution "
					+ x.getMemory().getData(20));
			x.getMemory().setData(20, (short) 10);
			x.run();
			for (Cache c : Engine.getInstance().getCaches()) {
				System.out.println("Instruction Cache");
				System.out.println(Arrays.toString(c.getInstructions()));
				System.out.println("====================");
				c.printCache();
			}
			System.out.println("Location 20 after execution "
					+ x.getMemory().getData(20));
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
