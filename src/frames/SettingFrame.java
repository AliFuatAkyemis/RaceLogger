package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class SettingFrame extends TemplateFrame {
	private int width = 300, height = 500;
	private String[] config = {"login", "theme"};
	private boolean[] values = {false, true};
	private JPanel panel;
	private JButton back, save;

	public SettingFrame() {
		//Frame
		this.setTitle("Settings");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation((1920-width)/2, (1080-height)/2);
		this.setResizable(false);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Button
		back = new JButton("Back");
		back.setBounds(20, 10, 80, 25);
		back.setFont(new Font("Arial", Font.PLAIN, 16));
		back.addActionListener(e -> {
			this.dispose();
			Main.showDash();
		});

		save = new JButton("Save");
		save.setBounds(200, 420, 80, 25);
		save.setFont(new Font("Arial", Font.PLAIN, 16));
		save.addActionListener(e -> {
			saveConfig();
		});

		//Composition part
		panel.add(back);
		panel.add(save);

		this.add(panel);
	}

	private void saveConfig() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/configurations/settings.csv", true));
			for (int i = 0; i < config.length; i++) {
				writer.write(config[i]+","+String.valueOf(values[i])+"\n");
			}
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
