package com.alexsobiek.tinymcp.old.mcp;

import com.alexsobiek.tinymcp.PackageRelocator;
import com.alexsobiek.tinymcp.csv.CSVReader;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.representation.entry.ClassEntry;
import cuchaz.enigma.utils.Pair;

import java.io.File;

public class ClassMappings extends MCPMappingProvider {

    public static ClassMappings MCP26_SERVER(File csv) {
        return new ClassMappings(csv, 4, 1, 4, 6, PackageRelocator.DEFAULT);
    }

    private final int nameColumn;
    private final int obfuscatedColumn;
    private final int commentColumn;
    private final PackageRelocator relocator;
    private ClassMappings(File file, int headerRow, int nameColumn, int obfuscatedColumn, int commentColumn, PackageRelocator relocator) {
        super(new CSVReader(file, headerRow));
        this.nameColumn = nameColumn-1;
        this.obfuscatedColumn = obfuscatedColumn-1;
        this.commentColumn = commentColumn-1;
        this.relocator = relocator;
        populate();
    }

    private void populate() {
        reader.forEachRow(entry -> {
            String[] row = entry.row();
            mappings.add(new Pair<>(
                    new ClassEntry(row[obfuscatedColumn]),
                    new EntryMapping(relocator.relocate(row[obfuscatedColumn], row[nameColumn]), row[commentColumn].equals("*") ? "" : row[commentColumn])
            ));
        });
    }
}
