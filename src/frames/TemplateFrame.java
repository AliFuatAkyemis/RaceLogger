package frames;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Font;

public class TemplateFrame extends JFrame {
        protected Font defaultPlainFont = new Font("Arial", Font.PLAIN, 16);
        protected Font defaultBoldFont = new Font("Arial", Font.BOLD, 16);

	public TemplateFrame() {
		ImageIcon icon = new ImageIcon("images/icon.png");
		this.setIconImage(icon.getImage());
		this.setResizable(false);
	}
}
