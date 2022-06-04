package com.alexsobiek.tinymcp.csv;

import com.alexsobiek.tinymcp.Mapper;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.representation.entry.MethodEntry;

import java.util.function.BiConsumer;

public class NamedCSVMethodMapper implements Mapper<MethodEntry> {



    @Override
    public MethodEntry findEntry(String deobfName) {
        return null;
    }

    @Override
    public EntryMapping findMapping(String obfName) {
        return null;
    }

    @Override
    public MethodEntry find(EntryMapping mapping) {
        return null;
    }

    @Override
    public EntryMapping find(MethodEntry entry) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<MethodEntry, EntryMapping> consumer) {

    }
}
