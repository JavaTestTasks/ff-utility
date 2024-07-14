package ru.fev;

import java.io.BufferedWriter;

public class Filter {
    private int numOfIntegers;
    private int maxInt;
    private int minInt;
    private int sumInt;
    private int avgInt;

    private int numOfFloats;
    private int maxFloat;
    private int minFloat;
    private int sumFloat;
    private int avgFloat;

    private int numOfStrings;
    private int maxStringLen;
    private int minStringLen;



    public static void getFilteredValues(Arguments arguments) {
//        System.out.println("Options:");
//        var opts = arguments.getOptions();
//        opts.forEach(System.out::println);
//
//        System.out.println("Files:");
//        var files = arguments.getFileNames();
//        files.forEach(System.out::println);



        for (String path : arguments.getFileNames()) {

        }
    }
}
