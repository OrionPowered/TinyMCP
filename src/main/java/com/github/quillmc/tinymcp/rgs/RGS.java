package com.github.quillmc.tinymcp.rgs;

import com.github.quillmc.tinymcp.AbstractMapper;
import com.github.quillmc.tinymcp.MappingProvider;
import com.github.quillmc.tinymcp.PackageRelocater;
import com.github.quillmc.tinymcp.field.FieldCache;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.representation.entry.ClassEntry;
import cuchaz.enigma.translation.representation.entry.FieldEntry;
import cuchaz.enigma.translation.representation.entry.MethodEntry;
import cuchaz.enigma.utils.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class RGS implements MappingProvider {
    private final File srgFile;
    private final ClassMapper classMapper;
    private final MethodMapper methodMapper;
    private final FieldMapper fieldMapper;

    public RGS(File srgFile, File jar) {
        this.srgFile = srgFile;
        this.classMapper = new ClassMapper();
        this.methodMapper = new MethodMapper();
        this.fieldMapper = new FieldMapper(jar);
        if (!srgFile.exists()) throw new RuntimeException(srgFile + " does not exist");
        parse();
    }

    private void parse() {
        try {
            Scanner scanner = new Scanner(new FileInputStream(srgFile));
            while (scanner.hasNextLine()) {
                try {
                    RGSEntry entry = RGSEntry.parse(scanner.nextLine());

                    switch (entry.type) {
                        case CLASS -> classMapper.add((RGSClassMapEntry) entry);
                        case METHOD -> methodMapper.add((RGSMethodMapEntry) entry);
                        case FIELD -> fieldMapper.add((RGSFieldMapEntry) entry);
                    }
                } catch (RuntimeException ignored) {
                    // Runtime exceptions will be thrown if it receives an invalid line, that's okay.
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ClassMapper getClassMapper() {
        return classMapper;
    }

    public MethodMapper getMethodMapper() {
        return methodMapper;
    }

    public FieldMapper getFieldMapper() {
        return fieldMapper;
    }

    public static class ClassMapper extends AbstractMapper<ClassEntry> {
        public void add(RGSClassMapEntry e) {
            ClassEntry ce = new ClassEntry(e.notchClass);
            EntryMapping em = new EntryMapping(PackageRelocater.DEFAULT.relocate(e.notchClass, e.mappedName), "");
            byDeobfName.put(e.mappedName, ce);
            byObfName.put(e.notchClass, em);
            mappings.add(new Pair<>(ce, em));
        }
    }

    public static class MethodMapper extends AbstractMapper<MethodEntry> {
        public void add(RGSMethodMapEntry e) {
            MethodEntry me = MethodEntry.parse(e.notchClass, e.notchName, e.methodSignature);
            EntryMapping em = new EntryMapping(e.mappedName);
            byDeobfName.put(e.mappedName, me);
            byObfName.put(e.notchName, em);
            mappings.add(new Pair<>(me, em));
        }
    }

    public static class FieldMapper extends AbstractMapper<FieldEntry> {
        private FieldCache fieldCache;
        public FieldMapper(File jar) {
            try {
                fieldCache = new FieldCache(jar.toPath());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        public void add(RGSFieldMapEntry e) {
            fieldCache.getField(e.notchClass, e.notchName).ifPresent(f -> {
                FieldEntry fe = FieldEntry.parse(e.notchClass, e.notchName, f.descriptor());
                EntryMapping em = new EntryMapping(e.mappedName);
                byDeobfName.put(e.mappedName, fe);
                byObfName.put(e.notchName, em);
                mappings.add(new Pair<>(fe, em));
            });
        }
    }
}

