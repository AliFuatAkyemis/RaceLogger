package main;

import frames.*;
import javax.swing.*;

public class Main {
	private static JFrame login, dash, record, list, analyze, setting;

	public static void main(String[] args) {
		showLogin();
	}

	//Navigation
	public static void showLogin() {
		if (login != null) login.dispose();
		login = new LoginFrame();
		login.setVisible(true);
	}

	public static void showDash() {
		if (dash != null) dash.dispose();
		dash = new DashFrame();
		dash.setVisible(true);
	}
	
	public static void showRecord() {
		if (record != null) record.dispose();
		record = new RecordFrame();
		record.setVisible(true);
	}

	public static void showList() {
		if (list != null) list.dispose();
		list = new ListFrame();
		list.setVisible(true);
	}

	public static void showAnalyze(String filename) {
		if (analyze != null) analyze.dispose();
		analyze = new AnalyzeFrame(filename);
		analyze.setVisible(true);
	}

	public static void showSetting() {
		if (setting != null) setting.dispose();
		setting = new SettingFrame();
		setting.setVisible(true);
	}
}
