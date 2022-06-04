package com.alexsobiek.tinymcp.old;

import com.alexsobiek.tinymcp.old.mcp.MethodMappings;
import com.alexsobiek.tinymcp.rgs.RGS;
import cuchaz.enigma.ProgressListener;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.mapping.serde.MappingFileNameFormat;
import cuchaz.enigma.translation.mapping.serde.MappingSaveParameters;
import cuchaz.enigma.translation.mapping.serde.tinyv2.TinyV2Writer;
import cuchaz.enigma.translation.mapping.tree.EntryTree;
import cuchaz.enigma.translation.mapping.tree.HashEntryTree;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {

        EntryTree<EntryMapping> intermediaryMappings = new HashEntryTree<>();
        EntryTree<EntryMapping> namedMappings = new HashEntryTree<>();

        RGS rgs = new RGS(new File("mcp/conf/minecraft_server.rgs"));
        //rgs.apply(intermediaryMappings);

        MethodMappings methodMappings = MethodMappings.MCP26_SERVER(new File("mcp/conf/methods.csv"));
        methodMappings.apply(namedMappings);

        //ClassMappings classMappings = ClassMappings.SERVER_BETA1_1__02(new File("mcp/conf/classes.csv"));
        //classMappings.apply(mappings);

        File out = new File("mappings.tiny");
        if (out.exists()) out.delete();

        MappingSaveParameters parameters = new MappingSaveParameters(MappingFileNameFormat.BY_DEOBF);

        new TinyV2Writer("notch", "intermediary");

        new TinyV2Writer("notch", "intermediary").write(intermediaryMappings, out.toPath(), ProgressListener.none(), parameters);
        new TinyV2Writer("intermediary", "named").write(namedMappings, out.toPath(), ProgressListener.none(), parameters);


    }
}
