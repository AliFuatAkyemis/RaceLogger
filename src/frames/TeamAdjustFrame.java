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
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class TeamAdjustFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 600, height = 420, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
        private JButton recover, save;
        private JTable racerTable;
        private JScrollPane racerPane;

        public TeamAdjustFrame(String filename) {
		//Frame
		this.setTitle("Analyze");
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

                //ScrollPane
                racerPane = new JScrollPane(racerTable);

                //Button
                recover = new JButton("Recover");
                recover.setFont(super.defaultPlainFont);
                recover.setBounds(0, 0, 80, 25);
                recover.addActionListener(e -> {
                        String[][] recoveredData = collectRacerData(filename);

                });

                save = new JButton("Save");
                save.setFont(super.defaultPlainFont);
                save.setBounds(0, 0, 80, 25);
                save.addActionListener(e -> {
                        //Save Function
                });

                this.add(panel);
        }

        //Utility
        private String[][] collectRacerData(String filename) {
                try {
                        checkFileStructure();
                        BufferedReader reader = new BufferedReader(new FileReader(filename));
                        String[][] racerData = new String[lineCount(filename)][4];
                        String str = reader.readLine();

                        int i = 0;
                        while (str != null) {
                                String[] temp = str.split(","); 
                                if (!isContain(racerData, Integer.valueOf(temp[0]))) {
                                        racerData[i++] = new String[] {temp[0], temp[1], "empty", "empty"};
                                }
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
                        String[] temp = filename.split("/");
                        BufferedWriter writer = new BufferedWriter(new FileWriter("data/racerinfo/recovered/"+temp[temp.length-1]));

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
                File folder = new File("data/racerinfo/recovered");

                if (!folder.exists()) folder.mkdirs();
        }
}
