package frames;

import main.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AnalyzeFrame extends TemplateFrame {
	private int width = 400, height = 400;

	public AnalyzeFrame() {
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
	}

	private long convertToMillisecond(String time) {
		//Conversions
		String[] temp = time.split(":");
		return (Integer.valueOf(temp[0])*60*60*1000)+(Integer.valueOf(temp[1])*60*1000)+(Integer.valueOf(temp[2])*1000)+Integer.valueOf(temp[3]);
	}
}
