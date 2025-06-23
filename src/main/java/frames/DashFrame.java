package frames;

import controller.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.io.File;

public class DashFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 250, height = 275, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
	private JButton createRecord, records, settings, exit;

	public DashFrame() {
		//Frame
		this.setTitle("DashBoard");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
                this.setLocation(x, y);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Button
		createRecord = new JButton("Create Record");
		createRecord.setBounds((width-150)/2, 25, 150, 25);
		createRecord.setFont(super.defaultPlainFont);
		createRecord.addActionListener(e -> {
			this.dispose();
			Main.showRecord();
		});
		
		records = new JButton("Past Records");
		records.setBounds((width-150)/2, 75, 150, 25);
		records.setFont(super.defaultPlainFont);
		records.addActionListener(e -> {
			this.dispose();
			Main.showList();
		});
		
		settings = new JButton("Settings");
		settings.setBounds((width-150)/2, 125, 150, 25);
		settings.setFont(super.defaultPlainFont);
		settings.addActionListener(e -> {
			this.dispose();
			Main.showSetting();
		});

		exit = new JButton("Exit");
		exit.setBounds((width-150)/2, 175, 150, 25);
		exit.setFont(super.defaultPlainFont);
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

        //Utilities
        private void checkFileStructure() {
                File folder = new File("data");
                
                if (!folder.exists()) folder.mkdirs();
        }
}
