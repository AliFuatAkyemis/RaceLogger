package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SettingFrame extends TemplateFrame {
	private int width = 300, height = 500;
	private JPanel panel;
	private JButton back;

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

		//Composition part
		panel.add(back);

		this.add(panel);
	}
}
