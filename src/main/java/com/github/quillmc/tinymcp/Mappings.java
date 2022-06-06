package com.github.quillmc.tinymcp;

import com.alexsobiek.async.util.Lazy;
import cuchaz.enigma.translation.mapping.serde.tinyv2.TinyV2Writer;
import net.fabricmc.tinyremapper.NonClassCopyMode;
import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;
import net.fabricmc.tinyremapper.TinyUtils;

import java.io.IOException;
import java.nio.file.Path;

public class Mappings {
    enum Type {
        NOTCH,
        MCP
    }

    private static final Lazy<TinyV2Writer> WRITER = new Lazy<>(() -> new TinyV2Writer(Type.NOTCH.name(), Type.MCP.name()));

    public static TinyV2Writer writer() {
        return WRITER.get();
    }

    public static TinyRemapper remapper(Path mappings, Type from, Type to) {
        return TinyRemapper.newRemapper()
                .keepInputData(true)
                .checkPackageAccess(true)
                .fixPackageAccess(true)
                .ignoreConflicts(true)
                .withMappings(TinyUtils.createTinyMappingProvider(mappings, from.name(), to.name()))
                .threads(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public static TinyRemapper notchToMCP(Path mappings) {
        return remapper(mappings, Type.NOTCH, Type.MCP);
    }

    public static TinyRemapper MCPtoNotch(Path mappings) {
        return remapper(mappings, Type.MCP, Type.NOTCH);
    }

    public static void remap(Path jar, TinyRemapper remapper) {
        NonClassCopyMode nonClassCopyMode = NonClassCopyMode.FIX_META_INF;
        try (OutputConsumerPath outputConsumer = new OutputConsumerPath.Builder(Path.of(jar.toString().replace(".jar", "-out.jar"))).build()) {
            outputConsumer.addNonClassFiles(jar, nonClassCopyMode, remapper);
            remapper.readInputs(jar);
            remapper.apply(outputConsumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            remapper.finish();
        }
    }

    public static void remap(Path mappings, Path jar, Type from, Type to) throws IOException {
        remap(jar, remapper(mappings, from, to));
    }
}
