/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spatter.sequenceprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author faroq
 */

/*
 * Copyright (C) 2011-2 Faroq AL-Tam and Hamid Reza Shahbazkia, 2015 Sean
 * Clifford This program is part of spatter.
 * 
 * If you want to improve the software, this is free software for non-commercial
 * purposes.
 */
public class SpaType {

	ArrayList<Entry> entries = new ArrayList<Entry>();
	File file;

	public SpaType() {
	}

	public SpaType(File file) {
		this.file = file;
	}

	public boolean load() {
		if (file == null) {
			return false;
		}
		if (!file.exists()) {
			return false;
		}

		try {

			BufferedReader bFreader = null;
			bFreader = new BufferedReader(new FileReader(file));
			String nameline = null;
			String stringline = null;
			int linescounter = 0;
			// repeat until all lines are read
			// lines should be even so we starts form 1 to n

			// the file has the following structure
			// t\d,\d+

			while ((nameline = bFreader.readLine()) != null) {
				// now read the string line
				if (nameline.equals("")) {
					continue; // empty line
				}
				linescounter++;
				int commaindex = nameline.indexOf(",");
				if (commaindex == -1) {
					System.out.println(
							"Oops! can not find the seperator" + "',' in the file at line:" + linescounter + 1);
					continue;
				}

				stringline = nameline.substring(commaindex + 1, nameline.length());
				nameline = nameline.substring(0, commaindex);
				Entry ent = new Entry(nameline, stringline);
				entries.add(ent);
			}

		} catch (IOException ex) {
			Logger.getLogger(SpaType.class.getName()).log(Level.SEVERE, null, ex);
		}
		return true;
	}

	/**
	 * return the ID of the type given its value
	 *
	 * @param value
	 * @return
	 */
	public String getidByvalue(String value) {

		int size = entries.size();
		for (int i = 0; i < size; i++) {
			Entry e = entries.get(i);
			if (e.value.equals(value)) {
				return e.name;
			}
		}
		return null; // not ound

	}

	public ArrayList<String> gettypeIDfromSequence(String seqString) {
		ArrayList<String> types = new ArrayList<String>();
		String resultString = seqString;
		int size = entries.size();
		for (int i = 0; i < size; i++) {
			if (entries.get(i).getValue().equals("")) {
				continue;
			}
			String oldresult = resultString;
			resultString = resultString.replace(entries.get(i).getValue(), entries.get(i).getName());
			if (!oldresult.equals(resultString)) { // something were found
				// System.out.println("entry name:"+entries.get(i));
				types.add(entries.get(i).getName());
			}
		}
		return types;
	}

	public ArrayList<String> gettypefromRepeats(String repeats) {
		ArrayList<String> names = new ArrayList<String>();
		String typeString = repeats;

		int size = entries.size();
		for (int i = 0; i < size; i++) {
			String value = entries.get(i).getValue();
			if (value.equals("")) {
				continue;
			}
			if (repeats.equals(value)) {
				int start = repeats.indexOf(value);
				int end = start + value.length();
				names.add(Integer.toString(value.length()));
				names.add(value);
				names.add(Integer.toString(start + 1));
				names.add(Integer.toString(end + 1));
				return names;
			}

		}
		names.add("");
		names.add("");
		names.add("");
		names.add("");
		return names;
	}

	public void SpaRepeate(File file) {
		this.file = file;
	}

	public void print() {
		int size = entries.size();
		for (int i = 0; i < size; i++) {
			System.out.println(entries.get(i).toString());
		}
		System.out.println("Number of entries: " + size);

	}

	public Entry getEntry(int index) {

		return entries.get(index);
	}

	public int getlength() {
		return entries.size();
	}
}
