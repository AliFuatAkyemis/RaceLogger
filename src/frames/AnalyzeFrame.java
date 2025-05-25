package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AnalyzeFrame extends TemplateFrame {
	private int width = 400, height = 400;
	private JPanel panel;
	private JLabel lapLabel, timeLimit, hourLabel, minuteLabel;
	private JComboBox<Integer> lap, hour, minute;

	public AnalyzeFrame(String filename) {
		//Frame
		this.setTitle("Analyze");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				AnalyzeFrame.this.dispose();
			}
		});

		this.setSize(width, height);
		this.setLocation((1920-width)/2, (1080-height)/2);
		this.setResizable(false);

		//Panel
		panel = new JPanel();
		panel.setLayout(null);

		//Label
		lapLabel = new JLabel("Lap:");
		lapLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		lapLabel.setBounds(10, 10, 40, 25);

		timeLimit = new JLabel("Time Limit:");
		timeLimit.setFont(new Font("Arial", Font.PLAIN, 16));
		timeLimit.setBounds(110, 10, 90, 25);

		hourLabel = new JLabel("h :");
		hourLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		hourLabel.setBounds(250, 10, 30, 25);

		minuteLabel = new JLabel("min");
		minuteLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		minuteLabel.setBounds(320, 10, 40, 25);

		//ComboBox
		lap = new JComboBox<>(range(0, 9));
		lap.setBounds(50, 10, 40, 25);
		lap.setFont(new Font("Arial", Font.PLAIN, 16));

		hour = new JComboBox<>(range(0, 24));
		hour.setBounds(200, 10, 45, 25);
		hour.setFont(new Font("Arial", Font.PLAIN, 16));

		minute = new JComboBox<>(range(0, 60));
		minute.setBounds(270, 10, 45, 25);
		minute.setFont(new Font("Arial", Font.PLAIN, 16));

		//Composition part
		panel.add(lapLabel);
		panel.add(timeLimit);
		panel.add(hourLabel);
		panel.add(minuteLabel);
		panel.add(lap);
		panel.add(hour);
		panel.add(minute);

		this.add(panel);
	}

	//Utility
	public Integer[] range(int i, int j) {
		Integer[] arr = new Integer[j-i];
		for (int n = 0; n < j-i; n++) arr[n] = n+i;
		return arr;
	}
}
