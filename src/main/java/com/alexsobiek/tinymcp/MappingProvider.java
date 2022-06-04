package com.alexsobiek.tinymcp;

import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.representation.entry.ClassEntry;
import cuchaz.enigma.translation.representation.entry.Entry;
import cuchaz.enigma.translation.representation.entry.FieldEntry;
import cuchaz.enigma.translation.representation.entry.MethodEntry;

import java.util.Optional;
import java.util.function.BiConsumer;

public interface MappingProvider {
    Mapper<ClassEntry> getClassMapper();

    Mapper<MethodEntry> getMethodMapper();

    Mapper<FieldEntry> getFieldMapper();

    default ClassEntry findClassEntry(EntryMapping mapping) {
        return getClassMapper().find(mapping);
    }

    default MethodEntry findMethodEntry(EntryMapping mapping) {
        return getMethodMapper().find(mapping);
    }

    default FieldEntry findFieldEntry(EntryMapping mapping) {
        return getFieldMapper().find(mapping);
    }

    default ClassEntry findClassEntry(String deobfName) {
        return getClassMapper().findEntry(deobfName);
    }

    default MethodEntry findMethodEntry(String deobfName) {
        return getMethodMapper().findEntry(deobfName);
    }

    default FieldEntry findFieldEntry(String deobfName) {
        return getFieldMapper().findEntry(deobfName);
    }

    default EntryMapping findEntryMapping(ClassEntry entry) {
        return getClassMapper().find(entry);
    }

    default EntryMapping findEntryMapping(MethodEntry entry) {
        return getMethodMapper().find(entry);
    }

    default EntryMapping findFieldMapping(FieldEntry entry) {
        return getFieldMapper().find(entry);
    }

    default EntryMapping findClassMapping(String obfName) {
        return getClassMapper().findMapping(obfName);
    }

    default EntryMapping findMethodMapping(String obfName) {
        return getMethodMapper().findMapping(obfName);
    }

    default EntryMapping findFieldMapping(String obfName) {
        return getFieldMapper().findMapping(obfName);
    }

    default void forEach(BiConsumer<Entry<?>, EntryMapping> consumer) {
        Optional.ofNullable(getClassMapper()).ifPresent(m -> m.forEach(consumer::accept));
        Optional.ofNullable(getMethodMapper()).ifPresent(m -> m.forEach(consumer::accept));
        Optional.ofNullable(getFieldMapper()).ifPresent(m -> m.forEach(consumer::accept));
    }
}
