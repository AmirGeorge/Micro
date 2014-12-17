package eg.edu.guc.gui;

import java.awt.EventQueue;
import java.io.IOException;

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
		if (Parser.getInstance().parse(code) != null) {
			Engine.getInstance().readCacheInputs(hierarchy);
			// Engine.getInstance().run();
		}
		populateGUI();
	}

	private void populateGUI() throws IOException {
		// LinkedList<Cache> caches = Engine.getInstance().getCaches();
		Memory memory = Engine.getInstance().getMemory();
		// StringBuilder sb = Engine.getInstance().getGUIConsoleOutput();

		// TODO populate caches

		// TODO populate memory

		// populate console output
		// frame.editorPane_output.setText(sb.toString());
	}
}
