package com.alexsobiek.tinymcp;

import com.alexsobiek.tinymcp.csv.NamedCSVMapper;
import com.alexsobiek.tinymcp.rgs.RGS;
import cuchaz.enigma.ProgressListener;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.mapping.serde.MappingFileNameFormat;
import cuchaz.enigma.translation.mapping.serde.MappingFormat;
import cuchaz.enigma.translation.mapping.serde.MappingSaveParameters;
import cuchaz.enigma.translation.mapping.tree.EntryTree;
import cuchaz.enigma.translation.mapping.tree.HashEntryTree;
import cuchaz.enigma.translation.representation.entry.FieldEntry;
import cuchaz.enigma.translation.representation.entry.MethodEntry;

import java.nio.file.Path;

public class TinyMCP {

    public static void main(String[] args) {
        TinyMCP tmcp = SERVER_BETA1_1__02(Path.of("mcp").resolve("conf"));

        tmcp.getIntermediary().forEach((e, m) -> {
            //System.out.printf("Found intermediary mapping %s -> %s%n", e, m.targetName());
        });

        tmcp.getNamed().forEach((e, m) -> {
            //System.out.printf("Found named mapping %s -> %s%n", e, m.targetName());
        });

        tmcp.write(Path.of("mappings.tiny"));
    }

    protected MappingProvider intermediary;
    protected MappingProvider named;

    public static TinyMCP SERVER_BETA1_1__02(Path mcpConf) {
        TinyMCP tmcp = new TinyMCP();
        tmcp.intermediary = new RGS(mcpConf.resolve("minecraft_server.rgs").toFile());
        tmcp.named = NamedCSVMapper.SERVER_BETA1_1__02(tmcp.intermediary, mcpConf);
        return tmcp;
    }

    protected TinyMCP() {

    }

    public MappingProvider getIntermediary() {
        return intermediary;
    }

    public MappingProvider getNamed() {
        return named;
    }

    public void write(Path out) {
        MappingSaveParameters parameters = new MappingSaveParameters(MappingFileNameFormat.BY_DEOBF);
        ProgressListener progressListener = ProgressListener.none();
        EntryTree<EntryMapping> mappings = new HashEntryTree<>();

        intermediary.forEach((e, m) -> {
            if (e instanceof MethodEntry) {
                EntryMapping namedMapping = named.findMethodMapping(m.targetName());
                if (namedMapping != null) m = namedMapping;
            } else if (e instanceof FieldEntry) {
                EntryMapping namedMapping = named.findFieldMapping(m.targetName());
                if (namedMapping != null) m = namedMapping;
            }

            mappings.insert(e, m);
        });

        MappingFormat.TINY_V2.write(mappings, out, progressListener, parameters);
    }

}
