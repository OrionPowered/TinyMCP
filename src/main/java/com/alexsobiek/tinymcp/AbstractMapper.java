package com.alexsobiek.tinymcp;

import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.representation.entry.Entry;
import cuchaz.enigma.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class AbstractMapper<T extends Entry<?>> implements Mapper<T> {
    protected final List<Pair<T, EntryMapping>> mappings = new ArrayList<>();
    protected final HashMap<String, T> byDeobfName = new HashMap<>();
    protected final HashMap<String, EntryMapping> byObfName = new HashMap<>();

    @Override
    public T findEntry(String deobfName) {
        return byDeobfName.get(deobfName);
    }

    @Override
    public EntryMapping findMapping(String obfName) {
        return byObfName.get(obfName);
    }

    @Override
    public T find(EntryMapping mapping) {
        return byDeobfName.get(mapping.targetName());
    }

    @Override
    public EntryMapping find(T entry) {
        return byObfName.get(entry.getName());
    }

    @Override
    public void forEach(BiConsumer<T, EntryMapping> consumer) {
        mappings.forEach(p -> consumer.accept(p.a, p.b));
    }
}
