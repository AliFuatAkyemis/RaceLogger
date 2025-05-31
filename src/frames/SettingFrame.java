package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class SettingFrame extends TemplateFrame {
	private int width = 300, height = 500;
	private String[] config = {"login"};
	private boolean[] values = {false};
	private JPanel panel;
	private JLabel loginL;
	private JButton back, save, login;

	public SettingFrame() {
		//Load Configs before startup
		loadConfig();

		//Frame
		this.setTitle("Settings");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation((1920-width)/2, (1080-height)/2);
		this.setResizable(false);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Label
		loginL = new JLabel("Login Required");
		loginL.setBounds(20, 50, 130, 25);
		loginL.setFont(new Font("Arial", Font.PLAIN, 16));

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

		String str = values[0] ? "ON" : "OFF"; 
		login = new JButton(str);
		login.setBounds(200, 50, 70, 25);
		login.setFont(new Font("Arial", Font.PLAIN, 16));
		login.addActionListener(e -> {
			if (login.getText().equals("ON")) {
				login.setText("OFF");
				values[0] = false;
			} else {
				login.setText("ON");
				values[0] = true;
			}
		});

		//Composition part
		panel.add(back);
		panel.add(save);
		panel.add(loginL);
		panel.add(login);

		this.add(panel);
	}

	//Utility
	private void saveConfig() { //Config save function to settings.csv
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/configurations/settings.csv"));
			for (int i = 0; i < config.length; i++) {
				writer.write(config[i]+","+String.valueOf(values[i])+"\n");
			}
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void loadConfig() { //Config load function to load on startup
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/configurations/settings.csv"));
			String row = reader.readLine();
			int i = 0;

			while (row != null && i < values.length) {
				String[] temp = row.split(",");
				config[i] = temp[0];
				values[i++] = Boolean.parseBoolean(temp[1]);
			}

			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean[] getConfig() {
		return this.values;
	}
}
