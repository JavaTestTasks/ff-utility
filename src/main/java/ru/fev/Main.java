package ru.fev;

public class Main {
    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);
        Filter.getFilteredValues(arguments);
    }
}