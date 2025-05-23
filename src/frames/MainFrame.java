package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.nio.file.*;
import java.util.Calendar;

public class MainFrame extends JFrame {
	int width = 800, height = 600;
	JPanel panel, raceMap;
	JTable table;
	JScrollPane scrollPane;
	JTextField text1;
	JButton addButton, chronoStartButton, chronoPauseButton, saveButton;
	JLabel chrono;
	ImageIcon map;
	Image scaled;
	Calendar calendar;
	long startTime = -1, pausedTime, pausedTimeAmount = 0;

	//Constructor
	public MainFrame() {
		//Frame
		this.setTitle("Main");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation((1920-width)/2, (1080-height)/2);
		this.setResizable(false);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

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

		//Label
		chrono = new JLabel("00:00:00:000", SwingConstants.CENTER);
		chrono.setBounds(30, 250, 100, 25);
		chrono.setFont(new Font("MV Boli", Font.PLAIN, 16));
		chrono.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		//ScrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(425, 20, 350, 500);

		//TextField
		text1 = new JTextField(100);
		text1.setBounds(30, 300, 100, 25);
		text1.setFont(new Font("MV Boli", Font.PLAIN, 16));

		//Button
		addButton = new JButton("Add");
		addButton.setBounds(150, 300, 70, 25);
		addButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		addButton.addActionListener(e -> {
			//Necessary objects
			String text = text1.getText().trim();
		
			if (text.matches("\\d+") && !Main.getIsPaused()) {
				int id = Integer.valueOf(text);
				String name = Main.identify(id),
				time = chrono.getText();
	
				//Table update phase
				if (name != null) {
					model.addRow(new Object[] {
						id,
						name,
						time
					});
	
					appendResult(id+","+name+","+time+"\n"); //Updating results.csv due to an unexpected crash
				}
			}

			//Making text field prepared for next submissions
			text1.setText("");
		});

		chronoStartButton = new JButton("Start");
		chronoStartButton.setBounds(150, 250, 75, 25);
		chronoStartButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		chronoStartButton.addActionListener(e -> {
			if (Main.getIsPaused()) {
				//Starting chronometer thread
				Main.switchPauseState();
				Main.startChronometer();
				
				//Initialization of startTime
				if (startTime == -1) {
					calendar = Calendar.getInstance();
					startTime = calendar.getTimeInMillis();
				} else {
					//If recording started before calculate pausedTimeAmount
					calendar = Calendar.getInstance();
					pausedTimeAmount += calendar.getTimeInMillis() - pausedTime;
				}

			}
		});

		chronoPauseButton = new JButton("Pause");
		chronoPauseButton.setBounds(240, 250, 80, 25);
		chronoPauseButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		chronoPauseButton.addActionListener(e -> {
			if (!Main.getIsPaused()) {
				//Switch pause state to stop recording
				Main.switchPauseState();

				//Initialize pausedTime to calculate pausedTimeAmount
				calendar = Calendar.getInstance();
				pausedTime = calendar.getTimeInMillis();
			}
		});

		saveButton = new JButton("Save");
		saveButton.setBounds(690, 525, 80, 25);
		saveButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		saveButton.addActionListener(e -> {
			saveResults(0);
		});

		//RaceMapPanel
		raceMap = new JPanel();
		raceMap.setBounds(30, 20, 350, 200);
		raceMap.setLayout(new BorderLayout());
		raceMap.setBackground(new Color(0xaaaaaa));

		//Image
		ImageIcon map = new ImageIcon("images/route.png");
		Image scaled = map.getImage().getScaledInstance(350, 200, Image.SCALE_SMOOTH);
		
		//Composition part
		raceMap.add(new JLabel(new ImageIcon(scaled)), BorderLayout.CENTER);

		panel.add(raceMap);
		panel.add(text1);
		panel.add(addButton);
		panel.add(chronoStartButton);
		panel.add(chronoPauseButton);
		panel.add(saveButton);
		panel.add(chrono);
		panel.add(scrollPane);

		this.add(panel);
		this.getRootPane().setDefaultButton(addButton);
	}

	private void appendResult(String str) {
		try {
			//Initializing BufferedWriter object in order to update informations in results.csv
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/results.csv", true));
			writer.write(str);
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void saveResults(int n) {
		try {
			Path source = Paths.get("data/results.csv");
			Path target = Paths.get("data/oldResults/"+"_".repeat(n)+"results.csv");
			
			Files.move(source, target);
		} catch(FileAlreadyExistsException e) {
			saveResults(n+1);
		} catch(Exception e) {
			
			e.printStackTrace();
		}
	}

	public void chronoUpdate() {
		calendar = Calendar.getInstance();
		long currentTime = calendar.getTimeInMillis();
		currentTime -= (startTime + pausedTimeAmount);
		long hour = currentTime / (60 * 60 * 1000); currentTime %= (60 * 60 * 1000);
		long minute = currentTime / (60 * 1000); currentTime %= (60 * 1000);
		long second = currentTime / 1000; currentTime &= 1000;
		long millisecond = currentTime;
		chrono.setText(hour+":"+minute+":"+second+":"+millisecond);
	}
}
