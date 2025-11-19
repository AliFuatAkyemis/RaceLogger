package frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class TeamAdjustFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 480, height = 450, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
        private JButton recover, save, retrieve;
        private JTable racerTable;
        private JScrollPane racerPane;

        public TeamAdjustFrame(String filename) {
		//Frame
		this.setTitle("Team Adjust");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				TeamAdjustFrame.this.dispose();
			}
		});

		this.setSize(width, height);
                this.setLocation(x, y);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

                //Table
                DefaultTableModel model = new DefaultTableModel() {
                        @Override
                        public Class<?> getColumnClass(int columnIndex) {
                                if (columnIndex == 0) {
                                        return Integer.class; // ID column is Integer
                                }
                                return String.class; // Other columns are String
                        }
                };

                model.setColumnIdentifiers(new String[] {"ID", "Name", "Gender", "Team"});
                
                racerTable = new JTable(model);
                racerTable.setFillsViewportHeight(false);
                racerTable.setEnabled(true);

                racerTable.setAutoCreateRowSorter(true);
                racerTable.getRowSorter().toggleSortOrder(0);
 
                TableColumnModel columnModel = racerTable.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(2).setPreferredWidth(100);
                columnModel.getColumn(3).setPreferredWidth(100);

                //ScrollPane
                racerPane = new JScrollPane(racerTable);
                racerPane.setBounds(15, 50, 450, 350);

                //Button
                recover = new JButton("Recover");
                recover.setFont(super.defaultPlainFont);
                recover.setBounds(10, 10, 100, 25);
                recover.addActionListener(e -> {
                        if (model.getRowCount() != 0) {
                                int response = JOptionPane.showConfirmDialog(
                                        this,
                                        "Table is not empty!\nAre you sure to overwrite existing table?",
                                        "Warning",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE
                                );

                                if (response == JOptionPane.YES_OPTION) {
                                        //Do nothing
                                } else {
                                        return;
                                }
                        }

                        model.setRowCount(0);
                        String[][] recoveredData = null;
                        if (new File("data/racerinfo/recovered/RacersOf_"+filename).exists()) recoveredData = loadFile("data/racerinfo/recovered/RacersOf_"+filename);
                        else recoveredData = collectRacerData(filename);

                        for (String[] arr : recoveredData) {
                                if (arr[0] == null) continue;
                                model.addRow(new Object[] {Integer.valueOf(arr[0]), arr[1], arr[2], arr[3]});
                        }
                });

                save = new JButton("Save");
                save.setFont(super.defaultPlainFont);
                save.setBounds(390, 10, 80, 25);
                save.addActionListener(e -> {
                        if (model.getRowCount() == 0) {
                                JOptionPane.showMessageDialog(
                                        this,
                                        "Table is empty!",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE
                                );

                                return;
                        }

                        if (new File("data/racerinfo/recovered/RacersOf_"+filename).exists()) {
                                int response = JOptionPane.showConfirmDialog(
                                        this,
                                        "There is a file with the same name!\nAre you sure to overwrite existing file?",
                                        "Warning",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE
                                );

                                if (response == JOptionPane.YES_OPTION) {
                                        //Do nothing
                                } else {
                                        return;
                                }
                        }

                        String[][] data = new String[model.getRowCount()][4];

                        for (int i = 0; i < data.length; i++) {
                                for (int j = 0; j < data[0].length; j++) {
                                        data[i][j] = String.valueOf(model.getValueAt(i, j));
                                }
                        }

                        saveRacerData(data, filename);

                        JOptionPane.showMessageDialog(
                                this,
                                "Racers are saved",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        this.dispose();
                });

                retrieve = new JButton("Retrieve");
                retrieve.setBounds(190, 10, 100, 25);
                retrieve.setFont(super.defaultPlainFont);
                retrieve.addActionListener(e -> {
                        if (model.getRowCount() != 0) {
                                int response = JOptionPane.showConfirmDialog(
                                        this,
                                        "Table is not empty!\nAre you sure overwriting to existing table?",
                                        "Warning",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE
                                ); 

                                if (response == JOptionPane.YES_OPTION) {
                                        //Do nothing
                                } else {
                                        return;
                                }
                        }

                        model.setRowCount(0);

                        String[][] racers = loadFile("data/racerinfo/racers.csv");

                        for (String[] row : racers) {
                                model.addRow(new Object[] {Integer.valueOf(row[0]), row[1], row[2], row[3]});
                        }

                        JOptionPane.showMessageDialog(
                                this,
                                "Racer data is retrieved",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                });

                //Composition Part
                panel.add(racerPane);
                panel.add(recover);
                panel.add(save);
                panel.add(retrieve);

                this.add(panel);
        }

        //Utility
        private String[][] loadFile(String filename) {
                try {
                        checkFileStructure();
                        BufferedReader reader = new BufferedReader(new FileReader(filename));
                        String[][] data = new String[lineCount(filename)][4];
                        String str = reader.readLine();

                        int i = 0;
                        while (str != null) {
                                String[] temp = str.split(",");
                                data[i++] = temp;
                                
                                str = reader.readLine();
                        }

                        reader.close();
                        return data;
                } catch (IOException e) {
                        e.printStackTrace();
                }

                return null;
        }

        private String[][] collectRacerData(String filename) {
                try {
                        BufferedReader reader = new BufferedReader(new FileReader("data/oldRecords/"+filename));
                        String[][] racerData = new String[lineCount("data/oldRecords/"+filename)][4];
                        String str = reader.readLine();

                        int i = 0;
                        while (str != null) {
                                String[] temp = str.split(","); 
                                if (!isContain(racerData, Integer.valueOf(temp[0]))) {
                                        racerData[i++] = new String[] {temp[0], temp[1], "?", "none"};
                                }

                                str = reader.readLine();
                        }

                        reader.close();
                        return racerData;
                } catch (IOException e) {
                        e.printStackTrace();
                }

                return null;
        }

        private void saveRacerData(String[][] racerData, String filename) {
                try {
                        checkFileStructure();
                        BufferedWriter writer = new BufferedWriter(new FileWriter("data/racerinfo/recovered/RacersOf_"+filename));

                        for (int i = 0; i < racerData.length; i++) {
                                String str = String.join(",", racerData[i]);
                                writer.write(str);
                                if (i < racerData.length-1) writer.newLine();
                        }

                        writer.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        private boolean isContain(String[][] matrix, int i) {
                for (String[] arr : matrix) {
                        if (arr[0] == null) continue;
                        if (Integer.valueOf(arr[0]) == i) return true;
                }

                return false;
        }
        
        private int lineCount(String filename) {
                try {
                        BufferedReader reader = new BufferedReader(new FileReader(filename));
                        String str = reader.readLine();

                        int i = 0;
                        while (str != null) {
                                i++;
                                str = reader.readLine();
                        }

                        reader.close();
                        return i;
                } catch (IOException e) {
                        e.printStackTrace();
                }

                return -1;
        }

        private void checkFileStructure() {
                File folder = new File("data/racerinfo/recovered/");

                if (!folder.exists()) folder.mkdirs();
        }
}
