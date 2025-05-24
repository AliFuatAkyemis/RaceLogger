package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.nio.file.*;
import java.util.Calendar;

public class RecordFrame extends JFrame {
	private int width = 800, height = 600;
	private JPanel panel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField text1;
	private JButton addButton, startButton, pauseButton, saveButton, backButton;
	private JLabel chrono;
	private Calendar calendar;
	private long startTime = -1, pausedTime, pausedTimeAmount = 0;
	private Thread chronoThread;
	private boolean isPaused = true;
	

	//Constructor
	public RecordFrame() {
		//Frame
		this.setTitle("Main");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		//Before on close action it saves records
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				File file = new File("data/results.csv"); //Obtain the file
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
		chrono = new JLabel("00:00:00:000", SwingConstants.CENTER);
		chrono.setBounds(30, 20, 100, 25);
		chrono.setFont(new Font("MV Boli", Font.PLAIN, 16));
		chrono.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		//ScrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(425, 20, 350, 500);

		//TextField
		text1 = new JTextField(100);
		text1.setBounds(30, 70, 100, 25);
		text1.setFont(new Font("MV Boli", Font.PLAIN, 16));

		//Button
		addButton = new JButton("Add");
		addButton.setBounds(150, 70, 70, 25);
		addButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		addButton.addActionListener(e -> {
			//Necessary objects
			String text = text1.getText().trim();
		
			if (text.matches("\\d+") && !isPaused) { //If text is decimal and chronometer is running then,...
				//Getting info
				int id = Integer.valueOf(text);
				String name = Main.identify(id),
				time = chrono.getText();
	
				//Table update phase
				if (name != null) {
					//Inserting to table
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

		startButton = new JButton("Start");
		startButton.setBounds(150, 20, 75, 25);
		startButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		startButton.addActionListener(e -> {
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

		pauseButton = new JButton("Pause");
		pauseButton.setBounds(240, 20, 80, 25);
		pauseButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		pauseButton.addActionListener(e -> {
			if (!isPaused) {
				//Switch pause state to stop recording
				isPaused = !isPaused;

				//Initialize pausedTime to calculate pausedTimeAmount
				calendar = Calendar.getInstance();
				pausedTime = calendar.getTimeInMillis();
			}
		});

		saveButton = new JButton("Save");
		saveButton.setBounds(690, 525, 80, 25);
		saveButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		saveButton.addActionListener(e -> {
			saveResults(0); //Save the results into /data/oldResults
		});

		backButton = new JButton("Back to Dashboard");
		backButton.setBounds(30, 525, 200, 25);
		backButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		backButton.addActionListener(e -> {
			File file = new File("data/results.csv"); //Obtain the file
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
		panel.add(addButton);
		panel.add(startButton);
		panel.add(pauseButton);
		panel.add(saveButton);
		panel.add(backButton);
		panel.add(chrono);
		panel.add(scrollPane);

		this.add(panel);
		this.getRootPane().setDefaultButton(addButton);
	}

	//.csv Modifier
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
			if (!new File("data/results.csv").exists()) return; //If result.csv is doesn't exist then, do nothing
			//Getting required paths source and destination
			Path source = Paths.get("data/results.csv");
			Path destination = Paths.get("data/oldResults/"+"_".repeat(n)+"results.csv"); //Underscore will be repeated as many as n
			
			Files.move(source, destination);
		} catch(FileAlreadyExistsException e) {
			saveResults(n+1); //To handle name conflict automatically, increase n
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Chronometer utility method to update its state
	public void chronoUpdate() {
		calendar = Calendar.getInstance(); //Each time we need to update calendar object because time is consistent
		long currentTime = calendar.getTimeInMillis();
		currentTime -= (startTime + pausedTimeAmount); //Paused time amount is should be also substracted from total time (totalTime = currentTime - startTime)
		
		//Time conversions
		long hour = currentTime / (60 * 60 * 1000); currentTime %= (60 * 60 * 1000);
		long minute = currentTime / (60 * 1000); currentTime %= (60 * 1000);
		long second = currentTime / 1000; currentTime &= 1000;
		long millisecond = currentTime;
		
		//Updating the label
		chrono.setText(hour+":"+minute+":"+second+":"+millisecond);
	}

	//Start method to start chronometer thread
	public void startChronometer() {
		//New Thread to keep track of time
		chronoThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					if (!isPaused) chronoUpdate(); //Chronometer label update function
					Thread.sleep(100); //Update wait time
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});

		chronoThread.start();
	}
}
