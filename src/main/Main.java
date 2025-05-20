package main;

import frames.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Main {
	private static LoginFrame login;
	private static MainFrame main;
	private static String[] racers;

	public static void main(String[] args) {
		login = new LoginFrame();
		login.setVisible(true);
//		showMain();
	}

	public static void showMain() {
		login.setVisible(false);
		if (main == null) main = new MainFrame();
		main.setVisible(true);
	}

	public static String identify(int id) {
		try {
			BufferedReader read = new BufferedReader(new FileReader("data/racers.csv"));
			List<String> list = new ArrayList<>();
			list.add("");
			String row = read.readLine();
		
			while (row != null) {
				String[] temp = row.split(",");
				list.add(temp[1]);
				row = read.readLine();
			}

			return list.get(id);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
