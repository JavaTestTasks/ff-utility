package ru.fev;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Filter {
    private static int numOfIntegers;
    private static long maxInt = Long.MIN_VALUE;
    private static long minInt = Long.MAX_VALUE;
    private static long sumInt = 0;
    private static double avgInt = 0;

    private static int numOfFloats;
    private static float maxFloat = Float.MIN_VALUE;
    private static float minFloat = Float.MAX_VALUE;
    private static float sumFloat = 0;
    private static double avgFloat = 0;

    private static int numOfStrings;
    private static int maxStringLen = 0;
    private static int minStringLen = 0;

    private static final String INT_OUT = "integers.txt";
    private static final String FLOAT_OUT = "floats.txt";
    private static final String STRING_OUT = "strings.txt";
    private static String intOutName = INT_OUT;
    private static String floatOutName = FLOAT_OUT;
    private static String stringOutName = STRING_OUT;

    private static boolean startWritingInt = false;
    private static boolean startWritingFloat = false;
    private static boolean startWritingString = false;

    public static void getFilteredValues(Arguments arguments) {

        updateOutNames(arguments);
        boolean append = arguments.getOptions().containsKey("a");

        for (String path : arguments.getFileNames()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {

                    try {
                        long t = Long.parseLong(line);
                        writeLine(append, startWritingInt, intOutName, t);
                        startWritingInt = true;
                        updateIntegers(t);
                    } catch (Exception e) {

                        try {
                            float f = Float.parseFloat(line);
                            writeLine(append, startWritingFloat, floatOutName, f);
                            startWritingFloat = true;
                            updateFloats(f);
                        } catch (Exception ex) {

                            writeLine(append, startWritingString, stringOutName, line);
                            startWritingString = true;
                            updateStrings(line);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        printStat(arguments);
        if (arguments.getOptions().containsKey("o")) {
            moveFiles(arguments);
        }
    }

    private static void printFullIntStat(String intOutName) {
        printShortStat(intOutName, numOfIntegers);
        System.out.println("Max written value: " + maxInt);
        System.out.println("Min written value: " + minInt);
        System.out.println("Sum of written elements: " + sumInt);
        System.out.println("Average value of written elements: " + avgInt);
    }

    private static void printFullFloatStat(String floatOutName) {
        printShortStat(floatOutName, numOfFloats);
        System.out.println("Max written value: " + maxFloat);
        System.out.println("Min written value: " + minFloat);
        System.out.println("Sum of written elements: " + sumFloat);
        System.out.println("Average value of written elements: " + avgFloat);
    }

    private static void printFullStringStat(String stringOutName) {
        printShortStat(stringOutName, numOfStrings);
        System.out.println("The longest string's length: " + maxStringLen);
        System.out.println("The shortest string's length: " + minStringLen);
    }

    private static void printShortStat(String name, int numOfElements) {
        System.out.println(name);
        System.out.println("Written " + numOfElements + " elements.");
    }

    private static void updateIntegers(long i) {
        ++numOfIntegers;
        minInt = minInt == Integer.MAX_VALUE ? i : Math.min(minInt, i);
        maxInt = maxInt == Integer.MIN_VALUE ? i : Math.max(maxInt, i);
        sumInt += i;
        avgInt = (double) sumInt / numOfIntegers;
    }

    private static void updateFloats(float f) {
        ++numOfFloats;
        minFloat = minFloat == Float.MAX_VALUE ? f : Math.min(f, minFloat);
        maxFloat = maxFloat == Float.MIN_VALUE ? f : Math.max(f, maxFloat);
        sumFloat += f;
        avgFloat = sumFloat / numOfFloats;
    }

    private static void updateStrings(String s) {
        ++numOfStrings;
        maxStringLen = maxStringLen == 0 ? s.length() : Math.max(maxStringLen, s.length());
        minStringLen = minStringLen == 0 ? s.length() : Math.min(minStringLen, s.length());
    }

    private static void printStat(Arguments arguments) {
        if (arguments.getOptions().containsKey("s")) {
            if (numOfIntegers > 0) {
                printShortStat(intOutName, numOfIntegers);
            }
            if (numOfFloats > 0) {
                printShortStat(floatOutName, numOfFloats);
            }
            if (numOfStrings > 0) {
                printShortStat(stringOutName, numOfStrings);
            }
        } else if (arguments.getOptions().containsKey("f")) {
            if (numOfIntegers > 0) {
                printFullIntStat(intOutName);
            }
            if (numOfFloats > 0) {
                printFullFloatStat(floatOutName);
            }
            if (numOfStrings > 0) {
                printFullStringStat(stringOutName);
            }
        }
    }

    private static void updateOutNames(Arguments arguments) {
        if (arguments.getOptions().containsKey("p")) {
            String prefix = arguments.getOptions().get("p");
            intOutName = prefix + intOutName;
            floatOutName = prefix + floatOutName;
            stringOutName = prefix + stringOutName;
        }
//        if (arguments.getOptions().containsKey("o")) {
//            String path = arguments.getOptions().get("o");
//            path += path.charAt(path.length() - 1) == '/' ? "" : '/';
//            intOutName = path + intOutName;
//            floatOutName = path + floatOutName;
//            stringOutName = path + stringOutName;
//        }
    }

    private static <T> void writeLine(boolean append,
                                      boolean startWriting,
                                      String outName,
                                      T value) throws IOException {
        Path outPath = Paths.get(outName);
        append = append ? append : startWriting;
        FileWriter writer;
        if (Files.exists(outPath)) {
            writer = new FileWriter(outName, append);
            writer.write(value + "\n");
        } else {
            writer = new FileWriter(outName);
            writer.write(value + "\n");
        }
        writer.close();
    }

    private static void moveFiles(Arguments arguments) {
        String add = "/";
        if (System.getProperty("os.name").contains("Windows")) {
            add = "\\";
        }

        Path outputDir = Paths.get(arguments.getOptions().get("o"));

        try {
            if (Files.notExists(outputDir)) {
                Files.createDirectory(outputDir);
            }
            String toCopy = Paths.get(intOutName).toString();
            if (Files.exists(Paths.get(intOutName))) {
                Files.move(Paths.get(intOutName), Paths.get(outputDir + add + toCopy));
            }
            toCopy = Paths.get(floatOutName).toString();
            if (Files.exists(Paths.get(floatOutName))) {
                Files.move(Paths.get(floatOutName), Paths.get(outputDir + add + toCopy));
            }
            toCopy = Paths.get(stringOutName).toString();
            if (Files.exists(Paths.get(stringOutName))) {
                Files.move(Paths.get(stringOutName), Paths.get(outputDir + add + toCopy));
            }
        } catch (IOException e) {
            System.err.println("Can not copy result files: " + e.getMessage());
        }
    }
}
