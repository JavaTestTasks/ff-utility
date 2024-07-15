package ru.fev;

import java.io.*;

public class Filter {
    private static int numOfIntegers;
    private static int maxInt = Integer.MIN_VALUE;
    private static int minInt = Integer.MAX_VALUE;
    ;
    private static int sumInt = 0;
    private static double avgInt = 0;

    private static int numOfFloats;
    private static float maxFloat = Float.MAX_VALUE;
    private static float minFloat = Float.MIN_VALUE;
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


    public static void getFilteredValues(Arguments arguments) {

        updateOutNames(arguments);
        boolean append = arguments.getOptions().containsKey("a");

        try (BufferedWriter intWriter = new BufferedWriter(new FileWriter(intOutName, append));
             BufferedWriter floatWriter = new BufferedWriter(new FileWriter(floatOutName, append));
             BufferedWriter stringWriter = new BufferedWriter(new FileWriter(stringOutName, append))) {

            for (String path : arguments.getFileNames()) {

                try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                    String line;
                    while ((line = reader.readLine()) != null) {

                        try {
                            int t = Integer.parseInt(line);
                            intWriter.write(t + "\n");
                            updateInts(t);
                        } catch (Exception e) {

                            try {
                                float f = Float.parseFloat(line);
                                floatWriter.write(f + "\n");
                                updateFloats(f);
                            } catch (Exception ex) {
                                stringWriter.write(line + "\n");
                                updateStrings(line);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }

            printStat(arguments);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printFullIntStat(String intName) {
        printShortStat(intName, numOfIntegers);
        System.out.println("Max written value: " + maxInt);
        System.out.println("Min written value: " + minInt);
        System.out.println("Sum of written elements: " + sumInt);
        System.out.println("Average value of written elements: " + avgInt);
    }

    private static void printFullFloatStat(String floatName) {
        printShortStat(floatName, numOfFloats);
        System.out.println("Max written value: " + maxFloat);
        System.out.println("Min written value: " + minFloat);
        System.out.println("Sum of written elements: " + sumFloat);
        System.out.println("Average value of written elements: " + avgFloat);
    }

    private static void printFullStringStat(String stringName) {
        printShortStat(stringName, numOfStrings);
        System.out.println("The longest string's length: " + maxStringLen);
        System.out.println("The shortest string's length: " + minStringLen);
    }

    private static void printShortStat(String name, int numOfElements) {
        System.out.println(name);
        System.out.println("Written " + numOfElements + " elements.");
    }

    private static void updateInts(int i) {
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
    }
}
