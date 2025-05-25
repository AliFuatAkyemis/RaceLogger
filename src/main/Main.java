package main;

import frames.*;
import javax.swing.*;

public class Main {
	private static JFrame login, dash, record, list;

	public static void main(String[] args) {
		showLogin();
	}

	//Navigation
	public static void showLogin() {
		login = new LoginFrame(); //Initialize the login frame
		login.setVisible(true);
	}

	public static void showDash() {
		if (dash == null) dash = new DashFrame(); //Initializethe login frame
		dash.setVisible(true);
	}
	
	public static void showRecord() {
		record = new RecordFrame(); //Create a new frame
		record.setVisible(true);
	}

	public static void showList() {
		list = new ListFrame();
		list.setVisible(true);
	}
}
