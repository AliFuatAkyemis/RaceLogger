package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.util.Calendar;

public class MainFrame extends JFrame implements ActionListener {
	int width = 800, height = 600;
	JPanel panel, raceMap;
	JTable table;
	JScrollPane scrollPane;
	JTextField text1;
	JButton addButton;
	ImageIcon map;
	Image scaled;

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

		//ScrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(425, 20, 350, 500);

		//TextField
		text1 = new JTextField(100);
		text1.setBounds(30, 250, 100, 25);
		text1.setFont(new Font("MV Boli", Font.PLAIN, 16));

		//Button
		addButton = new JButton("Add");
		addButton.setBounds(150, 250, 70, 25);
		addButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		addButton.addActionListener(this);

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
		panel.add(scrollPane);

		this.add(panel);
		this.getRootPane().setDefaultButton(addButton);

		//Initializing results.csv
		appendResult("ID,Name,Time\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Necessary objects
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		String text = text1.getText().trim();
		Calendar calendar;
	
		if (!text.equals("")) {
			//Getting required attributes
			calendar = Calendar.getInstance();
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);
			int seconds = calendar.get(Calendar.SECOND);
			int milliseconds = calendar.get(Calendar.MILLISECOND);			
			int id = Integer.valueOf(text);
			String name = Main.identify(id), time = new String(hours+":"+minutes+":"+seconds+":"+milliseconds);
			
			//Table update phase
			if (name != null) {
				model.addRow(new Object[] {
					id,
					name,
					time
				});

				appendResult(id+","+name+","+time+"\n"); //Updating results.csv due to unexpected crash
			}
		}

		//Making text field prepared for next submissions
		text1.setText("");
	}

	private void appendResult(String str) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/results.csv", true));
			writer.write(str);
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
