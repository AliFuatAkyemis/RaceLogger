package frames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {
	int width = 800, height = 600;
	JPanel panel;
	JTextField text1;
	JButton addButton;

	public MainFrame() {
		//Frame
		this.setTitle("Main");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation((1920-width)/2, (1080-height)/2);
		this.setResizable(false);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//TextField
		text1 = new JTextField(100);
		text1.setBounds(30, 30, 100, 25);
		text1.setFont(new Font("MV Boli", Font.PLAIN, 16));

		//Button
		addButton = new JButton("Add");
		addButton.setBounds(150, 30, 70, 25);
		addButton.setFont(new Font("Arial", Font.PLAIN, 16));

		panel.add(text1);
		panel.add(addButton);

		this.add(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	} 
}
