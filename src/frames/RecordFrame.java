package frames;

import controller.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.FileAlreadyExistsException;
import java.util.Calendar;
import java.util.HashMap;

public class RecordFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        private int width = 800, height = 600, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
	private JTable records, racers;
	private JScrollPane recordPane, racerPane;
	private JTextField text;
	private JButton add, start, pause, save, back, edit;
	private JLabel chronoLabel, editLabel;
	private Calendar calendar;
	private long startTime = -1, pausedTime, pausedTimeAmount = 0, recoveredTime = 0;
	private Thread chronoThread;
	private boolean isPaused = true;
	private HashMap<Integer, String> map;
	private HashMap<Integer, Integer> laps;

	//Constructor
	public RecordFrame() {
		//Frame
		this.setTitle("Record");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                
		//Before on close action it saves records
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				File file = new File("data/record.csv"); //Obtain the file
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
						String str = JOptionPane.showInputDialog(
							RecordFrame.this,
							"Record name?",
							"Save record",
							JOptionPane.PLAIN_MESSAGE
						);
						if (str != null) {
							saveResults(0, str);
							System.exit(0);
						}
						break;
					case JOptionPane.NO_OPTION: //Do not save and close
                                                saveResults(0, "Deleted-"+System.currentTimeMillis());
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
		this.setLocation(x, y);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Table
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] {"ID", "Name", "Time", "Laps"});
		
		records = new JTable(model);
		records.setFillsViewportHeight(false);
		records.setEnabled(false);
		
		//TableColumnModel
		TableColumnModel columnModel = records.getColumnModel(); //Getting the instance of column model
		
		//Then editing the size seperately
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(2).setPreferredWidth(100);
		columnModel.getColumn(3).setPreferredWidth(50);
	
		//To disable dragging action from table an override is needed here
		records.setTableHeader(new JTableHeader(columnModel) {
		    @Override
		    public void setDraggedColumn(TableColumn column) {}
		});

		DefaultTableModel model2 = new DefaultTableModel();
		model2.setColumnIdentifiers(new String[] {"ID", "Name"});
		
		racers = new JTable(model2);
		racers.setFillsViewportHeight(false);
		racers.setEnabled(false);
		racers.setAutoCreateRowSorter(true);
		racers.getRowSorter().toggleSortOrder(0); //To sort according to IDs (ascending order)

		//TableColumnModel
		TableColumnModel columnModel2 = racers.getColumnModel();

		//Column size
		columnModel2.getColumn(0).setPreferredWidth(50);
		columnModel2.getColumn(1).setPreferredWidth(200);
	
		//To disable column dragging action we need to override drag function
		racers.setTableHeader(new JTableHeader(columnModel2) {
			@Override
			public void setDraggedColumn(TableColumn column) {}
		});

		//ScrollPane
		recordPane = new JScrollPane(records);
		recordPane.setBounds(375, 20, 400, 480);

		racerPane = new JScrollPane(racers);
		racerPane.setBounds(30, 120, 250, 300);

		//Label
		chronoLabel = new JLabel("00:00:00:000", SwingConstants.CENTER);
		chronoLabel.setBounds(30, 20, 120, 25);
		chronoLabel.setFont(super.defaultBoldFont);
		chronoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                editLabel = new JLabel("Edit", SwingConstants.CENTER);
		editLabel.setBounds(30, 430, 80, 25);
		editLabel.setFont(super.defaultPlainFont);
		editLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                editLabel.setVisible(false);

		//TextField
		text = new JTextField(100);
		text.setBounds(30, 70, 120, 25);
		text.setFont(super.defaultPlainFont);

		//Button
		add = new JButton("Add");
		add.setBounds(170, 70, 70, 25);
		add.setFont(super.defaultPlainFont);
		add.addActionListener(e -> {
			//Necessary objects
			String input = text.getText().trim();
		
			if (input.matches("\\d+") && !isPaused) { //If text is decimal and chronometer is running then,...
                                //Additional check for racerinfo.csv
                                if (new File("data/racerinfo/racers.csv").exists()) {
        				//Getting info
        				int id = Integer.valueOf(input),
        				lap = lapUpdate(id);
        				String name = identify(id),
        				time = chronoLabel.getText();
        	
        				//Table update phase
        				if (name != null) {
        					//Inserting to table
        					model.addRow(new Object[] {
        						id,
        						name,
        						time,
        						lap
        					});
        	
        					appendResult(id+","+name+","+convertToMillisecond(time)+","+lap+"\n"); //Updating records.csv due to an unexpected crash
        				}
                                }
			}

			//Making text field prepared for next submissions
			text.setText("");
		});

		edit = new JButton("Edit");
		edit.setBounds(30, 430, 80, 25);
		edit.setFont(super.defaultPlainFont);
		edit.addActionListener(e -> {
			if (startTime == -1) {
				this.dispose();
				Main.showRacerEdit();
			}
		});

		start = new JButton("Start");
		start.setBounds(170, 20, 85, 25);
		start.setFont(super.defaultPlainFont);
		start.addActionListener(e -> {
			if (isPaused) {
				//Starting chronometer thread
				isPaused = !isPaused;
				startChronometer(recoveredTime);
				
				//Initialization of startTime
				if (startTime == -1) {
					calendar = Calendar.getInstance();
					startTime = calendar.getTimeInMillis();
					edit.setVisible(false);
                                        editLabel.setVisible(true);
				} else {
					//If recording started before calculate pausedTimeAmount
					calendar = Calendar.getInstance();
					pausedTimeAmount += calendar.getTimeInMillis() - pausedTime;
				}

			}
		});

		pause = new JButton("Pause");
		pause.setBounds(270, 20, 90, 25);
		pause.setFont(super.defaultPlainFont);
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
		save.setFont(super.defaultPlainFont);
		save.addActionListener(e -> {
			if (new File("data/record.csv").exists()) { //If there a record exists an optionpane pops up.
				String str = JOptionPane.showInputDialog(
					this,
					"Record name?",
					"Save record",
					JOptionPane.PLAIN_MESSAGE
				);
				if (str != null) { //According to input results are saved
					saveResults(0, str); //Save the results into /data/oldResults
					this.dispose();
					Main.showDash();
				}
			}
		});

		back = new JButton("Back");
		back.setBounds(30, 525, 80, 25);
		back.setFont(super.defaultPlainFont);
		back.addActionListener(e -> {
			File file = new File("data/record.csv"); //Obtain the file
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
					String str = JOptionPane.showInputDialog(
						this,
						"Record name?",
						"Save record",
						JOptionPane.PLAIN_MESSAGE
					);
					if (str != null) {
						saveResults(0, str);
						this.dispose();
						Main.showDash();
					}
					break;
				case JOptionPane.NO_OPTION: //Do not save and close
                                        saveResults(0, "Deleted-"+System.currentTimeMillis());
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
		panel.add(text);
		panel.add(add);
		panel.add(start);
		panel.add(pause);
		panel.add(save);
		panel.add(back);
		panel.add(edit);
		panel.add(chronoLabel);
                panel.add(editLabel);
		panel.add(recordPane);
		panel.add(racerPane);

                recoverRecords(model);
		mapInit(model2); //Before recording initialize the map of racers
                checkFileStructure(); //Creates required folders

		this.add(panel);
		this.getRootPane().setDefaultButton(add);
	}
	
	//Utility
        private void checkFileStructure() {
                File folder = new File("data/racerinfo"), folder2 = new File("data/oldRecords");

                if (!folder.exists()) folder.mkdirs();
                if (!folder2.exists()) folder2.mkdirs();
        }
        
	private String identify(int id) {
		return (String) map.get(id); //It returns the information of a racer by his/her id
	}

	private int lapUpdate(int id) { //Updates the lap counter
		if (laps == null) laps = new HashMap<>(); //Initialize if object is null
		if (laps.get(id) == null) laps.put(id, 0); //Initial value is null for Integer class so check is needed

		//Update and return
		int i = laps.get(id);
		laps.put(id, i+1);
		return i+1;
	}

	private void saveResults(int n, String str) {
		try {
			if (str == null || str.equals("")) str = "record";
			if (!new File("data/record.csv").exists()) return; //If record.csv is doesn't exist then, do nothing
			//Getting required paths source and destination
			Path source = Paths.get("data/record.csv");

			String dir = "data/oldRecords/";
			if (!Files.isDirectory(Paths.get(dir))) new File(dir).mkdirs();
			Path destination = Paths.get(dir+"_".repeat(n)+str+".csv"); //Underscore will be repeated as many as n
			
			Files.move(source, destination);
		} catch (FileAlreadyExistsException e) {
			saveResults(n+1, str); //To handle name conflict automatically, increase n
		} catch (IOException e) {
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
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/record.csv", true));
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void mapInit(DefaultTableModel model) { //This method initializes the id-racer info to map and to the racers table at the same time
		try {
			if (!new File("data/racerinfo/racers.csv").exists()) return;
			BufferedReader reader = new BufferedReader(new FileReader("data/racerinfo/racers.csv")); //Obtain the file to read
			map = new HashMap<>(); //Initialize the map object
			String row = reader.readLine(); //First line of file
		
			while (row != null) {
				String[] temp = row.split(","); //Simple split method to seperate ID and Name
				map.put(Integer.valueOf(temp[0]), temp[1]); //Mapping IDs and Names
				model.addRow(new Object[] {temp[0], temp[1]});
				row = reader.readLine(); //Update row with next line
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

        private void recoverRecords(DefaultTableModel model) { //This method recovers the previous record if it is still unsaved due to a immeidate shutdown.
                try {
                        File file = new File("data/record.csv"); //Create a File object that points to the record.csv
                        if (!file.exists()) return; //If it is not located then, terminate the function.

                        BufferedReader reader = new BufferedReader(new FileReader(file)); //Create a reader object to transfer records.csv content to record screen table.
                        String lastLine = "", str = reader.readLine(); //Initialization

                        //Iteration part
                        while (str != null) { //If the content is finished then, reader returns null(stop condition)
                                String[] row = str.split(","); //Turn str content into String[] to reach each element by its index

                                //Append to the table
                                model.addRow(new Object[] {
                                        row[0],
                                        row[1],
                                        convertTime(Integer.valueOf(row[2])),
                                        row[3]
                                });

                                lastLine = str;

                                str = reader.readLine(); //Took the next row
                        }

                        String[] row = lastLine.split(",");
                        recoveredTime = Integer.valueOf(row[2]);

                        reader.close(); //Close the reader object to free memory
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

	//Chronometer utility method to update its state
	private void chronoUpdate(long offTime) {
		calendar = Calendar.getInstance(); //Each time we need to update calendar object because time is consistent
		long currentTime = calendar.getTimeInMillis();
		currentTime -= (startTime + pausedTimeAmount); //Paused time amount is should be also substracted from total time (totalTime = currentTime - startTime)
		
		//Updating the label
		chronoLabel.setText(convertTime(offTime+currentTime));
	}

	//Start method to start chronometer thread
	private void startChronometer(long offTime) {
		//New Thread to keep track of time
		chronoThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					if (!isPaused) chronoUpdate(offTime); //Chronometer label update function
//					Thread.sleep(50); //Wait 50 milliseconds to slow down cpu core
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		chronoThread.start();
	}
}
