package frames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class MainFrame extends JFrame implements ActionListener {
	int width = 800, height = 600;
	JPanel panel, raceMap;
	JTable table;
	JScrollPane scrollPane;
	JTextField text1;
	JButton addButton;
	ImageIcon map;
	Image scaled;

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

		//Table
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] {"ID", "Name", "Time"});
		table = new JTable(model);
		table.setFillsViewportHeight(false);

		//ScrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(425, 25, 350, 500);

		//TextField
		text1 = new JTextField(100);
		text1.setBounds(30, 250, 100, 25);
		text1.setFont(new Font("MV Boli", Font.PLAIN, 16));

		//Button
		addButton = new JButton("Add");
		addButton.setBounds(150, 250, 70, 25);
		addButton.setFont(new Font("MV Boli", Font.PLAIN, 16));
		addButton.addActionListener(this);

		//RaceMapPanel
		raceMap = new JPanel();
		raceMap.setBounds(30, 20, 350, 200);
		raceMap.setLayout(new BorderLayout());
		raceMap.setBackground(new Color(0xaaaaaa));

		//Image
		ImageIcon map = new ImageIcon("/home/alifuatakyemis/Git/RaceLogger/images/route.png");
		Image scaled = map.getImage().getScaledInstance(350, 200, Image.SCALE_SMOOTH);

		//Compose
		raceMap.add(new JLabel(new ImageIcon(scaled)), BorderLayout.CENTER);

		panel.add(raceMap);
		panel.add(text1);
		panel.add(addButton);
		panel.add(scrollPane);

		this.add(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		model.addRow(new Object[] {
			(int) (Math.random() * 10),
			"Ali Fuat Akyemiş",
			Integer.valueOf(text1.getText())
		});

		text1.setText("");
	} 
}
