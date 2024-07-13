package ru.fev;

public class Filter {
    public static void getFilteredValues(Arguments arguments) {
        System.out.println("Options:");
        var opts = arguments.getOptions();
        opts.forEach(System.out::println);

        System.out.println("Files:");
        var files = arguments.getArgs();
        files.forEach(System.out::println);
    }
}
