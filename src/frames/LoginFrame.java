package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
	private int width = 300, height = 200;
	private JTextField text1, text2;
	private JLabel id, password;
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
		id.setFont(new Font("MV Boli", Font.BOLD, 16));

		password = new JLabel("PW:");
		password.setBounds(20, 60, 30, 30);
		password.setFont(new Font("MV Boli", Font.BOLD, 16));

		//TextField
		text1 = new JTextField(100);
		text2 = new JTextField(100);

		text1.setBounds(60, 20, 200, 30);
		text2.setBounds(60, 60, 200, 30);

		text1.setFont(new Font("MV Boli", Font.PLAIN, 20));
		text2.setFont(new Font("MV Boli", Font.PLAIN, 20));

		//Button
		loginButton = new JButton("Login");
		loginButton.setBounds(75, 110, 150, 30);
		loginButton.addActionListener(e -> {
			//Basic if block to check authorization
			if (e.getSource() == loginButton) {
				String username = text1.getText();
				String password = text2.getText();
				if (username.equals(defUser) && password.equals(defPass)) {
					this.setVisible(false);
					Main.showDash();
				} //Static method from Main.java
				else System.out.println("Access Denied");
			}
		});

		//Composition part
		panel.add(id);
		panel.add(password);
		panel.add(text1);
		panel.add(text2);
		panel.add(loginButton);

		this.add(panel, BorderLayout.CENTER);
		this.getRootPane().setDefaultButton(loginButton);
	}
}
