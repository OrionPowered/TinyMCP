package com.alexsobiek.tinymcp;

import cuchaz.enigma.ProgressListener;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.mapping.serde.MappingFileNameFormat;
import cuchaz.enigma.translation.mapping.serde.MappingSaveParameters;
import cuchaz.enigma.translation.mapping.serde.tinyv2.TinyV2Writer;
import cuchaz.enigma.translation.mapping.tree.EntryTree;
import cuchaz.enigma.translation.mapping.tree.HashEntryTree;

import java.nio.file.Path;

public class IntermediaryMappings {
    public static TinyV2Writer WRITER = new TinyV2Writer("notch", "intermediary");

    MappingSaveParameters parameters = new MappingSaveParameters(MappingFileNameFormat.BY_DEOBF);
    EntryTree<EntryMapping> intermediaryMappings = new HashEntryTree<>();

    public void write(Path output) {
        WRITER.write(intermediaryMappings, output, ProgressListener.none(), parameters);
    }
}
