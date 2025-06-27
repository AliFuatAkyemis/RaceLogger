package frames;

import controller.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

public class ListFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 500, height = 500, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private String selected;
	private JPanel panel;
	private JButton back, analyze, delete;
	private JTable table;
	private JScrollPane scrollPane;
	private JComboBox<String> box;

	public ListFrame() {
		//Frame
		this.setTitle("Past Records");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
                this.setLocation(x, y);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Table
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(new String[] {"ID", "Name", "Time", "Laps"});
		table = new JTable(tableModel);
		table.setFillsViewportHeight(false);
		table.setEnabled(false);

		//TableColumnModel
		TableColumnModel columnModel = table.getColumnModel(); //Getting the instance of column model
		
		//Then editing the size seperately
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(250);
		columnModel.getColumn(2).setPreferredWidth(100);
		columnModel.getColumn(3).setPreferredWidth(50);

		//Avoiding drag movement of columns
		table.setTableHeader(new JTableHeader(columnModel) {
			@Override
			public void setDraggedColumn(TableColumn column) {} 
		});

		//Button
		back = new JButton("Back");
		back.setBounds(20, 10, 80, 25);
		back.setFont(super.defaultPlainFont);
		back.addActionListener(e -> {
			Main.closeAnalyze(); //Maybe analyze screen would be opened so it should be closed
			this.dispose();
			Main.showDash();
		});

		analyze = new JButton("Analyze");
		analyze.setBounds(380, 10, 100, 25);
		analyze.setFont(super.defaultPlainFont);
		analyze.addActionListener(e -> {
			if (selected != null) Main.showAnalyze(selected);
		});

		delete = new JButton("Delete");
		delete.setBounds(20, 430, 90, 25);
		delete.setFont(super.defaultPlainFont);
		delete.addActionListener(e -> {
			if (selected != null) { //If a file selected ask for deletion
				int response = JOptionPane.showConfirmDialog(
					this,
					"Are you sure to delete this record?",
					"Delete record",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE
				);
				
				switch(response) { //According to confirm dialog execute deletion
				case JOptionPane.YES_OPTION:
					Main.closeAnalyze(); //Maybe analyze screen would be opened so it should be closed
					deleteRecord(selected);
					tableModel.setRowCount(0); //To reset table
					DefaultComboBoxModel<String> boxModel = (DefaultComboBoxModel<String>) box.getModel();
					boxModel.removeAllElements(); //To reset drowdown menu
					for (String str : new File("data/oldRecords/").list()) boxModel.addElement(str);
					break;
				}
			}
		});

		//ComboBox
		String[] names = new File("data/oldRecords").list();
		if (names == null) names = new String[0];
		box = new JComboBox<>(names);
		box.setBounds((width-130)/2, 10, 130, 25);
		box.setFont(super.defaultPlainFont);
		box.addActionListener(e -> {
			selected = (String) box.getSelectedItem();
			if (selected != null) updateTable(tableModel, selected);
		});

		//ScrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(25, 50, 450, 370);

		//Composition part
		panel.add(box);
		panel.add(back);
		panel.add(analyze);
		panel.add(scrollPane);
		panel.add(delete);

                checkFileStructure(); //Creates required folders

		this.add(panel);
	}

	//Utility
	private void checkFileStructure() {
                File folder = new File("data/oldRecords");

                if (!folder.exists()) folder.mkdirs();
        }

        private void updateTable(DefaultTableModel model, String filename) {
		try {
			model.setRowCount(0);
			File file = new File("data/oldRecords/"+filename);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String row = reader.readLine();

			while (row != null) {
				String[] temp = row.split(",");
				model.addRow(new Object[] {temp[0], temp[1], convertTime(Integer.valueOf(temp[2])), temp[3]});
				row = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteRecord(String filename) {
                try {
		        new File("data/oldRecords/"+filename).delete();
                } catch (SecurityException e) {
                        e.printStackTrace();
                }
        }

	private String convertTime(long currentTime) {
		//Conversions
		long hour = currentTime / (60 * 60 * 1000); currentTime %= (60 * 60 * 1000);
		long minute = currentTime / (60 * 1000); currentTime %= (60 * 1000);
		long second = currentTime / 1000; currentTime %= 1000;
		long millisecond = currentTime;

		return String.format("%02d:%02d:%02d:%03d", hour, minute, second, millisecond);
	}
} 
