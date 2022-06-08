package com.github.quillmc.tinymcp.csv;

import com.github.quillmc.tinymcp.Mapper;
import com.github.quillmc.tinymcp.MappingProvider;
import com.github.quillmc.tinymcp.PackageRelocation;
import cuchaz.enigma.translation.representation.entry.ClassEntry;
import cuchaz.enigma.translation.representation.entry.FieldEntry;
import cuchaz.enigma.translation.representation.entry.MethodEntry;

import java.io.File;

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
                        PackageRelocation.DEFAULT
                ),
                new NamedCSVFieldMapper(
                        intermediary,
                        fieldsCsv,
                        3,
                        4,
                        6,
                        7,
                        8,
                        PackageRelocation.DEFAULT
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
                        PackageRelocation.DEFAULT
                ),
                new NamedCSVFieldMapper(
                        intermediary,
                        fieldsCsv,
                        3,
                        1,
                        3,
                        7,
                        8,
                        PackageRelocation.DEFAULT
                )
        );
    }

    public static NamedCSVMapper SERVER_BETA1_2__01(MappingProvider intermediary, File methodsCsv, File fieldsCsv) {
        return new NamedCSVMapper(
                new NamedCSVMethodMapper(
                        intermediary,
                        methodsCsv,
                        4,
                        3,
                        4,
                        5,
                        6,
                        PackageRelocation.DEFAULT
                ),
                new NamedCSVFieldMapper(
                        intermediary,
                        fieldsCsv,
                        3,
                        5,
                        6,
                        7,
                        8,
                        PackageRelocation.DEFAULT
                )
        );
    }

    public static NamedCSVMapper CLIENT_BETA1_2__02(MappingProvider intermediary, File methodsCsv, File fieldsCsv) {
        return new NamedCSVMapper(
                new NamedCSVMethodMapper(
                        intermediary,
                        methodsCsv,
                        4,
                        1,
                        2,
                        5,
                        6,
                        PackageRelocation.DEFAULT
                ),
                new NamedCSVFieldMapper(
                        intermediary,
                        fieldsCsv,
                        3,
                        1,
                        3,
                        7,
                        8,
                        PackageRelocation.DEFAULT
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
