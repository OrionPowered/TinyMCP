package com.github.quillmc.tinymcp.csv;

import com.github.quillmc.tinymcp.AbstractMapper;
import com.github.quillmc.tinymcp.MappingProvider;
import com.github.quillmc.tinymcp.PackageRelocater;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.representation.entry.FieldEntry;
import cuchaz.enigma.utils.Pair;

import java.io.File;

public class NamedCSVFieldMapper extends AbstractMapper<FieldEntry> {
    private final MappingProvider intermediary;
    private final CSVReader reader;
    private final int classColumn;
    private final int fieldColumn;
    private final int mappedColumn;
    private final int javaDocColumn;
    private final PackageRelocater relocater;

    protected NamedCSVFieldMapper(MappingProvider intermediary, File file, int headerRow, int classColumn, int fieldColumn, int mappedColumn, int javaDocColumn, PackageRelocater relocater) {
        this.intermediary = intermediary;
        reader = new CSVReader(file, headerRow);
        this.classColumn = classColumn - 1;
        this.fieldColumn = fieldColumn - 1;
        this.mappedColumn = mappedColumn - 1;
        this.javaDocColumn = javaDocColumn - 1;
        this.relocater = relocater;
        populate();
    }

    public void populate() {
        reader.forEachRow(entry -> {
            String[] row = entry.row();
            if (!row[fieldColumn].equals("*")) {
                FieldEntry fe = intermediary.findFieldEntry(row[fieldColumn]);
                if (fe != null) {
                    fe = FieldEntry.parse(relocater.relocate("", row[classColumn]), row[fieldColumn], fe.getDesc().toString());
                    EntryMapping em = new EntryMapping(row[mappedColumn], row[javaDocColumn].equals("*") ? "" : row[javaDocColumn]);
                    byDeobfName.put(row[mappedColumn], fe);
                    byObfName.put(row[fieldColumn], em);
                    mappings.add(new Pair<>(fe, em));
                } else System.err.printf("Found mapping %s -> %s but no matching field descriptor!%n", row[fieldColumn], row[mappedColumn]);
            }
        });
    }
}
