package com.alexsobiek.tinymcp.csv;

import com.alexsobiek.tinymcp.AbstractMapper;
import com.alexsobiek.tinymcp.MappingProvider;
import com.alexsobiek.tinymcp.PackageRelocator;
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
    private final PackageRelocator relocator;

    protected NamedCSVFieldMapper(MappingProvider intermediary, File file, int headerRow, int classColumn, int fieldColumn, int mappedColumn, int javaDocColumn, PackageRelocator relocator) {
        this.intermediary = intermediary;
        reader = new CSVReader(file, headerRow);
        this.classColumn = classColumn - 1;
        this.fieldColumn = fieldColumn - 1;
        this.mappedColumn = mappedColumn - 1;
        this.javaDocColumn = javaDocColumn - 1;
        this.relocator = relocator;
        populate();
    }

    public void populate() {
        reader.forEachRow(entry -> {
            String[] row = entry.row();

            if (!row[fieldColumn].equals("*")) {

                FieldEntry fe = intermediary.findFieldEntry(row[fieldColumn]);
                if (fe != null) {
                    fe = FieldEntry.parse(relocator.relocate("", row[classColumn]), row[fieldColumn], "");
                    EntryMapping em = new EntryMapping(row[mappedColumn], row[javaDocColumn]);
                    byDeobfName.put(row[mappedColumn], fe);
                    byObfName.put(row[fieldColumn], em);
                    mappings.add(new Pair<>(fe, em));
                    System.out.println(fe);

                /*MethodEntry me = intermediary.findMethodEntry(row[fieldColumn]);
                if (me != null) {
                    String sig = intermediary.findMethodEntry(row[fieldColumn]).getDesc().toString();
                    me = MethodEntry.parse(relocator.relocate("", row[classColumn]), row[fieldColumn], sig);
                    EntryMapping em = new EntryMapping(row[mappedColumn], row[javaDocColumn]);

                    byDeobfName.put(row[mappedColumn], me);
                    byObfName.put(row[fieldColumn], em);
                    mappings.add(new Pair<>(me, em));
                    // System.out.println(row[methodColumn]);

                 */
                } // else System.err.printf("Found mapping %s -> %s but no matching method descriptor!%n", row[fieldColumn], row[mappedColumn]);
            }
        });
    }
}
