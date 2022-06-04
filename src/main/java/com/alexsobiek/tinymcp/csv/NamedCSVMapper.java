package com.alexsobiek.tinymcp.csv;

import com.alexsobiek.tinymcp.Mapper;
import com.alexsobiek.tinymcp.MappingProvider;
import com.alexsobiek.tinymcp.PackageRelocator;
import cuchaz.enigma.translation.representation.entry.ClassEntry;
import cuchaz.enigma.translation.representation.entry.FieldEntry;
import cuchaz.enigma.translation.representation.entry.MethodEntry;

import java.nio.file.Path;

public class NamedCSVMapper implements MappingProvider {
    public static NamedCSVMapper SERVER_BETA1_1__02(MappingProvider intermediary, Path mcpConf) {
        return new NamedCSVMapper(
                new NamedCSVMethodMapper(
                        intermediary,
                        mcpConf.resolve("methods.csv").toFile(),
                        4,
                        3,
                        4,
                        5,
                        6, PackageRelocator.DEFAULT
                ),
                new NamedCSVFieldMapper(
                        intermediary,
                        mcpConf.resolve("fields.csv").toFile(),
                        3,
                        4,
                        6,
                        7,
                        8,
                        PackageRelocator.DEFAULT
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
