package spatter;

import java.io.File;

import spatter.sequenceprocessing.*;

public class cli {

    /*
     * cli - Handles the command line aspect of spatter
     * 
     * Copyright (C) 2015 Sean Clifford
     * This program is part of DNACLI.
     * 
     * If you want to improve the software, this is a free software for non-commercial purposes.
     */
    public static void help() {
        System.out.println("DNACLI - CLI frontend to/extension of DNAGear");
        System.out.println();
        System.out.println("USAGE: dnacli [OPTION]...[OUTPUT DIR]");
        System.out.println();
        System.out.println("-r repeats_file");
        System.out.println("\t\t\t" + "The repeats file to read");
        System.out.println("-t types_file");
        System.out.println("\t\t\t" + "The types file to read");
        System.out.println("-d sequence_directory");
        System.out.println("\t\t\t" + "The directory containing the sequences"
                + " to read");
        System.out.println("-y --always-complete");
        System.out.println("\t\t\t" + "Ignore errors in sequences");
        System.out.println("-n --never-complete");
        System.out.println("\t\t\t" + "Exit when finding errors in sequences");
        System.out.println();
        System.out.println("EXAMPLES:");
        System.out.println();
        System.out.println("java -jar DNACLI.jar -r repeats -t types -d dir output");
        System.out.println();
        System.out
                .println("Processes the files in \'dir\' using \'repeats\'"
                        + " as the repeats file and \'types\' as the types file, and \'outputs\'"
                        + " the results to output. If it finds errors in the sequences,"
                        + " prompts the user whether to ignore them or exit.");
        System.out.println();
        System.out.println();
        System.out
                .println("java -jar DNACLI.jar --always-complete -r repeats -t types -d dir output");
        System.out.println();
        System.out
                .println("Processes the files in \'dir\' using \'repeats\'"
                        + " as the repeats file and \'types\' as the types file, and \'outputs\'"
                        + " the results to output. Ignores any errors in the sequences.");
        System.out.println();
        System.out.println();
        System.out.println("java -jar DNACLI.jar -n -r repeats -t types -d dir output");
        System.out.println();
        System.out
                .println("Processes the files in \'dir\' using \'repeats\'"
                        + " as the repeats file and \'types\' as the types file, and \'outputs\'"
                        + " the results to output. Exits if it encounters any errors in the sequences.");
        return;
    }

    public static void main(String[] args) {
        /*
         * Error codes
         * 
         * 2) no repeats file 3) no types file 4: no sequence directory 10:
         * unknown argument 
         */
        // Parse arguments
        String repeats_file = "";
        String types_file = "";
        String sequence_directory = "";
        String output_directory = "";

        for (int i = 0; i < args.length - 1; i++) {
            String s = args[i];
            switch (s) {
            case "-r":
                repeats_file = args[i + 1];
                break;
            case "-t":
                types_file = args[i + 1];
                break;
            case "-d":
                sequence_directory = args[i + 1];
                break;
            case "-h":
                help();
                System.exit(0);
                break;
            default:
                break;
            }
            output_directory = args[args.length - 1];
            File output = new File(output_directory);
            if (output.exists()) {
            	output.delete();
            }
            
        }

        if (repeats_file == "") {
            System.out.println("No repeats file defined!");
            help();
            System.exit(2);
        }
        if (types_file == "") {
            System.out.println("No types file defined!");
            help();
            System.exit(3);
        }
        if (sequence_directory == "") {
            System.out.println("No sequence directory defined!");
            help();
            System.exit(4);
        }
        ProcessSequencesAction.actionPerformed(repeats_file, types_file,
                sequence_directory, output_directory);
    }
}
