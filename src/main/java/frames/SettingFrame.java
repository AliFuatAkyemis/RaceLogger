package frames;

import controller.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.util.HashMap;

public class SettingFrame extends TemplateFrame {
        private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 300, height = 500, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private HashMap<String, Boolean> config = new HashMap<>();
	private JPanel panel;
	private JLabel loginL, idL, passwordL, authL;
        private JTextField id, password;
	private JButton back, save, login;

	public SettingFrame() {
		//Load Configs before startup
		loadConfig();

		//Frame
		this.setTitle("Settings");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation(x, y);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Label
		loginL = new JLabel("Login Required");
		loginL.setBounds(20, 50, 130, 25);
		loginL.setFont(super.defaultPlainFont);

                authL = new JLabel("Authentication Info:");
                authL.setBounds(50, 120, 180, 25);
                authL.setFont(defaultPlainFont);

                idL = new JLabel("ID:");
                idL.setBounds(40, 160, 80, 25);
                idL.setFont(defaultPlainFont);

                passwordL = new JLabel("PW:");
                passwordL.setBounds(40, 190, 80, 25);
                passwordL.setFont(defaultPlainFont);

                //TextField
                id = new JTextField();
                id.setBounds(80, 160, 150, 25);
                id.setFont(defaultPlainFont);

                password = new JTextField();
                password.setBounds(80, 190, 150, 25);
                password.setFont(defaultPlainFont);

		//Button
		back = new JButton("Back");
		back.setBounds(20, 10, 80, 25);
		back.setFont(defaultPlainFont);
		back.addActionListener(e -> {
			this.dispose();
			Main.showDash();
		});

		save = new JButton("Save");
		save.setBounds(200, 420, 80, 25);
		save.setFont(defaultPlainFont);
		save.addActionListener(e -> {
			saveConfig(); //Config save
                        updateAuth(id.getText().trim(), password.getText()); //Authorizations info save
		});

		login = new JButton(config.get("login") ? "ON" : "OFF");
		login.setBounds(200, 50, 70, 25);
		login.setFont(defaultPlainFont);
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
                panel.add(authL);
                panel.add(idL);
                panel.add(passwordL);
                panel.add(id);
                panel.add(password);

                checkFileStructure(); //Creates required folders

		this.add(panel);
	}

	//Utility
	private void checkFileStructure() {
                File folder = new File("data/config");

                if (!folder.exists()) folder.mkdirs();
        }

        private void saveConfig() { //Config save function to settings.csv
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/config/settings.csv"));
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
			File file = new File("data/config/settings.csv");
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

        private void updateAuth(String id, String password) { //Update function for authorization informations
                try {
                        if (id.equals("") || password.equals("")) return; //If textfields are empty do nothing
                        
                        //Otherwise go on save process
                        File file = new File("data/logininfo/authorization.csv");
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                        writer.write(id+","+password);
                        writer.close();
                } catch(Exception e) {
                        e.printStackTrace();
                }
        }
}
