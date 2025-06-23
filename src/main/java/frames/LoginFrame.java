package frames;

import controller.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class LoginFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 300, height = 200, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JTextField username;
	private JPasswordField password;
	private JLabel id, pw;
	private JButton loginButton;
	private String defUser = "";
	private String defPass = "";

	public LoginFrame() {
		//Frame Properties
		this.setTitle("Login");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
                this.setLocation(x, y);

		//Panel
		JPanel panel = new JPanel();
		panel.setLayout(null);

		//Label
		id = new JLabel("ID:");
		id.setBounds(25, 20, 30, 30);
		id.setFont(super.defaultBoldFont);

		pw = new JLabel("PW:");
		pw.setBounds(20, 60, 35, 30);
		pw.setFont(super.defaultBoldFont);

		//TextField
		username = new JTextField();
		username.setBounds(60, 20, 200, 30);
		username.setFont(new Font("Arial", Font.PLAIN, 20));
		
		//PasswordField
		password = new JPasswordField();
		password.setBounds(60, 60, 200, 30);
		password.setFont(new Font("Arial", Font.PLAIN, 20));

		//Button
		loginButton = new JButton("Login");
		loginButton.setBounds(75, 110, 150, 30);
		loginButton.addActionListener(e -> {
			//Basic if block to check authorization
			if (e.getSource() == loginButton) {
				if (username.getText().trim().equals(defUser) && String.valueOf(password.getPassword()).equals(defPass)) {
					this.setVisible(false);
					Main.showDash();
				} //Static method from Main.java
				else {
                                        System.out.println("Access Denied");
                                        username.setText("");
                                        password.setText("");
                                }
			}
		});

		//Composition part
		panel.add(id);
		panel.add(pw);
		panel.add(username);
		panel.add(password);
		panel.add(loginButton);

                loadAuth(); //Load authorization informations

		this.add(panel, BorderLayout.CENTER);
		this.getRootPane().setDefaultButton(loginButton);
	}

        private void loadAuth() { //Saving authorization informations to a .csv file
                try {
                        File file = new File("data/logininfo/authorization.csv"); //Create a File instace
                        BufferedReader reader = null; //Define a BufferedReader instance
                        if (file.exists()) { //file check if it is exists
                                //Then, do opertions to save
                                reader = new BufferedReader(new FileReader(file));
                                String[] temp = reader.readLine().split(",");
                                defUser = temp[0];
                                defPass = temp[1];
                        }
                } catch(Exception e) {
                        e.printStackTrace();
                }
        }
}
