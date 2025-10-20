package frames;

import controller.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
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
	private int width = 580, height = 400, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
	private JLabel idL, nameL, sexL, teamL;
	private JTextField id, name, team;
        private JComboBox<String> sex;
	private JButton add, remove, save, load, back, reset, choose;
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
		model.setColumnIdentifiers(new String[] {"ID", "Name", "Gender", "Team"});
		
		table = new JTable(model);
		table.setFillsViewportHeight(false);
		table.setEnabled(false);

		//Column Model
		TableColumnModel columnModel = table.getColumnModel();

		//Resize columns
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(2).setPreferredWidth(100);
                columnModel.getColumn(3).setPreferredWidth(100);

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

                sexL = new JLabel("Gender:");
                sexL.setBounds(310, 10, 80, 25);
		sexL.setFont(super.defaultPlainFont);

                teamL = new JLabel("Team:");
                teamL.setBounds(440, 10, 80, 25);
		teamL.setFont(super.defaultPlainFont);

		//TextField
		id = new JTextField();
		id.setBounds(45, 10, 60, 25);
		id.setFont(super.defaultPlainFont);

		name = new JTextField();
		name.setBounds(175, 10, 120, 25);
		name.setFont(super.defaultPlainFont);

                team = new JTextField();
                team.setBounds(490, 10, 60, 25);
                team.setFont(super.defaultPlainFont);
		
                //ComboBox
		sex = new JComboBox<>(new String[] {"E", "K"});
		sex.setBounds(375, 10, 50, 25);
		sex.setFont(super.defaultPlainFont);

		//Button
		back = new JButton("Back");
		back.setBounds(20, 325, 80, 25);
		back.setFont(super.defaultPlainFont);
		back.addActionListener(e -> {
			this.dispose();
			Main.showRecord();
		});

		add = new JButton("Add");
		add.setBounds(330, 80, 100, 50);
		add.setFont(super.defaultPlainFont);
		add.addActionListener(e -> {
			if (id.getText().trim().matches("\\d+") && !name.getText().trim().equals("")) { //ID must be digit so we first check it and name must be entered
				boolean found = false; //Duplicate control state
				for (int i = 0; i < model.getRowCount(); i++) if (!found) found = model.getValueAt(i, 0).equals(Integer.valueOf(id.getText().trim())) ? true : false; //Iteration of all entries is check
				if (!found) {
                                        model.addRow(new Object[] {
                                                Integer.valueOf(id.getText().trim()),
                                                name.getText().trim(),
                                                ((String) sex.getSelectedItem()).trim()
                                        }); //If id is unique then, add new
                                }
                        }

			//Reset textfields
			id.setText("");
			name.setText("");
			id.requestFocusInWindow();
		});

		remove = new JButton("Remove");
		remove.setBounds(450, 80, 100, 50);
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
		save.setBounds(330, 160, 100, 50);
		save.setFont(super.defaultPlainFont);
		save.addActionListener(e -> {
			if (model.getRowCount() != 0) {
                                saveTable(model);
                                JOptionPane.showMessageDialog(
                                        this,
                                        "Racers are saved",
                                        "Information",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                        }
		});

		load = new JButton("Load");
		load.setBounds(450, 160, 100, 50);
		load.setFont(super.defaultPlainFont);
		load.addActionListener(e -> {
			if (model.getRowCount() == 0) loadTable(model);
                        else {
				int response = JOptionPane.showConfirmDialog(
					this,
					"Table is not empty, are you sure to load table?",
					"Confirmation",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE
				);
			
				if (response == JOptionPane.YES_OPTION) {
					model.setRowCount(0);
                                        loadTable(model);
				}
                        }
		});

		reset = new JButton("Delete");
		reset.setBounds(330, 240, 100, 50);
		reset.setFont(super.defaultPlainFont);
		reset.addActionListener(e -> {
			if (model.getRowCount() != 0) {
				int response = JOptionPane.showConfirmDialog(
					this,
					"Are you sure to delete racer info?",
					"Confirmation",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE
				);
			
				if (response == JOptionPane.YES_OPTION) {
					deleteTable(model);
					model.setRowCount(0);
                                        JOptionPane.showMessageDialog(
                                                this,
                                                "Racer information is deleted",
                                                "Information",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
				}
			}
		});

                choose = new JButton("Import");
                choose.setBounds(450, 240, 100, 50);
                choose.setFont(super.defaultPlainFont);
                choose.addActionListener(e -> {
                        File file = null;
                        JFileChooser fileChooser = new JFileChooser();
                        int result = fileChooser.showOpenDialog(this);

                        if (result == fileChooser.APPROVE_OPTION) {
                                file = fileChooser.getSelectedFile();
                        }

                        if (file == null) { //If file is null then, warn the user
                                JOptionPane.showMessageDialog(
                                        this,
                                        "Invalid File Type!",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE
                                );

                                return;
                        }

                        saveRacerInfo(file); //Copy the content of imported file to data/racerinfo/ location
                        model.setRowCount(0); //Clear the table content
                        loadTable(model); //Load with new informations
                        JOptionPane.showMessageDialog(
                                this,
                                "File content is imported",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                });

		//Composition part
		panel.add(back);
		panel.add(idL);
		panel.add(nameL);
                panel.add(sexL);
                panel.add(teamL);
		panel.add(id);
		panel.add(name);
                panel.add(sex);
                panel.add(team);
		panel.add(add);
		panel.add(remove);
		panel.add(save);
		panel.add(load);
		panel.add(reset);
                panel.add(choose);
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
				model.addRow(new Object[] {Integer.valueOf(temp[0]), temp[1], temp[2]});
				row = reader.readLine(); //Update row with next line
			}

			reader.close();
		} catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                } catch (IOException e2) {
			e2.printStackTrace();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteTable(DefaultTableModel model) {
                try {
		        new File("data/racerinfo/racers.csv").delete();
                } catch (SecurityException e) {
                        e.printStackTrace();
                }
	}

        private void saveRacerInfo(File file) {
                try {
                        //Create require tools to read-write files
                        File saved = new File("data/racerinfo/racers.csv");
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(saved));
                        String str = reader.readLine(); //Initialization

                        while (str != null) {
                                //Writing part
                                writer.write(str);
                                writer.newLine(); //CR-LF characters

                                str = reader.readLine(); //Read next line
                        }

                        //Freeing up the objects from memory
                        reader.close();
                        writer.close(); //This is essential to apply changes on file
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
