package main;

import frames.*;
import javax.swing.*;

public class Main {
	private static LoginFrame login;
	private static DashFrame dash;
	private static RecordFrame record;
	private static ListFrame list;
	private static AnalyzeFrame analyze;
	private static SettingFrame setting;

	public static void main(String[] args) {
		if (new SettingFrame().getConfig()[0]) showLogin();
		else showDash();
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
