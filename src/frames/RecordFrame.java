package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.nio.file.*;
import java.util.Calendar;
import java.util.HashMap;

public class RecordFrame extends TemplateFrame {
	private int width = 800, height = 600;
	private JPanel panel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField text1;
	private JButton add, start, pause, save, back;
	private JLabel chronoLabel;
	private Calendar calendar;
	private long startTime = -1, pausedTime, pausedTimeAmount = 0;
	private Thread chronoThread;
	private boolean isPaused = true;
	private HashMap<Integer, String> map;

	//Constructor
	public RecordFrame() {
		//Frame
		this.setTitle("Record");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		//Before on close action it saves records
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				File file = new File("data/records.csv"); //Obtain the file
				if (file.exists()) { //If it is exist then, define the actions
					int response = JOptionPane.showConfirmDialog(
						RecordFrame.this,
						"Do you want to save recorded data?",
						"Record Data",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE
					);
					switch(response) {
					case JOptionPane.YES_OPTION: //Save and close
						saveResults(0);
						System.exit(0);
						break;
					case JOptionPane.NO_OPTION: //Do not save and close
						file.delete();
						System.exit(0);
						break;
					}

					//If cancelled do nothing...

				} else { //If file is not exist close normally
					System.exit(0);
				}
			}
		});

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
		chronoLabel = new JLabel("00:00:00:000", SwingConstants.CENTER);
		chronoLabel.setBounds(30, 20, 100, 25);
		chronoLabel.setFont(new Font("Arial", Font.BOLD, 16));
		chronoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		//ScrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(425, 20, 350, 480);

		//TextField
		text1 = new JTextField(100);
		text1.setBounds(30, 70, 100, 25);
		text1.setFont(new Font("Arial", Font.PLAIN, 16));

		//
		add = new JButton("Add");
		add.setBounds(150, 70, 70, 25);
		add.setFont(new Font("Arial", Font.PLAIN, 16));
		add.addActionListener(e -> {
			//Necessary objects
			String text = text1.getText().trim();
		
			if (text.matches("\\d+") && !isPaused) { //If text is decimal and chronometer is running then,...
				//Getting info
				int id = Integer.valueOf(text);
				String name = identify(id),
				time = chronoLabel.getText();
	
				//Table update phase
				if (name != null) {
					//Inserting to table
					model.addRow(new Object[] {
						id,
						name,
						time
					});
	
					appendResult(id+","+name+","+convertToMillisecond(time)+"\n"); //Updating results.csv due to an unexpected crash
				}
			}

			//Making text field prepared for next submissions
			text1.setText("");
		});

		start = new JButton("Start");
		start.setBounds(150, 20, 75, 25);
		start.setFont(new Font("Arial", Font.PLAIN, 16));
		start.addActionListener(e -> {
			if (isPaused) {
				//Starting chronometer thread
				isPaused = !isPaused;
				startChronometer();
				
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

		pause = new JButton("Pause");
		pause.setBounds(240, 20, 80, 25);
		pause.setFont(new Font("Arial", Font.PLAIN, 16));
		pause.addActionListener(e -> {
			if (!isPaused) {
				//Switch pause state to stop recording
				isPaused = !isPaused;

				//Initialize pausedTime to calculate pausedTimeAmount
				calendar = Calendar.getInstance();
				pausedTime = calendar.getTimeInMillis();
			}
		});

		save = new JButton("Save&Exit");
		save.setBounds(650, 525, 120, 25);
		save.setFont(new Font("Arial", Font.PLAIN, 16));
		save.addActionListener(e -> {
			saveResults(0); //Save the results into /data/oldResults
			this.dispose();
			Main.showDash();
		});

		back = new JButton("Back");
		back.setBounds(30, 525, 80, 25);
		back.setFont(new Font("Arial", Font.PLAIN, 16));
		back.addActionListener(e -> {
			File file = new File("data/records.csv"); //Obtain the file
			if (file.exists()) { //If file is exist then, define the actions
				int response = JOptionPane.showConfirmDialog(
					this,
					"Do you want to save recorded data?",
					"Record Data",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE
				);
				switch(response) {
				case JOptionPane.YES_OPTION: //Save and close
					saveResults(0);
					this.dispose();
					Main.showDash();
					break;
				case JOptionPane.NO_OPTION: //Do not save and close
					file.delete();
					this.dispose();
					Main.showDash();
					break;
				}

				//If it is cancelled do nothing...

			} else { //If file is not exist go back
				this.dispose();
				Main.showDash();
			}
		});

		//Composition part
		panel.add(text1);
		panel.add(add);
		panel.add(start);
		panel.add(pause);
		panel.add(save);
		panel.add(back);
		panel.add(chronoLabel);
		panel.add(scrollPane);

		mapInit(); //Before recording initialize the map of racers
		
		this.add(panel);
		this.getRootPane().setDefaultButton(add);
	}
	
	//Utility
	private String identify(int id) {
		return (String) map.get(id); //It returns the information of a racer by his/her id
	}

	private void saveResults(int n) {
		try {
			if (!new File("data/records.csv").exists()) return; //If result.csv is doesn't exist then, do nothing
			//Getting required paths source and destination
			Path source = Paths.get("data/records.csv");

			String dir = "data/oldRecords/";
			if (!Files.isDirectory(Paths.get(dir))) new File(dir).mkdirs();
			Path destination = Paths.get(dir+"_".repeat(n)+"records.csv"); //Underscore will be repeated as many as n
			
			Files.move(source, destination);
		} catch(FileAlreadyExistsException e) {
			saveResults(n+1); //To handle name conflict automatically, increase n
		} catch(Exception e) {
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

	private long convertToMillisecond(String time) {
		//Conversions
		String[] temp = time.split(":");
		return (Integer.valueOf(temp[0])*60*60*1000)+(Integer.valueOf(temp[1])*60*1000)+(Integer.valueOf(temp[2])*1000)+Integer.valueOf(temp[3]);
	}

	//.csv Modifier
	private void appendResult(String str) {
		try {
			//Initializing BufferedWriter object in order to update informations in results.csv
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/records.csv", true));
			writer.write(str);
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void mapInit() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/racers.csv")); //Obtain the file to read
			map = new HashMap<>(); //Initialize the map object
			String row = reader.readLine(); //First line of file
		
			while (row != null) {
				String[] temp = row.split(","); //Simple split method to seperate ID and Name
				map.put(Integer.valueOf(temp[0]), temp[1]); //Mapping IDs and Names
				row = reader.readLine(); //Update row with next line
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Chronometer utility method to update its state
	private void chronoUpdate() {
		calendar = Calendar.getInstance(); //Each time we need to update calendar object because time is consistent
		long currentTime = calendar.getTimeInMillis();
		currentTime -= (startTime + pausedTimeAmount); //Paused time amount is should be also substracted from total time (totalTime = currentTime - startTime)
		
		//Updating the label
		chronoLabel.setText(convertTime(currentTime));
	}

	//Start method to start chronometer thread
	private void startChronometer() {
		//New Thread to keep track of time
		chronoThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					if (!isPaused) chronoUpdate(); //Chronometer label update function
//					Thread.sleep(50); //Wait 50 milliseconds to slow down cpu core
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});

		chronoThread.start();
	}
}
