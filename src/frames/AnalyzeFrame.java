package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.util.HashMap;

public class AnalyzeFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 400, height = 400, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
	private JLabel timeLimit, hourLabel, minuteLabel;
	private JComboBox<Integer> hour, minute;
	private JButton calculate;
	private JTable table;
	private JScrollPane scrollPane;

	public AnalyzeFrame(String filename) {
		//Frame
		this.setTitle("Analyze");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				AnalyzeFrame.this.dispose();
			}
		});

		this.setSize(width, height);
                this.setLocation(x, y);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Label
		timeLimit = new JLabel("Time Limit:");
		timeLimit.setFont(new Font("Arial", Font.PLAIN, 16));
		timeLimit.setBounds(20, 10, 90, 25);

		hourLabel = new JLabel("h :");
		hourLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		hourLabel.setBounds(160, 10, 30, 25);

		minuteLabel = new JLabel("min");
		minuteLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		minuteLabel.setBounds(230, 10, 40, 25);

		//ComboBox
		hour = new JComboBox<>(range(0, 24));
		hour.setBounds(110, 10, 45, 25);
		hour.setFont(new Font("Arial", Font.PLAIN, 16));

		minute = new JComboBox<>(range(0, 60));
		minute.setBounds(180, 10, 45, 25);
		minute.setFont(new Font("Arial", Font.PLAIN, 16));

		//Table
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] {"ID", "Name", "Average", "Laps"});
		table = new JTable(model);
		table.setFillsViewportHeight(false);
		table.setEnabled(false);
		
		//Table sorting configurations
		table.setAutoCreateRowSorter(true);
		table.getRowSorter().toggleSortOrder(3); //There is double occurrance of toggleSortOrder(3) because
		table.getRowSorter().toggleSortOrder(3); //first one is toggles to ascending order but we need descending order. So, it is called twice

		//Column size fix
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(175);
		columnModel.getColumn(2).setPreferredWidth(100);
		columnModel.getColumn(3).setPreferredWidth(50);

		//Avoiding column drag action
		table.setTableHeader(new JTableHeader(columnModel) {
			@Override
			public void setDraggedColumn(TableColumn column) {}
		});

		//ScrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(15, 50, 375, 300);

		//Button
		calculate = new JButton("Calculate");
		calculate.setBounds(280, 10, 110, 25);
		calculate.setFont(new Font("Arial", Font.PLAIN, 16));
		calculate.addActionListener(e -> {
			model.setRowCount(0);
			long milliseconds = ((int) hour.getSelectedItem())*60*60*1000 + ((int) minute.getSelectedItem())*60*1000;
			Object[][] rows = matchNames(calculateAverage(getData(filename), milliseconds));

			for (int i = 0; i < rows.length; i++) {
				model.addRow(rows[i]);
			}
		});

		//Composition part
		panel.add(timeLimit);
		panel.add(hourLabel);
		panel.add(minuteLabel);
		panel.add(hour);
		panel.add(minute);
		panel.add(calculate);
		panel.add(scrollPane);

		this.add(panel);
	}

	//Utility
	private Integer[] range(int i, int j) {
		Integer[] arr = new Integer[j-i];
		for (int n = 0; n < j-i; n++) arr[n] = n+i;
		return arr;
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private Object[][] calculateAverage(String[][] data, long milliseconds) { //Calculation of lap counts and average lap times
		HashMap<Integer, Integer> laps = new HashMap<>(); //To hold lap data a HashMap created
		HashMap<Integer, Long> times = new HashMap<>(); //To hold time data a HashMap created

		//In this for loop data is read completely by updating lap and time values
		for (int i = 0; i < data.length; i++) {
			int id = Integer.valueOf(data[i][0]);
			if (laps.get(id) == null) laps.put(id, 0); //Initialize value
			if (times.get(id) == null) times.put(id, 0L); //Initialize value
			laps.put(id, laps.get(id)+1); //Each hit means one more lap
			long time = Integer.valueOf(data[i][2]);
			if (time > times.get(id)) times.put(id, time); //To get biggest number of time
		}
		
		int size = laps.size();
		Object[][] newData = new Object[size][4]; //This array is prepared to be added to table directly(Object array)

		int j = 0;
		for (int i : laps.keySet()) { //Values calculated to put into final average table
			long time = times.get(i);
			int lap = laps.get(i);
			String str = milliseconds != 0 && milliseconds < time ? "(DNF)" : "";
			newData[j++] = new Object[] {i, str, time/lap, lap};
		}

		return newData;
	}

	private Object[][] matchNames(Object[][] arr) { //This phase is matching the names of racers(This is a design preference to do this in calculateAverage() method)
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/racerinfo/racers.csv"));
			HashMap<Integer, String> map = new HashMap<>();
			String row = reader.readLine();

			while (row != null) {
				String[] temp = row.split(",");
				map.put(Integer.valueOf(temp[0]), temp[1]);
				row = reader.readLine();
			}

			//Matching part
			for (int i = 0; i < arr.length; i++) {
				arr[i][1] = map.get(arr[i][0]) + arr[i][1];
				arr[i][2] = convertTime((long) arr[i][2]); //Also converting milliseconds to readable format
			}
			reader.close();

			return arr;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
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
