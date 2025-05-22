package main;

import frames.*;
import java.io.*;
import java.util.HashMap;

public class Main {
	private static LoginFrame login;
	private static MainFrame main;
	private static HashMap map;
	private static boolean isPaused = true;
	private static Thread chronoThread;

	public static void main(String[] args) {
		login = new LoginFrame();
		login.setVisible(true);
		showMain();
	}

	public static void showMain() {
		login.setVisible(false);
		if (main == null) main = new MainFrame();
		mapInit(); //Before making main frame visible initialize the map
		main.setVisible(true);
	}
	
	@SuppressWarnings("unchecked")
	public static void mapInit() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/racers.csv"));
			map = new HashMap<Integer, String>();
			String row = reader.readLine(); //First line of file
		
			while (row != null) {
				String[] temp = row.split(","); //Simple split method to seperate ID and Name
				map.put(Integer.valueOf(temp[0]), temp[1]); //Mapping IDs and Names
				row = reader.readLine(); //Update row with next line
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String identify(int id) {
		return (String) map.get(id);
	}

	public static void startChronometer() {
		//New Thread to keep track of time
		chronoThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					if (!isPaused) main.chronoUpdate(); //Chronometer label update function
					Thread.sleep(100); //Update wait time
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});

		chronoThread.start();
	}

	public static boolean getIsPaused() {
		return isPaused;
	}

	public static void switchPauseState() {
		isPaused = !isPaused;
	}
}
