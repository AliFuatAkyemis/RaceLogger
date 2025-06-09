package frames;

import javax.swing.*;

public class TemplateFrame extends JFrame {
	public TemplateFrame() {
		ImageIcon icon = new ImageIcon("images/icon.png");
		this.setIconImage(icon.getImage());
		this.setResizable(false);
	}
}
