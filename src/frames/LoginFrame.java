package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginFrame extends TemplateFrame {
	private int width = 300, height = 200;
	private JTextField username;
	private JPasswordField password;
	private JLabel id, pw;
	private JButton loginButton;
	private String defUser = "admin";
	private String defPass = "1234";

	public LoginFrame() {
		//Frame Properties
		this.setTitle("Login");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation((1920-width)/2, (1080-height)/2);
		this.setResizable(false);

		//Panel
		JPanel panel = new JPanel();
		panel.setLayout(null);

		//Label
		id = new JLabel("ID:");
		id.setBounds(25, 20, 30, 30);
		id.setFont(new Font("Arial", Font.PLAIN, 16));

		pw = new JLabel("PW:");
		pw.setBounds(20, 60, 30, 30);
		pw.setFont(new Font("Arial", Font.PLAIN, 16));

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
				if (username.getText().equals(defUser) && String.valueOf(password.getPassword()).equals(defPass)) {
					this.setVisible(false);
					Main.showDash();
				} //Static method from Main.java
				else System.out.println("Access Denied");
			}
		});

		//Composition part
		panel.add(id);
		panel.add(pw);
		panel.add(username);
		panel.add(password);
		panel.add(loginButton);

		this.add(panel, BorderLayout.CENTER);
		this.getRootPane().setDefaultButton(loginButton);
	}
}
