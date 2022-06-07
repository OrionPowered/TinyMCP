package com.github.quillmc.tinymcp;

import com.github.quillmc.tinymcp.csv.NamedCSVMapper;
import com.github.quillmc.tinymcp.rgs.RGS;
import cuchaz.enigma.ProgressListener;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.mapping.serde.MappingFileNameFormat;
import cuchaz.enigma.translation.mapping.serde.MappingSaveParameters;
import cuchaz.enigma.translation.mapping.tree.EntryTree;
import cuchaz.enigma.translation.mapping.tree.HashEntryTree;
import cuchaz.enigma.translation.representation.entry.FieldEntry;
import cuchaz.enigma.translation.representation.entry.MethodEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

public class TinyMCP {
    protected static final File cacheDir = System.getProperty("TINYMCP_CACHE") != null
            ? Path.of(System.getProperty("TINYMCP_CACHE")).toFile()
            : new File(".tinymcp");

    static {
        if (!cacheDir.exists()) cacheDir.mkdirs();
    }

    protected MappingProvider intermediary;
    protected MappingProvider named;

    public static TinyMCP SERVER_BETA1_1__02() {
        TinyMCP tmcp = new TinyMCP();
        File rgs = new File(cacheDir, "mcp26_server.rgs");
        File methodsCsv = new File(cacheDir, "mcp26_methods.csv");
        File fieldsCsv = new File(cacheDir, "mcp26_fields.csv");
        File serverJar = new File(cacheDir, "b1.1_02_server.jar");

        if (!rgs.exists())
            download("https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/minecraft_server.rgs", rgs);
        if (!methodsCsv.exists())
            download("https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/methods.csv", methodsCsv);
        if (!fieldsCsv.exists())
            download("https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/fields.csv", fieldsCsv);
        if (!serverJar.exists()) download("http://files.betacraft.uk/server-archive/beta/b1.1_02.jar", serverJar);

        tmcp.intermediary = new RGS(rgs, serverJar);
        tmcp.named = NamedCSVMapper.SERVER_BETA1_1__02(tmcp.intermediary, methodsCsv, fieldsCsv);
        return tmcp;
    }

    public static TinyMCP CLIENT_BETA1_1__02() {
        TinyMCP tmcp = new TinyMCP();
        File rgs = new File(cacheDir, "mcp26_client.rgs");
        File methodsCsv = new File(cacheDir, "mcp26_methods.csv");
        File fieldsCsv = new File(cacheDir, "mcp26_fields.csv");
        File clientJar = new File(cacheDir, "b1.1_02_client.jar");

        if (!rgs.exists())
            download("https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/minecraft.rgs", rgs);
        if (!methodsCsv.exists())
            download("https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/methods.csv", methodsCsv);
        if (!fieldsCsv.exists())
            download("https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/fields.csv", fieldsCsv);
        if (!clientJar.exists())
            download("https://launcher.mojang.com/v1/objects/e1c682219df45ebda589a557aadadd6ed093c86c/client.jar", clientJar);

        tmcp.intermediary = new RGS(rgs, clientJar);
        tmcp.named = NamedCSVMapper.CLIENT_BETA1_1__02(tmcp.intermediary, methodsCsv, fieldsCsv);
        return tmcp;
    }

    private static void download(String downloadUrl, File out) {
        try {
            URL url = new URL(downloadUrl);
            ReadableByteChannel channel = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(out);
            fos.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
            fos.close();
        } catch (Throwable t) {
            throw new RuntimeException("Failed downloading " + downloadUrl, t);
        }
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

        Mappings.writer().write(mappings, out, progressListener, parameters);
    }

}
