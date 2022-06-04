package com.alexsobiek.tinymcp.old.mcp;

import com.alexsobiek.tinymcp.PackageRelocator;
import com.alexsobiek.tinymcp.csv.CSVReader;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.representation.entry.MethodEntry;
import cuchaz.enigma.utils.Pair;

import java.io.File;

public class MethodMappings extends MCPMappingProvider {

    public static MethodMappings MCP26_SERVER(File csv) {
        return new MethodMappings(csv, 4, 1, 2, 5, 6, PackageRelocator.DEFAULT);
    }

    private final int classColumn;
    private final int methodColumn;
    private final int mappedColumn;
    private final int javaDocColumn;
    private final PackageRelocator relocator;

    private MethodMappings(File file, int headerRow, int classColumn, int methodColumn, int mappedColumn, int javaDocColumn, PackageRelocator relocator) {
        super(new CSVReader(file, headerRow));
        this.classColumn = classColumn;
        this.methodColumn = methodColumn;
        this.mappedColumn = mappedColumn;
        this.javaDocColumn = javaDocColumn;
        this.relocator = relocator;
        populate();
    }

    private void populate() {
        reader.forEachRow(entry -> {
            String[] row = entry.row();
            mappings.add(new Pair<>(
                    MethodEntry.parse(relocator.relocate("", row[classColumn]), row[methodColumn], ""),
                    new EntryMapping(row[mappedColumn], row[javaDocColumn])
            ));
        });
    }
}
