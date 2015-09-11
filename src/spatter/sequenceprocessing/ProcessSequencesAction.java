/*
 * Copyright (C) 2011-2 Faroq AL-Tam and Hamid Reza Shahbazkia, 2015 Sean Clifford
 * This program is part of spatter.
 * 
 * If you want to improve the software, this is free software for non-commercial purposes.
 */
package spatter.sequenceprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import spatter.sequenceprocessing.Sequence;
import spatter.sequenceprocessing.SpaProcessor;
import spatter.sequenceprocessing.SpaType;

public final class ProcessSequencesAction {

	public static void actionPerformed(String repeats_file, String types_file, String sequence_directory,
			String output_directory) {
		// TODO use context

		File rfile = new File(types_file);
		File tfile = new File(repeats_file);
		if (rfile == null || tfile == null) {
			System.out.println("Repeats or type files missing!");
			return;
		}

		SpaRepeate repeate = new SpaRepeate(tfile);
		SpaType type = new SpaType(rfile);

		// now process each sequence in the sequence folder
		try {
			File sfile = new File(sequence_directory);

			if (sfile == null || (sfile != null && sfile.list().length == 0)) {
				System.out.println("No sequence files found!");
				return;
			}

			// now load the repeat and types
			if (!repeate.load()) {
				System.out.println("Failed to load repeats file");
				return;
			}

			if (!type.load()) {
				System.out.println("Failed to load type file");
				return;
			}

			int size = sfile.listFiles().length;
			for (int i = 0; i < size; i++) {
				File sequenceFile = sfile.listFiles()[i];
				System.out.println("Processing " + sequenceFile.getAbsolutePath());
				Sequence sequence = new Sequence(sequenceFile);
				if (!sequence.load()) {
					System.out.println("Failed to load sequence file " + sequenceFile.getAbsolutePath());
					continue;
				}

				File output = new File(output_directory);

				sequenceProcessingHelper(repeate, type, sequence, output);

			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public static void sequenceProcessingHelper(SpaRepeate repeate, SpaType type, Sequence sequence, File output) {
		Scanner input = new Scanner(System.in);
		String filename = sequence.getFile().getName();
		int wrote = 0;
		try {
			SpaProcessor processor = new SpaProcessor();
			System.out.println("Writing output to " + output.getAbsolutePath());
			if (output != null) {
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(output, true));
				bufferedWriter.write("File name" + '\t' + "Contig" + '\t' + "Gene" + '\t' + "Start" + '\t' + "End"
						+ '\t' + "Length" + '\t' + "Left read location" + '\t' + "Right read location" + '\n');
				bufferedWriter.write(sequence.getFile().getName() + '\t');
				for (int i = 0; i < sequence.getlength(); i++) {
					if (processor.toType(sequence.getEntry(i), bufferedWriter, repeate, type, filename) > 0) {
						wrote++;
					}
				}
				if (wrote == 0) {
					bufferedWriter.write("\t" + "\t" + "\t" + "No repeat found");
				}
				wrote = 0;
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
			input.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
