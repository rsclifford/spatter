/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spatter.sequenceprocessing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import spatter.sequenceprocessing.Entry;
import spatter.sequenceprocessing.SpaProcessor;
import spatter.sequenceprocessing.StringUtil;

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

public class SpaProcessor {
	ArrayList<SpaType> spaTypes = new ArrayList<SpaType>();
	ArrayList<Sequence> sequences = new ArrayList<Sequence>();

	/**
	 * Create a spatype from a sequence by using given repeats
	 * 
	 * @param sequence
	 * @param filename
	 */

	public static String DNAComplement(String dnastring) {

		// A<->T
		// C<->G
		// TACG
		// !@$^
		// !@$C
		// !@GC
		// !TGC
		// ATGC done

		dnastring = dnastring.replace('T', '!');
		dnastring = dnastring.replace('A', '@');
		dnastring = dnastring.replace('C', '$');
		dnastring = dnastring.replace('G', '^');

		dnastring = dnastring.replace('!', 'A');
		dnastring = dnastring.replace('@', 'T');
		dnastring = dnastring.replace('$', 'G');
		dnastring = dnastring.replace('^', 'C');

		// now reverse it
		StringBuilder stringBuffer = new StringBuilder(dnastring);

		dnastring = stringBuffer.reverse().toString();

		return dnastring;

	}

	public int toType(Entry sequence, BufferedWriter bfwritter, SpaRepeate repeate, SpaType type, String filename) {

		int wrote = 0;
		try {

			String results = sequence.getValue().toUpperCase();
			// write sequence name and the original sequence

			String start_string = "";
			String end_string = "";
			String length = "";
			String left_region = "";
			String right_region = "";
			int left_start = 0;
			int left_end = 0;
			int right_start = 0;
			int right_end = 0;
			int start_int = 0;
			int end_int = 0;
			int verified_reads = 0;
			ArrayList<String> left_reads = new ArrayList<String>();
			left_reads.add("ACAACAAAA");
			left_reads.add("GCAACAAAA");
			left_reads.add("ACACCAAAA");
			left_reads.add("GCACCAAAA");
			left_reads.add(DNAComplement("ACAACAAAA"));
			left_reads.add(DNAComplement("GCAACAAAA"));
			left_reads.add(DNAComplement("ACACCAAAA"));
			left_reads.add(DNAComplement("GCACCAAAA"));
			ArrayList<String> right_reads = new ArrayList<String>();
			right_reads.add("TACATGTCGT");
			right_reads.add("TATATGTCGT");
			right_reads.add(DNAComplement("TATATGTCGT"));
			right_reads.add(DNAComplement("TATATGTCGT"));

			int repeatsize = repeate.getlength();
			String newrepeats = "";
			// write sequence name and the original sequence

			String repeats = results;
			// System.out.println(results);
			System.out.println("Finding repeats");
			int repeatsfound = 0;
			for (int i = 0; i < repeatsize; i++) {
				Entry current_repeat = repeate.getEntry(i);

				Pattern pattern = Pattern.compile(current_repeat.getValue());
				Matcher matcher = pattern.matcher(results);

				String repeatname = StringUtil.getNumberonly(current_repeat.getName());
				if (repeatname.equals("0")) // new repeat get value 0
					continue;

				if (matcher.find() == true) {
					results = matcher.replaceAll("-" + repeatname);
					repeatsfound++;
					repeats = pattern.matcher(repeats).replaceAll("-" + repeatname);

				}

			}
			System.out.println("Repeats found:" + repeatsfound);

			// now find the new repeats
			System.out.println("Finding new repeats");
			int newrepeatsNO = 0;
			newrepeats = results;
			String nrepeats = "";
			for (int i = 0; i < repeatsize; i++) {
				Entry current_repeat = repeate.getEntry(i);
				Pattern pattern = Pattern.compile(current_repeat.getValue());
				Matcher matcher = pattern.matcher(newrepeats);
				String repeatname = StringUtil.getNumberonly(current_repeat.getName());
				if (!repeatname.equals("0")) // new repeat get value 0
					continue;

				if (matcher.find() == false)
					continue;
				int start = matcher.toMatchResult().start();
				int end = matcher.toMatchResult().end();
				if (!newrepeats.equals(""))
					newrepeats += "\n";
				nrepeats += newrepeats.substring(start, end);
				newrepeats = matcher.replaceAll("-" + repeatname);
				newrepeatsNO++;
			}
			;
			System.out.println("New repeats:" + newrepeatsNO);

			// keep the repeats only and remove anything else
			repeats = StringUtil.getAsHexaFormated(repeats);

			if (repeats != null) {

				ArrayList<String> normal_entry = type.gettypefromRepeats(repeats);
				ArrayList<String> complementry = type.gettypefromRepeats(DNAComplement(repeats));
				String entry = "";

				if (normal_entry.get(0) == "") {
					normal_entry.set(0, "0");
				}

				if (complementry.get(0) == "") {
					complementry.set(0, "0");
				}

				if (Integer.parseInt(normal_entry.get(0)) < Integer.parseInt(complementry.get(0))) {
					entry = complementry.get(1) + "R";
					start_string = complementry.get(2);
					end_string = complementry.get(3);
					length = complementry.get(0);
				} else {
					entry = normal_entry.get(1);
					start_string = normal_entry.get(2);
					end_string = normal_entry.get(3);
					length = normal_entry.get(0);
				}

				if (entry != "") {
					System.out.println(sequence.getName());
					System.out.println("TYPES: " + entry);
					System.out.println("Start point: " + start_string);
					System.out.println("End point: " + end_string);
					System.out.println("Length: " + length);
					System.out.println();
					start_int = Integer.parseInt(start_string);
					end_int = Integer.parseInt(end_string);

					if (start_int < 50 || repeats.length() - end_int < 50) {
						System.out.println("WARNING: gene may be truncated!");
						entry = entry + "T";
						System.out.println();
					} else {
						left_region = repeats.substring(start_int - 101, start_int - 1);
						right_region = repeats.substring(end_int + 1, end_int + 101);
						for (String read : left_reads) {
							if (left_region.contains(read)) {
								System.out.println("Found a read! " + read);
								verified_reads++;
								left_start = start_int - 100 + left_region.indexOf(read);
								left_end = left_start + read.length();
								System.out.println("Left chunk location: " + left_start + "-" + left_end);
								System.out.println();
								break;
							}
						}
						for (String read : right_reads) {
							if (right_region.contains(read)) {
								System.out.println("Found a read! " + read);
								verified_reads++;
								right_start = end_int + 2 + right_region.indexOf(read);
								right_end = right_start + read.length();
								System.out.println("Right chunk location: " + right_start + "-" + right_end);
								System.out.println();
								break;
							}
						}
						if (verified_reads >= 2) {
							System.out.println("Verified repeat!");
							entry = entry + "V";
							System.out.println();
						}
					}
				}

				if (entry.contains("t")) {
					bfwritter.write(filename + '\t');
					bfwritter.write(sequence.getName() + '\t');
					bfwritter.write("[" + entry.substring(1) + "]" + '\t');
					bfwritter.write(start_string + '\t');
					bfwritter.write(end_string + '\t');
					bfwritter.write(length + '\t');
					if (left_start > 0 && left_end > 0) {
						bfwritter.write(left_start + "-" + left_end + '\t');
					} else {
						bfwritter.write("N/A" + '\t');
					}
					if (right_start > 0 && right_end > 0) {
						bfwritter.write(right_start + "-" + right_end + '\t');
					} else {
						bfwritter.write("N/A" + '\t');
					}
					wrote++;
					bfwritter.newLine();
					bfwritter.flush();
				}
			}

		} catch (

		IOException ex)

		{
			Logger.getLogger(SpaProcessor.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("error:" + ex.getMessage());

		}

		return wrote;

	}

}
