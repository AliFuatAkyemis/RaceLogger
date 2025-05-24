package main;

import frames.*;
import java.io.*;
import java.util.HashMap;

public class Main {
	private static LoginFrame login;
	private static DashFrame dash;
	private static RecordFrame record;
	private static HashMap map;

	public static void main(String[] args) {
		login = new LoginFrame();
		login.setVisible(true);
//		showRecord();
	}

	//Navigation
	public static void showRecord() {
		if (record == null) record = new RecordFrame();
		mapInit(); //Before making main frame visible initialize the map
		record.setVisible(true);
	}

	//Utility
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
}
