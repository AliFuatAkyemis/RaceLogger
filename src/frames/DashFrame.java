package frames;

import main.*;
import java.awt.*;
import javax.swing.*;

public class DashFrame extends JFrame {
	int width = 400, height = 600;

	public DashFrame() {
		//Frame
		this.setTitle("DashBoard");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocation((1920-width)/2, (1080-height)/2);
		this.setResizable(false);
	}
}
