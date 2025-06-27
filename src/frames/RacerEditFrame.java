package frames;

import controller.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class RacerEditFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 460, height = 400, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
	private JLabel idL, nameL;
	private JTextField id, name;
	private JButton add, remove, save, load, back, reset;
	private JTable table;
	private JScrollPane scrollPane;

	public RacerEditFrame() {
		//Frame
		this.setTitle("Racer Edit");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
                this.setLocation(x, y);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Table
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] {"ID", "Name"});
		
		table = new JTable(model);
		table.setFillsViewportHeight(false);
		table.setEnabled(false);

		//Column Model
		TableColumnModel columnModel = table.getColumnModel();

		//Resize columns
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(200);

		//Disabling dragging action of columns
		table.setTableHeader(new JTableHeader(columnModel) {
			@Override
			public void setDraggedColumn(TableColumn column) {}
		});

		//ScrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(40, 60, 250, 250);

		//Label
		idL = new JLabel("ID:");
		idL.setBounds(20, 10, 50, 25);
		idL.setFont(super.defaultPlainFont);

		nameL = new JLabel("Name:");
		nameL.setBounds(120, 10, 80, 25);
		nameL.setFont(super.defaultPlainFont);

		//TextField
		id = new JTextField();
		id.setBounds(50, 10, 60, 25);
		id.setFont(super.defaultPlainFont);

		name = new JTextField();
		name.setBounds(180, 10, 120, 25);
		name.setFont(super.defaultPlainFont);

		//Button
		back = new JButton("Back");
		back.setBounds(20, 325, 80, 25);
		back.setFont(super.defaultPlainFont);
		back.addActionListener(e -> {
			this.dispose();
			Main.showRecord();
		});

		add = new JButton("Add");
		add.setBounds(330, 50, 100, 25);
		add.setFont(super.defaultPlainFont);
		add.addActionListener(e -> {
			if (id.getText().trim().matches("\\d+") && !name.getText().trim().equals("")) { //ID must be digit so we first check it and name must be entered
				boolean found = false; //Duplicate control state
				for (int i = 0; i < model.getRowCount(); i++) if (!found) found = model.getValueAt(i, 0).equals(Integer.valueOf(id.getText().trim())) ? true : false; //Iteration of all entries is check
				if (!found) model.addRow(new Object[] {Integer.valueOf(id.getText().trim()), name.getText().trim()}); //If id is unique then, add new
			}

			//Reset textfields
			id.setText("");
			name.setText("");
			id.requestFocusInWindow();
		});

		remove = new JButton("Remove");
		remove.setBounds(330, 110, 100, 25);
		remove.setFont(super.defaultPlainFont);
		remove.addActionListener(e -> {
			if (id.getText().matches("\\d+")) { //Deletion is being done by id so we only check id
				for (int i = 0; i < model.getRowCount(); i++) { //Iteration of all entries
					if (model.getValueAt(i, 0).equals(Integer.valueOf(id.getText()))) { //Check for matches
						model.removeRow(i); //Then, remove
						break;
					}
				}
			}
			
			//Reset textfields
			id.setText("");
			name.setText("");
			id.requestFocusInWindow();
		});

		save = new JButton("Save");
		save.setBounds(330, 230, 100, 25);
		save.setFont(super.defaultPlainFont);
		save.addActionListener(e -> {
			if (model.getRowCount() != 0) saveTable(model);
		});

		load = new JButton("Load");
		load.setBounds(330, 290, 100, 25);
		load.setFont(super.defaultPlainFont);
		load.addActionListener(e -> {
			if (model.getRowCount() == 0) loadTable(model);
		});

		reset = new JButton("Reset");
		reset.setBounds(330, 170, 100, 25);
		reset.setFont(super.defaultPlainFont);
		reset.addActionListener(e -> {
			if (model.getRowCount() != 0) {
				int response = JOptionPane.showConfirmDialog(
					this,
					"Are you sure to reset racer info?",
					"Reset racer info",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE
				);
			
				if (response == JOptionPane.YES_OPTION) {
					deleteTable(model);
					model.setRowCount(0);
				}
			}
		});

		//Composition part
		panel.add(back);
		panel.add(idL);
		panel.add(nameL);
		panel.add(id);
		panel.add(name);
		panel.add(add);
		panel.add(remove);
		panel.add(save);
		panel.add(load);
		panel.add(reset);
		panel.add(scrollPane);
	
		this.add(panel);
		this.getRootPane().setDefaultButton(add);
	}

	//Utility
	private void loadTable(DefaultTableModel model) { //This method initializes the id-racer info to map and to the racers table at the same time
		try {
			if (!new File("data/racerinfo/racers.csv").exists()) return;
			BufferedReader reader = new BufferedReader(new FileReader("data/racerinfo/racers.csv")); //Obtain the file to read
			String row = reader.readLine(); //First line of file
		
			while (row != null) {
				String[] temp = row.split(","); //Simple split method to seperate ID and Name
				model.addRow(new Object[] {Integer.valueOf(temp[0]), temp[1]});
				row = reader.readLine(); //Update row with next line
			}

			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void saveTable(DefaultTableModel model) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/racerinfo/racers.csv"));
			String[] row = new String[model.getColumnCount()];

			for (int i = 0; i < model.getRowCount(); i++) {
				for (int j = 0; j < row.length; j++) {
					row[j] = String.valueOf(model.getValueAt(i, j));
				}
				String str = String.join(",", row);
				writer.write(str+"\n");
			}

			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteTable(DefaultTableModel model) {
		try {
			new File("data/racerinfo/racers.csv").delete();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
