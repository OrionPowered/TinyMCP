package com.alexsobiek.tinymcp.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class CSVReader {
    private final File csvFile;
    private final int headerRow;
    private String[] header;

    public CSVReader(File csvFile, int headerRow) {
        this.csvFile = csvFile;
        this.headerRow = headerRow;
        if (!csvFile.exists()) throw new RuntimeException(csvFile + " does not exist");
    }

    public void forEachRow(Function<String[], String[]> headerModifier, Consumer<CSVEntry> rows) {
        try {
            Scanner scanner = new Scanner(new FileInputStream(csvFile));
            for (int i = 1; i < headerRow; i++) scanner.nextLine(); // advance to header row

            header = headerModifier.apply(scanner.nextLine().split(","));

            while (scanner.hasNextLine()) rows.accept(new CSVEntry(header, scanner.nextLine().split(",")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void forEachRow(Consumer<CSVEntry> rows) {
        forEachRow(e -> e, rows);
    }
}
