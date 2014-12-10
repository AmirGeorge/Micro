package eg.edu.guc.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Window extends JFrame {

	private JPanel contentPane;
	JTabbedPane tabbedPane_caches;
	private JButton btn_run_1;
	JEditorPane editorPane_hierarchy;
	JEditorPane editorPane_hardware;
	JEditorPane editorPane_code;
	JEditorPane editorPane_data;
	JEditorPane editorPane_output;
	JTable table_memory;
	JButton btn_run;
	JPanel panel_optons;

	/**
	 * Create the frame.
	 */
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(230, 70, 1024, 768);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_container = new JPanel();
		panel_container.setBounds(0, 0, 1018, 739);
		panel_container.setBackground(Color.LIGHT_GRAY);
		contentPane.add(panel_container);
		panel_container.setLayout(null);

		panel_optons = new JPanel();
		panel_optons.setBackground(Color.GRAY);
		panel_optons.setBounds(0, 0, 1018, 31);
		panel_container.add(panel_optons);
		panel_optons.setLayout(null);

		// JScrollPane scrollPane_caches = new JScrollPane();
		// scrollPane_caches.setBounds(618, 32, 400, 707);
		// panel_container.add(scrollPane_caches);

		// tabbedPane_caches = new JTabbedPane(JTabbedPane.TOP);
		// tabbedPane_caches.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		// scrollPane_caches.setViewportView(tabbedPane_caches);

		JTabbedPane tabbedPane_input = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_input.setBounds(0, 32, 608, 486);
		panel_container.add(tabbedPane_input);

		JScrollPane scrollPane_hierarchy = new JScrollPane();
		tabbedPane_input.addTab("Memory Hierarchy", null, scrollPane_hierarchy,
				null);

		editorPane_hierarchy = new JEditorPane();
		scrollPane_hierarchy.setViewportView(editorPane_hierarchy);

		JScrollPane scrollPane_hardware = new JScrollPane();
		tabbedPane_input.addTab("Hardware Organization", null,
				scrollPane_hardware, null);

		editorPane_hardware = new JEditorPane();
		scrollPane_hardware.setViewportView(editorPane_hardware);

		JScrollPane scrollPane_code = new JScrollPane();
		tabbedPane_input.addTab("Code", null, scrollPane_code, null);

		editorPane_code = new JEditorPane();
		scrollPane_code.setViewportView(editorPane_code);

		JScrollPane scrollPane_data = new JScrollPane();
		tabbedPane_input.addTab("Data", null, scrollPane_data, null);

		editorPane_data = new JEditorPane();
		scrollPane_data.setViewportView(editorPane_data);

		JTabbedPane tabbedPane_output = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_output.setBounds(10, 517, 598, 211);
		panel_container.add(tabbedPane_output);

		JScrollPane scrollPane_output = new JScrollPane();
		tabbedPane_output.addTab("Console Output", null, scrollPane_output,
				null);

		editorPane_output = new JEditorPane();
		scrollPane_output.setViewportView(editorPane_output);

		tabbedPane_caches = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_caches.setBounds(618, 32, 400, 707);
		panel_container.add(tabbedPane_caches);

		populateCacheTabs();

		JScrollPane scrollPane_memory = new JScrollPane();
		tabbedPane_caches.addTab("Memory", null, scrollPane_memory, null);

		// TODO fix, gives java heap size error in designer
		Integer[][] values = new Integer[1024 * 64][2];
		String[] columns2 = { "Address", "Data" };
		table_memory = new JTable(values, columns2);
		// table_memory = new JTable();
		scrollPane_memory.setViewportView(table_memory);

		initRunButton();
	}

	private void initRunButton() {
		btn_run = new JButton("Run");
		BufferedImage buttonIcon;
		try {
			buttonIcon = ImageIO.read(new File("res/img/icon_run.png"));
			btn_run_1 = new JButton(new ImageIcon(buttonIcon));
			btn_run_1.setHorizontalAlignment(SwingConstants.LEFT);
			btn_run_1.setBorder(BorderFactory.createEmptyBorder());
			btn_run_1.setContentAreaFilled(false);
			btn_run_1.setBounds(0, 0, 89, 23);
			panel_optons.add(btn_run_1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// btn_run_1.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// }
		// });
		btn_run_1.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				try {

					Adapter.getInstance().startEngine(
							editorPane_hierarchy.getText(),
							editorPane_hardware.getText(),
							editorPane_code.getText(),
							editorPane_data.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
	}

	private void populateCacheTabs() {
		for (int i = 0; i < 3; i++) {
			JPanel jp = new JPanel();
			JLabel jl = new JLabel();
			jl.setText("Dah L" + (i + 1));
			jp.add(jl);
			tabbedPane_caches.add("L" + (i + 1), jp);
		}

	}
}
