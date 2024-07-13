package ru.fev;

import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Arguments {
    private static final Options options;
    private final String[] args;
    private final List<Option> opts;
    private final List<String> files;

    static {
        options = new Options();
        options.addOption("a", "add", false, "add to result");
        options.addOption("f", "full", false, "full statistic");
        options.addOption("o", "path", true, "path to result");
        options.addOption("p", "prefix", true, "result name prefix");
        options.addOption("s", "short", false, "short statistic");
    }

    public Arguments(String[] args) {
        this.args = args;
        opts = parseArguments();
    }

    private List<Option> parseArguments() {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.getArgList().isEmpty()) {
                System.err.println("Required at least one file");
                printHelp();
                System.exit(1);
            }
            return Arrays.stream(cmd.getOptions()).toList();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }

        return Collections.emptyList();
    }

    public List<Option> getOptions() {
        return opts;
    }

    public List<String> getArgs() {
        return Arrays.stream(args).toList();
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
