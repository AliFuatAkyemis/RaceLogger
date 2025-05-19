package main;

import frames.*;

public class Main {
	private static LoginFrame login;
	private static MainFrame main;
	private static String[] racers;

	public static void main(String[] args) {
		login = new LoginFrame();
		login.setVisible(true);
		showMain();
	}

	public static void showMain() {
		login.setVisible(false);
		if (main == null) main = new MainFrame();
		main.setVisible(true);
	}
}
