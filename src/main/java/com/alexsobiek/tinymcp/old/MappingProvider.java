package com.alexsobiek.tinymcp.old;

import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.mapping.tree.EntryTree;
import cuchaz.enigma.translation.representation.entry.Entry;
import cuchaz.enigma.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class MappingProvider {
    protected List<Pair<Entry<?>, EntryMapping>> mappings = new ArrayList<>();

    public void apply(EntryTree<EntryMapping> mappings) {
        this.mappings.forEach(e -> mappings.insert(e.a, e.b));
    }
}
