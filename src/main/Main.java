package main;

import frames.*;
import java.io.*;
import java.util.HashMap;

public class Main {
	private static LoginFrame login;
	private static MainFrame main;
	private static HashMap map;
	private static boolean isChronoStart = false;

	public static void main(String[] args) {
		login = new LoginFrame();
		login.setVisible(true);
		showMain();
	}

	public static void showMain() {
		login.setVisible(false);
		if (main == null) main = new MainFrame();

		initMap(); //Before making main frame visible initialize the map
		
		main.setVisible(true);
	}
	
	@SuppressWarnings("unchecked")
	public static void initMap() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/racers.csv"));
			map = new HashMap<Integer, String>();
			String row = reader.readLine();
		
			while (row != null) {
				String[] temp = row.split(",");
				map.put(Integer.valueOf(temp[0]), temp[1]);
				row = reader.readLine();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String identify(int id) {
		return (String) map.get(id);
	}

	public static void startChronometer() {
		Thread updateThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					main.chronoUpdate();
					Thread.sleep(100);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});

		isChronoStart = true;
		updateThread.start();
	}

	public static boolean getChronoState() {
		return isChronoStart;
	}
}
