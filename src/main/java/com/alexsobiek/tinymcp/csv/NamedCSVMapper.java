package com.alexsobiek.tinymcp.csv;

import com.alexsobiek.tinymcp.Mapper;
import com.alexsobiek.tinymcp.MappingProvider;
import com.alexsobiek.tinymcp.PackageRelocater;
import cuchaz.enigma.translation.representation.entry.ClassEntry;
import cuchaz.enigma.translation.representation.entry.FieldEntry;
import cuchaz.enigma.translation.representation.entry.MethodEntry;

import java.io.File;
import java.nio.file.Path;

public class NamedCSVMapper implements MappingProvider {
    public static NamedCSVMapper SERVER_BETA1_1__02(MappingProvider intermediary, File methodsCsv, File fieldsCsv) {
        return new NamedCSVMapper(
                new NamedCSVMethodMapper(
                        intermediary,
                        methodsCsv,
                        4,
                        3,
                        4,
                        5,
                        6,
                        PackageRelocater.DEFAULT
                ),
                new NamedCSVFieldMapper(
                        intermediary,
                        fieldsCsv,
                        3,
                        4,
                        6,
                        7,
                        8,
                        PackageRelocater.DEFAULT
                )
        );
    }

    public static NamedCSVMapper CLIENT_BETA1_1__02(MappingProvider intermediary, File methodsCsv, File fieldsCsv) {
        return new NamedCSVMapper(
                new NamedCSVMethodMapper(
                        intermediary,
                        methodsCsv,
                        4,
                        1,
                        2,
                        5,
                        6,
                        PackageRelocater.DEFAULT
                ),
                new NamedCSVFieldMapper(
                        intermediary,
                        fieldsCsv,
                        3,
                        1,
                        3,
                        7,
                        8,
                        PackageRelocater.DEFAULT
                )
        );
    }

    private final Mapper<MethodEntry> methodMapper;
    private final Mapper<FieldEntry> fieldMapper;

    protected NamedCSVMapper(Mapper<MethodEntry> methodMapper, Mapper<FieldEntry> fieldMapper) {
        this.methodMapper = methodMapper;
        this.fieldMapper = fieldMapper;
    }


    @Override
    public Mapper<ClassEntry> getClassMapper() {
        return null;
    }

    @Override
    public Mapper<MethodEntry> getMethodMapper() {
        return methodMapper;
    }

    @Override
    public Mapper<FieldEntry> getFieldMapper() {
        return fieldMapper;
    }
}
