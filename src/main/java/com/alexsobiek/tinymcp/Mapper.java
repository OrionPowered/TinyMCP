package com.alexsobiek.tinymcp;

import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.mapping.tree.EntryTree;
import cuchaz.enigma.translation.representation.entry.Entry;

import java.util.function.BiConsumer;

public interface Mapper<T extends Entry<?>> {

    T findEntry(String deobfName);

    EntryMapping findMapping(String obfName);

    T find(EntryMapping mapping);

    EntryMapping find(T entry);

    void forEach(BiConsumer<T, EntryMapping> consumer);

    default void apply(EntryTree<EntryMapping> mappings) {
        forEach(mappings::insert);
    }

}
