package eg.edu.guc.gui;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JEditorPane;

import eg.edu.guc.micro.Engine;
import eg.edu.guc.micro.Memory;
import eg.edu.guc.parser.Parser;

public class Adapter {

	private static Adapter _instance;
	Window frame;

	private Adapter() {
	}

	public static Adapter getInstance() {
		if (_instance == null) {
			_instance = new Adapter();
		}
		return _instance;
	}

	public void startGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {
		Adapter x = new Adapter();
		x.startGUI();
	}

	public void startEngine(String hierarchy, String hardware, String code,
			String data) throws IOException {
		// TODO pass data to Parser and Engine
//		if (Parser.getInstance().parse(code) != null) {
//	//		Engine.getInstance().readCacheInputs(hierarchy);
//			Engine x = Engine.getInstance();
//			x.mWay = 1;
//			// x.getMemory().setData(10, (short) 100);
//			x.addExecuteLatency = 2;
//			x.loadExecuteLatanecy = 2;
//			x.storeExecuteLatanecy = 2;
//			x.multExecuteLatency = 2;
//			x.robSize = 1;
//			x.runNew();
//			Engine.getInstance().runNew();
//			frame.editorPane_output.setText(x.sb.toString());
//			System.out.println(x.sb.toString());
//		}else{
//			frame.editorPane_output.setText(Parser.getInstance().errors.toString());
//		}
//		populateGUI();
	}

	private void populateGUI() throws IOException {
		// LinkedList<Cache> caches = Engine.getInstance().getCaches();
		Memory memory = Engine.getInstance().getMemory();
		// StringBuilder sb = Engine.getInstance().getGUIConsoleOutput();

		// TODO populate caches

		// TODO populate memory

		// populate console output
		frame.editorPane_output.setText(Engine.getInstance().sb.toString());
		// frame.editorPane_code
	}
}
