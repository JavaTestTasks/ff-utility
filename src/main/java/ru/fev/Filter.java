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

    private static String intOutName = "integers.txt";
    private static String floatOutName = "floats.txt";
    private static String stringOutName = "strings.txt";

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
                        writeLine(arguments, append, startWritingInt, intOutName, t);
                        startWritingInt = true;
                        updateIntegers(t);
                    } catch (Exception e) {

                        try {
                            float f = Float.parseFloat(line);
                            writeLine(arguments, append, startWritingFloat, floatOutName, f);
                            startWritingFloat = true;
                            updateFloats(f);
                        } catch (Exception ex) {

                            writeLine(arguments, append, startWritingString, stringOutName, line);
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
    }

    private static <T> void printFullNumericStat(String name, int numOfElements, T min, T max, T sum, double avg) {
        printShortStat(name, numOfElements);
        System.out.println("Max written value: " + max);
        System.out.println("Min written value: " + min);
        System.out.println("Sum of written elements: " + sum);
        System.out.println("Average value of written elements: " + avg);
    }

    private static void printFullStringStat(String stringOutName) {
        printShortStat(stringOutName, numOfStrings);
        System.out.println("The longest string's length: " + maxStringLen);
        System.out.println("The shortest string's length: " + minStringLen);
    }

    private static void printShortStat(String name, int numOfElements) {
        System.out.println("\n" + name);
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
                printFullNumericStat(intOutName, numOfIntegers, minInt, maxInt, sumInt, avgInt);
            }
            if (numOfFloats > 0) {
                printFullNumericStat(floatOutName, numOfFloats, minFloat, maxFloat, sumFloat, avgFloat);
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
    }

    private static <T> void writeLine(Arguments arguments,
                                      boolean append,
                                      boolean startWriting,
                                      String outName,
                                      T value) throws IOException {

        if (arguments.getOptions().containsKey("o")) {
            Path dirPath = Path.of(arguments.getOptions().get("o"));
            if (Files.notExists(dirPath)) {
                Files.createDirectory(dirPath);
            }
            String add = "/";
            if (System.getProperty("os.name").contains("Windows")) {
                add = "\\";
            }
            outName = dirPath + add + outName;
        }

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
}
