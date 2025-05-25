package frames;

import main.*;
import java.awt.*;
import javax.swing.*;

public class DashFrame extends TemplateFrame {
	private int width = 250, height = 275;
	private JPanel panel;
	private JButton createRecord, records, settings, exit;

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
		createRecord.setFont(new Font("Arial", Font.PLAIN, 16));
		createRecord.addActionListener(e -> {
			this.setVisible(false);
			Main.showRecord();
		});
		
		records = new JButton("Records");
		records.setBounds((width-150)/2, 75, 150, 25);
		records.setFont(new Font("Arial", Font.PLAIN, 16));
		records.addActionListener(e -> {
			this.setVisible(false);
			Main.showList();
		});
		
		settings = new JButton("Settings");
		settings.setBounds((width-150)/2, 125, 150, 25);
		settings.setFont(new Font("Arial", Font.PLAIN, 16));
		settings.addActionListener(e -> {});

		exit = new JButton("Exit");
		exit.setBounds((width-150)/2, 175, 150, 25);
		exit.setFont(new Font("Arial", Font.PLAIN, 16));
		exit.addActionListener(e -> {
			System.exit(0);
		});

		//Composition part
		panel.add(createRecord);
		panel.add(records);
		panel.add(settings);
		panel.add(exit);

		this.add(panel);
	}
}
