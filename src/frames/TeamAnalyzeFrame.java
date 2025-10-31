package frames;

import java.awt.Dimension;
import java.awt.Toolkit;

public class TeamAnalyzeFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 600, height = 480, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
}
