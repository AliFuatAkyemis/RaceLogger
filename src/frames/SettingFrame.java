package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.HashMap;

public class SettingFrame extends TemplateFrame {
	private int width = 300, height = 500;
	private HashMap<String, Boolean> config = new HashMap<>();
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

		login = new JButton(config.get("login") ? "ON" : "OFF");
		login.setBounds(200, 50, 70, 25);
		login.setFont(new Font("Arial", Font.PLAIN, 16));
		login.addActionListener(e -> {
			if (login.getText().equals("ON")) {
				login.setText("OFF");
				config.put("login", false);
			} else {
				login.setText("ON");
				config.put("login", true);
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
			for (String str : config.keySet()) {
				writer.write(str+","+config.get(str)+"\n");
			}
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void loadConfig() { //Config load function to load on startup
		try {
			File file = new File("data/configurations/settings.csv");
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String row = reader.readLine();
	
				while (row != null) {
					String[] temp = row.split(",");
					config.put(temp[0], Boolean.parseBoolean(temp[1]));
					row = reader.readLine();
				}
	
				reader.close();
			} else { //If the file does not exist use default configurations
				config.put("login", false);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, Boolean> getConfig() { //Basic getter function for config load in Main
		return this.config;
	}
}
