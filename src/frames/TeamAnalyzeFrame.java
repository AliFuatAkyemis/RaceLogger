package frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class TeamAnalyzeFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 600, height = 480, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
        private JSeparator separator;
	private JLabel timeLimitMale, hourLabelMale, minuteLabelMale, lapLimitMale,
                        timeLimitFemale, hourLabelFemale, minuteLabelFemale, lapLimitFemale,
                        Male, Female;
	private JComboBox<Integer> hourMale, minuteMale, lapMale,
                                        hourFemale, minuteFemale, lapFemale;
        private JButton calcMale, calcFemale;
	private JTable tableMale, tableFemale, tableTemp;
	private JScrollPane scrollPaneMale, scrollPaneFemale;

        public TeamAnalyzeFrame(String filename) {
		//Frame
		this.setTitle("Analyze");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				TeamAnalyzeFrame.this.dispose();
			}
		});

		this.setSize(width, height);
                this.setLocation(x, y);

                //Panel
                panel = new JPanel();
                panel.setLayout(null);

                //Separator
                separator = new JSeparator(SwingConstants.VERTICAL);
                separator.setBounds(width/2, 0, 2, height);
                
		//Label
                Male = new JLabel("Male");
                Male.setFont(super.defaultBoldFont);
                Male.setBounds(130, 5, 70, 25);

                Female = new JLabel("Female");
                Female.setFont(super.defaultBoldFont);
                Female.setBounds(420, 5, 90, 25);

		timeLimitMale= new JLabel("Time Limit:");
		timeLimitMale.setFont(super.defaultPlainFont);
		timeLimitMale.setBounds(20, 40, 90, 25);

		hourLabelMale= new JLabel("h :");
		hourLabelMale.setFont(super.defaultPlainFont);
		hourLabelMale.setBounds(160, 40, 30, 25);

		minuteLabelMale= new JLabel("min");
		minuteLabelMale.setFont(super.defaultPlainFont);
		minuteLabelMale.setBounds(230, 40, 40, 25);

                lapLimitMale= new JLabel("Lap:");
                lapLimitMale.setFont(super.defaultPlainFont);
                lapLimitMale.setBounds(73, 75, 90, 25);

		timeLimitFemale= new JLabel("Time Limit:");
		timeLimitFemale.setFont(super.defaultPlainFont);
		timeLimitFemale.setBounds(320, 40, 90, 25);

		hourLabelFemale= new JLabel("h :");
		hourLabelFemale.setFont(super.defaultPlainFont);
		hourLabelFemale.setBounds(460, 40, 30, 25);

		minuteLabelFemale= new JLabel("min");
		minuteLabelFemale.setFont(super.defaultPlainFont);
		minuteLabelFemale.setBounds(530, 40, 40, 25);

                lapLimitFemale= new JLabel("Lap:");
                lapLimitFemale.setFont(super.defaultPlainFont);
                lapLimitFemale.setBounds(373, 75, 90, 25);

		//ComboBox
		hourMale= new JComboBox<>(range(0, 24));
		hourMale.setBounds(110, 40, 45, 25);
		hourMale.setFont(super.defaultPlainFont);

		minuteMale= new JComboBox<>(range(0, 60));
		minuteMale.setBounds(180, 40, 45, 25);
		minuteMale.setFont(super.defaultPlainFont);

                lapMale= new JComboBox<>(range(0, 10));
                lapMale.setBounds(110, 75, 45, 25);
                lapMale.setFont(super.defaultPlainFont);

		hourFemale= new JComboBox<>(range(0, 24));
		hourFemale.setBounds(410, 40, 45, 25);
		hourFemale.setFont(super.defaultPlainFont);

		minuteFemale= new JComboBox<>(range(0, 60));
		minuteFemale.setBounds(480, 40, 45, 25);
		minuteFemale.setFont(super.defaultPlainFont);

                lapFemale= new JComboBox<>(range(0, 10));
                lapFemale.setBounds(410, 75, 45, 25);
                lapFemale.setFont(super.defaultPlainFont);

                //Table
                DefaultTableModel modelMale = new DefaultTableModel();
                modelMale.setColumnIdentifiers(new String[] {"Name", "Average"});
                tableMale = new JTable(modelMale);
                tableMale.setFillsViewportHeight(false);
                tableMale.setEnabled(false);

                DefaultTableModel modelFemale = new DefaultTableModel();
                modelFemale.setColumnIdentifiers(new String[] {"Name", "Average"});
                tableFemale = new JTable(modelFemale);
                tableFemale.setFillsViewportHeight(false);
                tableFemale.setEnabled(false);

                //Table Sorting Configurations
                tableMale.setAutoCreateRowSorter(true);
                tableMale.getRowSorter().toggleSortOrder(1); //Sort by average in ascending order
                
                tableFemale.setAutoCreateRowSorter(true);
                tableFemale.getRowSorter().toggleSortOrder(1); //Sort by average in ascending order

                //Column model initialization
                TableColumnModel columnModelMale = tableMale.getColumnModel();

                TableColumnModel columnModelFemale = tableFemale.getColumnModel();
		
                //Avoiding column drag action
		tableMale.setTableHeader(new JTableHeader(columnModelMale) {
			@Override
			public void setDraggedColumn(TableColumn column) {}
		});

		tableFemale.setTableHeader(new JTableHeader(columnModelFemale) {
			@Override
			public void setDraggedColumn(TableColumn column) {}
		});

                //Temporary Table for Calculation
		DefaultTableModel modelTemp = new DefaultTableModel();
		modelTemp.setColumnIdentifiers(new String[] {"ID", "Name", "Average", "Laps"});
		tableTemp = new JTable(modelTemp);
		tableTemp.setFillsViewportHeight(false);
		tableTemp.setEnabled(false);
		
		//Table sorting configurations
		tableTemp.setAutoCreateRowSorter(true);

                List<RowSorter.SortKey> sortKeys = new ArrayList<>(); //A list for sort keys
                sortKeys.add(new RowSorter.SortKey(3, SortOrder.DESCENDING)); //Primary sort key
                sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING)); //Secondary sort key
                tableTemp.getRowSorter().setSortKeys(sortKeys); //Setting the sort keys

                //ScrollPane
                scrollPaneMale = new JScrollPane(tableMale);
                scrollPaneMale.setBounds(10, 110, 280, 330);

                scrollPaneFemale = new JScrollPane(tableFemale);
                scrollPaneFemale.setBounds(310, 110, 280, 330);

                //Button
                calcMale = new JButton("Calculate");
                calcMale.setBounds(75, 40, 150, 50);
                calcMale.setFont(super.defaultPlainFont);
                calcMale.addActionListener(e -> {
                        modelMale.setRowCount(0);
			modelTemp.setRowCount(0);
			long milliseconds = ((int) hourMale.getSelectedItem())*60*60*1000 + ((int) minuteMale.getSelectedItem())*60*1000;
                        int maxLap = ((int) lapMale.getSelectedItem());
			Object[][] rows = prepareToDisplay(calculateAverage(getData(filename), milliseconds, maxLap));

			for (int i = 0; i < rows.length; i++) {
				modelTemp.addRow(rows[i]);
			}

                        int a = modelTemp.getRowCount(), b = modelTemp.getColumnCount();

                        Object[][] temp = new Object[a][b];

                        for (int i = 0; i < a; i++) {
                                for (int j = 0; j < b; j++) {
                                        temp[i][j] = modelTemp.getValueAt(i, j);
                                }
                        }

                        HashMap<String, Long> map = new HashMap<>();
                        HashMap<String, Integer> map2 = new HashMap<>();

                        for (int i = 0; i < a; i++) {
                                String[] racerInfo = getTeamInfo((int) temp[i][0]);
                                if (racerInfo[3].equals("none")) continue;
                                map.put(racerInfo[3], 0L);
                                map2.put(racerInfo[3], 0);
                        }

                        for (int i = 0; i < a; i++) {
                                String[] racerInfo = getTeamInfo((int) temp[i][0]);
                                if (racerInfo[3].equals("none")) continue;
                                if (racerInfo[2].equals("E") && map2.get(racerInfo[3]) < 3) {
                                        map.put(racerInfo[3], map.get(racerInfo[3])+convertToMillisecond((String) temp[i][2]));
                                        map2.put(racerInfo[3], map2.get(racerInfo[3])+1);
                                }
                        }

                        for (String str : map.keySet()) {
                                if (map.get(str) == 0) continue;
                                modelMale.addRow(new Object[] {str, convertTime(map.get(str)/3)});
                        }
                });

                calcFemale = new JButton("Calculate");
                calcFemale.setBounds(375, 40, 150, 50);
                calcFemale.setFont(super.defaultPlainFont);
                calcFemale.addActionListener(e -> {
                        modelFemale.setRowCount(0);
			modelTemp.setRowCount(0);
			long milliseconds = ((int) hourFemale.getSelectedItem())*60*60*1000 + ((int) minuteFemale.getSelectedItem())*60*1000;
                        int maxLap = ((int) lapFemale.getSelectedItem());
			Object[][] rows = prepareToDisplay(calculateAverage(getData(filename), milliseconds, maxLap));

			for (int i = 0; i < rows.length; i++) {
				modelTemp.addRow(rows[i]);
			}

                        int a = modelTemp.getRowCount(), b = modelTemp.getColumnCount();

                        Object[][] temp = new Object[a][b];

                        for (int i = 0; i < a; i++) {
                                for (int j = 0; j < b; j++) {
                                        temp[i][j] = modelTemp.getValueAt(i, j);
                                }
                        }

                        HashMap<String, Long> map = new HashMap<>();
                        HashMap<String, Integer> map2 = new HashMap<>();

                        for (int i = 0; i < a; i++) {
                                String[] racerInfo = getTeamInfo((int) temp[i][0]);
                                if (racerInfo[3].equals("none")) continue;
                                map.put(racerInfo[3], 0L);
                                map2.put(racerInfo[3], 0);
                        }

                        for (int i = 0; i < a; i++) {
                                String[] racerInfo = getTeamInfo((int) temp[i][0]);
                                if (racerInfo[3].equals("none")) continue;
                                if (racerInfo[2].equals("K") && map2.get(racerInfo[3]) < 2) {
                                        map.put(racerInfo[3], map.get(racerInfo[3])+convertToMillisecond((String) temp[i][2]));
                                        map2.put(racerInfo[3], map2.get(racerInfo[3])+1);
                                }
                        }

                        for (String str : map.keySet()) {
                                if (map.get(str) == 0) continue;
                                modelFemale.addRow(new Object[] {str, convertTime(map.get(str)/3)});
                        }
                });

                //Composition Part
                panel.add(separator);
                panel.add(Male);
                panel.add(Female);
                /*
                panel.add(timeLimitMale);
                panel.add(hourLabelMale);
                panel.add(minuteLabelMale);
                panel.add(lapLimitMale);
                panel.add(timeLimitFemale);
                panel.add(hourLabelFemale);
                panel.add(minuteLabelFemale);
                panel.add(lapLimitFemale);
                panel.add(hourMale);
                panel.add(minuteMale);
                panel.add(lapMale);
                panel.add(hourFemale);
                panel.add(minuteFemale);
                panel.add(lapFemale);
                */
                panel.add(calcMale);
                panel.add(calcFemale);
                panel.add(scrollPaneMale);
                panel.add(scrollPaneFemale);

                this.add(panel);
        }

	//Utility
	private Integer[] range(int i, int j) {
		Integer[] arr = new Integer[j-i];
		for (int n = 0; n < j-i; n++) arr[n] = n+i;
		return arr;
	}

        private String[] getTeamInfo(int id) {
                String[][] racerData = getRacerInfo();

                for (int i = 0; i < racerData.length; i++) {
                        if (Integer.valueOf(racerData[i][0]) == id) return racerData[i];
                }

                return null;
        }

        private String[][] getRacerInfo() {
                try {
                        String filename = "data/racerinfo/racers.csv";
                        int lineCount = lineCount(filename);
                        String[][] data = new String[lineCount][4];
                        BufferedReader reader = new BufferedReader(new FileReader(filename));
                        String row = reader.readLine();

                        int i = 0;
                        while (row != null) {
                                data[i++] = row.split(","); 
                                row = reader.readLine();
                        }

                        reader.close();
                        return data;
                } catch (IOException e) {
                        e.printStackTrace();
                }
                
                return null;
        }

	private String[][] getData(String filename) {
		try { //This method reads the data from file
			BufferedReader reader = new BufferedReader(new FileReader("data/oldRecords/"+filename));
			String[][] data = new String[fileLength(filename)][4];
			String row = reader.readLine();

			int i = 0;
			while (row != null) {
				String[] temp = row.split(",");
				data[i++] = new String[] {temp[0], temp[1], temp[2], ""}; //4th column is added to place lap count later
				row = reader.readLine();
			}
			reader.close();

			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
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

	private int fileLength(String filename) {
		try { //Getting file length to init array length
			BufferedReader reader = new BufferedReader(new FileReader("data/oldRecords/"+filename));
			String row = reader.readLine();

			int i = 0;
			while (row != null) {
				i++;
				row = reader.readLine();
			}
			reader.close();

			return i;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private Object[][] calculateAverage(String[][] data, long milliseconds, int maxLap) { //Calculation of lap counts and average lap times
		HashMap<Integer, Integer> laps = new HashMap<>(); //To hold lap data a HashMap created
		HashMap<Integer, Long> times = new HashMap<>(); //To hold time data a HashMap created
                HashMap<Integer, String> names = new HashMap<>(); //To hold name data a HashMap created

		//In this for loop data is read completely by updating lap and time values
		for (int i = 0; i < data.length; i++) {
			int id = Integer.valueOf(data[i][0]);
			if (laps.get(id) == null) laps.put(id, 0); //Initialize value
			if (times.get(id) == null) times.put(id, 0L); //Initialize value
			laps.put(id, laps.get(id)+1); //Each hit means one more lap
			long time = Integer.valueOf(data[i][2]);
			if (time > times.get(id)) times.put(id, time); //To get biggest number of time
                        names.put(id, data[i][1]);
		}
		
		int size = laps.size();
		Object[][] newData = new Object[size][4]; //This array is prepared to be added to table directly(Object array)

		int j = 0;
		for (int i : laps.keySet()) { //Values calculated to put into final average table
			long time = times.get(i);
			int lap = laps.get(i);
                        String str = names.get(i);
			str += (milliseconds != 0 && milliseconds < time) || (maxLap != 0 && lap < maxLap) ? "(DNF)" : "";
			newData[j++] = new Object[] {i, str, time/lap, lap};
		}

		return newData;
	}

	private Object[][] prepareToDisplay(Object[][] arr) { //This phase makes output more readable
		for (int i = 0; i < arr.length; i++) {
			arr[i][2] = convertTime((long) arr[i][2]); //Also converting milliseconds to readable format
		}

		return arr;
	}

	private String convertTime(long currentTime) {
		//Conversions
		long hour = currentTime / (60 * 60 * 1000); currentTime %= (60 * 60 * 1000);
		long minute = currentTime / (60 * 1000); currentTime %= (60 * 1000);
		long second = currentTime / 1000; currentTime %= 1000;
		long millisecond = currentTime;

		return String.format("%02d:%02d:%02d:%03d", hour, minute, second, millisecond);
	}

	private long convertToMillisecond(String time) {
		//Conversions
		String[] temp = time.split(":");
		return (Integer.valueOf(temp[0])*60*60*1000)+(Integer.valueOf(temp[1])*60*1000)+(Integer.valueOf(temp[2])*1000)+Integer.valueOf(temp[3]);
	}
}
