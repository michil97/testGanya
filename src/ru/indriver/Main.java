package ru.indriver;

import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {

        Options options = new Options();

        Option input = new Option("f", "filepath", true, "input CSV file path");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("c", "cityId", true, "city id");
        output.setRequired(true);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String filepath = cmd.getOptionValue("filepath");
        String cityIdString = cmd.getOptionValue("cityId");

        int cityId = -1;
        try {
            cityId = Integer.parseInt(cityIdString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        new Sorter(filepath, cityId);

    }

}
