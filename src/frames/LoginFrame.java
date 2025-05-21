package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginFrame extends JFrame implements ActionListener {
	int width = 400, height = 280;
	JTextField text1, text2;
	JButton loginButton;
	String defUser = "admin";
	String defPass = "1234";

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

		//TextField
		text1 = new JTextField(100);
		text2 = new JTextField(100);

		text1.setBounds(50, 50, 300, 40);
		text2.setBounds(50, 100, 300, 40);

		text1.setFont(new Font("MV Boli", Font.PLAIN, 30));
		text2.setFont(new Font("MV Boli", Font.PLAIN, 30));

		//Button
		loginButton = new JButton("Login");
		loginButton.setBounds(125, 175, 150, 30);
		loginButton.addActionListener(this);

		//Composition part
		panel.add(text1);
		panel.add(text2);
		panel.add(loginButton);

		this.add(panel, BorderLayout.CENTER);
		this.getRootPane().setDefaultButton(loginButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Basic if block to check authorization
		if (e.getSource() == loginButton) {
			String username = text1.getText();
			String password = text2.getText();
			if (username.equals(defUser) && password.equals(defPass)) Main.showMain(); //Static method from Main.java
			else System.out.println("Access Denied");
		}
	}
}
