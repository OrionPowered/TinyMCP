package com.alexsobiek.tinymcp.csv;

import com.alexsobiek.tinymcp.AbstractMapper;
import com.alexsobiek.tinymcp.Mapper;
import com.alexsobiek.tinymcp.MappingProvider;
import com.alexsobiek.tinymcp.PackageRelocator;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.representation.entry.MethodEntry;
import cuchaz.enigma.utils.Pair;

import java.io.File;
import java.util.Optional;
import java.util.function.BiConsumer;

public class NamedCSVMethodMapper extends AbstractMapper<MethodEntry> {
    private final MappingProvider intermediary;
    private final CSVReader reader;
    private final int classColumn;
    private final int methodColumn;
    private final int mappedColumn;
    private final int javaDocColumn;
    private final PackageRelocator relocator;

    protected NamedCSVMethodMapper(MappingProvider intermediary, File file, int headerRow, int classColumn, int methodColumn, int mappedColumn, int javaDocColumn, PackageRelocator relocator) {
        this.intermediary = intermediary;
        reader = new CSVReader(file, headerRow);
        this.classColumn = classColumn-1;
        this.methodColumn = methodColumn-1;
        this.mappedColumn = mappedColumn-1;
        this.javaDocColumn = javaDocColumn-1;
        this.relocator = relocator;
        populate();
    }

    public void populate() {
        reader.forEachRow(entry -> {
            String[] row = entry.row();

            if (!row[methodColumn].equals("*")) {
                MethodEntry me = intermediary.findMethodEntry(row[methodColumn]);
                if (me != null) {
                    String sig = intermediary.findMethodEntry(row[methodColumn]).getDesc().toString();
                    me = MethodEntry.parse(relocator.relocate("", row[classColumn]), row[methodColumn], sig);
                    EntryMapping em = new EntryMapping(row[mappedColumn], row[javaDocColumn]);

                    byDeobfName.put(row[mappedColumn], me);
                    byObfName.put(row[methodColumn], em);
                    mappings.add(new Pair<>(me, em));
                    // System.out.println(row[methodColumn]);
                } else System.err.printf("Found mapping %s -> %s but no matching method descriptor!%n", row[methodColumn], row[mappedColumn]);
            }
        });
    }
}
