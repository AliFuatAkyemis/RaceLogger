package frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class TeamAnalyzeFrame extends TemplateFrame {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = 600, height = 480, x = (screenSize.width-width)/2, y = (screenSize.height-height)/2;
	private JPanel panel;
        private JSeparator separator;
	private JLabel timeLimitMale, hourLabelMale, minuteLabelMale, lapLimitMale,
                        timeLimitFemale, hourLabelFemale, minuteLabelFemale, lapLimitFemale,
                        Male, Female;
	private JComboBox<Integer> hourMale, minuteMale, lapMale,
                                        hourFemale, minuteFemale, lapFemale;

        public TeamAnalyzeFrame() {
		//Frame
		this.setTitle("Analyze");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				TeamAnalyzeFrame.this.dispose();
			}
		});

		this.setSize(width, height);
                this.setLocation(x, y);

                //Panel
                panel = new JPanel();
                panel.setLayout(null);

                //Separator
                separator = new JSeparator(SwingConstants.VERTICAL);
                separator.setBounds(width/2, 0, 2, height);
                
		//Label
                Male = new JLabel("Male");
                Male.setFont(super.defaultBoldFont);
                Male.setBounds(120, 5, 70, 25);

                Female = new JLabel("Female");
                Female.setFont(super.defaultBoldFont);
                Female.setBounds(420, 5, 90, 25);

		timeLimitMale= new JLabel("Time Limit:");
		timeLimitMale.setFont(super.defaultPlainFont);
		timeLimitMale.setBounds(20, 40, 90, 25);

		hourLabelMale= new JLabel("h :");
		hourLabelMale.setFont(super.defaultPlainFont);
		hourLabelMale.setBounds(160, 40, 30, 25);

		minuteLabelMale= new JLabel("min");
		minuteLabelMale.setFont(super.defaultPlainFont);
		minuteLabelMale.setBounds(230, 40, 40, 25);

                lapLimitMale= new JLabel("Lap:");
                lapLimitMale.setFont(super.defaultPlainFont);
                lapLimitMale.setBounds(73, 75, 90, 25);

		timeLimitFemale= new JLabel("Time Limit:");
		timeLimitFemale.setFont(super.defaultPlainFont);
		timeLimitFemale.setBounds(320, 40, 90, 25);

		hourLabelFemale= new JLabel("h :");
		hourLabelFemale.setFont(super.defaultPlainFont);
		hourLabelFemale.setBounds(460, 40, 30, 25);

		minuteLabelFemale= new JLabel("min");
		minuteLabelFemale.setFont(super.defaultPlainFont);
		minuteLabelFemale.setBounds(530, 40, 40, 25);

                lapLimitFemale= new JLabel("Lap:");
                lapLimitFemale.setFont(super.defaultPlainFont);
                lapLimitFemale.setBounds(373, 75, 90, 25);

		//ComboBox
		hourMale= new JComboBox<>(range(0, 24));
		hourMale.setBounds(110, 40, 45, 25);
		hourMale.setFont(super.defaultPlainFont);

		minuteMale= new JComboBox<>(range(0, 60));
		minuteMale.setBounds(180, 40, 45, 25);
		minuteMale.setFont(super.defaultPlainFont);

                lapMale= new JComboBox<>(range(0, 10));
                lapMale.setBounds(110, 75, 45, 25);
                lapMale.setFont(super.defaultPlainFont);

		hourFemale= new JComboBox<>(range(0, 24));
		hourFemale.setBounds(410, 40, 45, 25);
		hourFemale.setFont(super.defaultPlainFont);

		minuteFemale= new JComboBox<>(range(0, 60));
		minuteFemale.setBounds(480, 40, 45, 25);
		minuteFemale.setFont(super.defaultPlainFont);

                lapFemale= new JComboBox<>(range(0, 10));
                lapFemale.setBounds(410, 75, 45, 25);
                lapFemale.setFont(super.defaultPlainFont);

                //Composition Part
                panel.add(separator);
                panel.add(Male);
                panel.add(Female);
                panel.add(timeLimitMale);
                panel.add(hourLabelMale);
                panel.add(minuteLabelMale);
                panel.add(lapLimitMale);
                panel.add(timeLimitFemale);
                panel.add(hourLabelFemale);
                panel.add(minuteLabelFemale);
                panel.add(lapLimitFemale);
                panel.add(hourMale);
                panel.add(minuteMale);
                panel.add(lapMale);
                panel.add(hourFemale);
                panel.add(minuteFemale);
                panel.add(lapFemale);

                this.add(panel);
        }

	//Utility
	private Integer[] range(int i, int j) {
		Integer[] arr = new Integer[j-i];
		for (int n = 0; n < j-i; n++) arr[n] = n+i;
		return arr;
	}
}
