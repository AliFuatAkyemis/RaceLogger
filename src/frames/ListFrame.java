package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;

public class ListFrame extends TemplateFrame {
	private int width = 500, height = 500;
	private JPanel panel;
	private JButton backButton, analyzeButton;
	private JTable table;
	private JScrollPane scrollPane;
	private JComboBox<String> box;

	public ListFrame() {
		//Frame
		this.setTitle("Records");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation((1920-width)/2, (1080-height)/2);
		this.setResizable(false);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Button
		backButton = new JButton("Back to Dashboard");
		backButton.setBounds(20, 10, 200, 25);
		backButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		backButton.addActionListener(e -> {
			this.dispose();
			Main.showDash();
		});

		analyzeButton = new JButton("Analyze");
		analyzeButton.setBounds(390, 10, 80, 25);
		analyzeButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		analyzeButton.addActionListener(e -> {
			
		});

		//Table
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] {"ID", "Name", "Time"});
		table = new JTable(model);
		table.setFillsViewportHeight(false);
		table.setEnabled(false);

		//TableColumnModel
		TableColumnModel columnModel = table.getColumnModel(); //Getting the instance of column model
		
		//Then editing the size seperately
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(2).setPreferredWidth(100);

		//ComboBox
		String[] names = new File("data/oldRecords").list();
		if (names == null) names = new String[0];
		box = new JComboBox<>(names);
		box.setBounds(390, 10, 100, 25);
		box.addActionListener(e -> {
			String selected = (String) box.getSelectedItem();
			updateTable(model, selected);
		});

		//ScrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 50, 350, 390);

		//Composition part
		panel.add(box);
		panel.add(backButton);
		panel.add(scrollPane);

		this.add(panel);
	}

	private void updateTable(DefaultTableModel model, String filename) {
		try {
			model.setRowCount(0);
			File file = new File("data/oldRecords/"+filename);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String row = reader.readLine();

			while (row != null) {
				String[] temp = row.split(",");
				model.addRow(new Object[] {temp[0], temp[1], temp[2]});
				row = reader.readLine();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	} 
} 
