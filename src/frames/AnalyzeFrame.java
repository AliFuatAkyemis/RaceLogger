package frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class AnalyzeFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 400, height = 440, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
	private JLabel timeLimit, hourLabel, minuteLabel, lapLimit;
	private JComboBox<Integer> hour, minute, lap;
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
		timeLimit.setFont(super.defaultPlainFont);
		timeLimit.setBounds(20, 10, 90, 25);

		hourLabel = new JLabel("h :");
		hourLabel.setFont(super.defaultPlainFont);
		hourLabel.setBounds(160, 10, 30, 25);

		minuteLabel = new JLabel("min");
		minuteLabel.setFont(super.defaultPlainFont);
		minuteLabel.setBounds(230, 10, 40, 25);

                lapLimit = new JLabel("Lap:");
                lapLimit.setFont(super.defaultPlainFont);
                lapLimit.setBounds(73, 45, 90, 25);

		//ComboBox
		hour = new JComboBox<>(range(0, 24));
		hour.setBounds(110, 10, 45, 25);
		hour.setFont(super.defaultPlainFont);

		minute = new JComboBox<>(range(0, 60));
		minute.setBounds(180, 10, 45, 25);
		minute.setFont(super.defaultPlainFont);

                lap = new JComboBox<>(range(0, 10));
                lap.setBounds(110, 45, 45, 25);
                lap.setFont(super.defaultPlainFont);

		//Table
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] {"ID", "Name", "Average", "Laps"});
		table = new JTable(model);
		table.setFillsViewportHeight(false);
		table.setEnabled(false);
		
		//Table sorting configurations
		table.setAutoCreateRowSorter(true);

                List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                sortKeys.add(new RowSorter.SortKey(3, SortOrder.DESCENDING));
                sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
                table.getRowSorter().setSortKeys(sortKeys);

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
		scrollPane.setBounds(15, 90, 375, 300);

		//Button
		calculate = new JButton("Calculate");
		calculate.setBounds(280, 10, 110, 25);
		calculate.setFont(super.defaultPlainFont);
		calculate.addActionListener(e -> {
			model.setRowCount(0);
			long milliseconds = ((int) hour.getSelectedItem())*60*60*1000 + ((int) minute.getSelectedItem())*60*1000;
                        int maxLap = ((int) lap.getSelectedItem());
			Object[][] rows = prepareToDisplay(calculateAverage(getData(filename), milliseconds, maxLap));

			for (int i = 0; i < rows.length; i++) {
				model.addRow(rows[i]);
			}
		});

		//Composition part
		panel.add(timeLimit);
		panel.add(hourLabel);
		panel.add(minuteLabel);
                panel.add(lapLimit);
		panel.add(hour);
		panel.add(minute);
                panel.add(lap);
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
		} catch (IOException e) {
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
}
