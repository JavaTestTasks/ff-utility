package ru.fev;

import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.util.*;

public class Arguments {
    private static final Options options;
    private final String[] args;
    private Map<String, String> opts;
    private List<String> files;

    static {
        options = new Options();
        options.addOption("a", false, "add to result");
        options.addOption("f", false, "full statistic");
        options.addOption("o", true, "path to result");
        options.addOption("p", true, "result name prefix");
        options.addOption("s", false, "short statistic");
    }

    public Arguments(String[] args) {
        this.args = args;
        opts = Collections.emptyMap();
        files = Collections.emptyList();
        parseArguments();
    }

    private void parseArguments() {
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            opts = setOptions(cmd);
            files = Arrays.stream(cmd.getArgs()).toList();

            if (files.isEmpty()) {
                System.err.println("Required at least one file");
                printHelp();
                System.exit(1);
            }

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp();
        }
    }

    private Map<String, String> setOptions(CommandLine cmd) {
        Map<String, String> options = new HashMap<>();

        if (cmd.hasOption("a")) {
            options.put("a", "");
        }
        if (cmd.hasOption("s")) {
            options.put("s", "");
        }
        if (cmd.hasOption("f")) {
            options.put("f", "");
        }
        if (cmd.hasOption("p")) {
            options.put("p", cmd.getOptionValue("p"));
        }
        if (cmd.hasOption("o")) {
            options.put("o", cmd.getOptionValue("o"));
        }

        return options;
    }

    public Map<String, String> getOptions() {
        return opts;
    }

    public List<String> getFileNames() {
        return files;
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);

        pw.println("File filter utility");
        formatter.printUsage(pw, 100, "java -jar file-filter-formatter.jar [option]... [files]...");
        formatter.printOptions(pw, 100, options, 2, 5);

        pw.close();
    }
}
