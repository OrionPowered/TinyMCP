package com.github.quillmc.tinymcp.csv;

import com.github.quillmc.tinymcp.AbstractMapper;
import com.github.quillmc.tinymcp.MappingProvider;
import com.github.quillmc.tinymcp.PackageRelocater;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.representation.entry.MethodEntry;
import cuchaz.enigma.utils.Pair;

import java.io.File;

public class NamedCSVMethodMapper extends AbstractMapper<MethodEntry> {
    private final MappingProvider intermediary;
    private final CSVReader reader;
    private final int classColumn;
    private final int methodColumn;
    private final int mappedColumn;
    private final int javaDocColumn;
    private final PackageRelocater relocater;

    protected NamedCSVMethodMapper(MappingProvider intermediary, File file, int headerRow, int classColumn, int methodColumn, int mappedColumn, int javaDocColumn, PackageRelocater relocater) {
        this.intermediary = intermediary;
        reader = new CSVReader(file, headerRow);
        this.classColumn = classColumn-1;
        this.methodColumn = methodColumn-1;
        this.mappedColumn = mappedColumn-1;
        this.javaDocColumn = javaDocColumn-1;
        this.relocater = relocater;
        populate();
    }

    public void populate() {
        reader.forEachRow(entry -> {
            String[] row = entry.row();
            if (!row[methodColumn].equals("*")) {
                MethodEntry me = intermediary.findMethodEntry(row[methodColumn]);
                if (me != null) {
                    String sig = intermediary.findMethodEntry(row[methodColumn]).getDesc().toString();
                    me = MethodEntry.parse(relocater.relocate("", row[classColumn]), row[methodColumn], sig);
                    EntryMapping em = new EntryMapping(row[mappedColumn], row[javaDocColumn].equals("*") ? "" : row[javaDocColumn]);

                    byDeobfName.put(row[mappedColumn], me);
                    byObfName.put(row[methodColumn], em);
                    mappings.add(new Pair<>(me, em));
                } else System.err.printf("Found mapping %s -> %s but no matching method descriptor!%n", row[methodColumn], row[mappedColumn]);
            }
        });
    }
}
