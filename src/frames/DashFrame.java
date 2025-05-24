package frames;

import main.*;
import java.awt.*;
import javax.swing.*;

public class DashFrame extends JFrame {
	private int width = 250, height = 220;
	private JPanel panel;
	private JButton createRecord, records, settings;

	public DashFrame() {
		//Frame
		this.setTitle("DashBoard");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation((1920-width)/2, (1080-height)/2);
		this.setResizable(false);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Button
		createRecord = new JButton("Create Record");
		createRecord.setBounds((width-150)/2, 25, 150, 25);
		createRecord.setFont(new Font("MV Boli", Font.PLAIN, 16));
		createRecord.addActionListener(e -> {
			this.setVisible(false);
			Main.showRecord();
		});
		
		records = new JButton("Records");
		records.setBounds((width-150)/2, 75, 150, 25);
		records.setFont(new Font("MV Boli", Font.PLAIN, 16));
		records.addActionListener(e -> {});
		
		settings = new JButton("Settings");
		settings.setBounds((width-150)/2, 125, 150, 25);
		settings.setFont(new Font("MV Boli", Font.PLAIN, 16));
		settings.addActionListener(e -> {});

		//Composition part
		panel.add(createRecord);
		panel.add(records);
		panel.add(settings);

		this.add(panel);
	}
}
