package com.github.quillmc.tinymcp;

import com.alexsobiek.async.util.Lazy;
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
import java.util.ArrayList;
import java.util.List;

public class TinyMCP {

    public static TinyMCP SERVER_BETA1_1__02() {
        TinyMCP tmcp = new TinyMCP();
        File rgs = tmcp.artifact("mcp26_server.rgs", "https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/minecraft_server.rgs");
        File methodsCsv = tmcp.artifact("mcp26_methods.csv", "https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/methods.csv");
        File fieldsCsv = tmcp.artifact("mcp26_fields.csv", "https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/fields.csv");
        File serverJar = tmcp.artifact("b1.1_02_server.jar", "http://files.betacraft.uk/server-archive/beta/b1.1_02.jar");
        tmcp.jar = serverJar;

        tmcp.intermediary = new RGS(rgs, serverJar);
        tmcp.named = NamedCSVMapper.SERVER_BETA1_1__02(tmcp.intermediary, methodsCsv, fieldsCsv);
        return tmcp;
    }

    public static TinyMCP CLIENT_BETA1_1__02() {
        TinyMCP tmcp = new TinyMCP();
        File rgs = tmcp.artifact("mcp26_client.rgs", "https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/minecraft.rgs");
        File methodsCsv = tmcp.artifact("mcp26_methods.csv", "https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/methods.csv");
        File fieldsCsv = tmcp.artifact("mcp26_fields.csv", "https://raw.githubusercontent.com/QuillMC/MCPArchive/main/beta/mcp26/conf/fields.csv");
        File clientJar = tmcp.artifact("b1.1_02_client.jar", "https://launcher.mojang.com/v1/objects/e1c682219df45ebda589a557aadadd6ed093c86c/client.jar");

        tmcp.jar = clientJar;
        tmcp.intermediary = new RGS(rgs, clientJar);
        tmcp.named = NamedCSVMapper.CLIENT_BETA1_1__02(tmcp.intermediary, methodsCsv, fieldsCsv);
        return tmcp;
    }

    protected File jar;
    protected MappingProvider intermediary;
    protected MappingProvider named;

    private final List<File> artifacts = new ArrayList<>();
    private final File cacheDir = System.getProperty("TINYMCP_CACHE") != null
            ? Path.of(System.getProperty("TINYMCP_CACHE")).toFile()
            : new File(".tinymcp");
    private final Lazy<Path> mappings = new Lazy<>(this::write);
    private final Lazy<File> mappedJar = new Lazy<>(this::mapJar);

    public TinyMCP() {
        if (!cacheDir.exists()) cacheDir.mkdirs();
    }

    public File file(String fileName) {
        return new File(cacheDir, fileName);
    }

    public File artifact(String name, String downloadUrl) {
        File artifact = file(name);
        if (!artifact.exists()) download(downloadUrl, artifact);
        artifacts.add(artifact);
        return artifact;
    }

    private void download(String downloadUrl, File out) {
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

    public List<File> getArtifacts() {
        return artifacts;
    }

    public File getJar() {
        return jar;
    }

    public File getMappedJar() {
        return mappedJar.get();
    }

    public Path getMappings() {
        return mappings.get();
    }

    private File mapJar() {
        Path out = Path.of(jar.getAbsolutePath().replace(".jar", ".mapped.jar"));
        Mappings.remap(jar.toPath(), out, Mappings.notchToMCP(this.mappings.get()));
        return out.toFile();
    }

    public Path write() {
        Path p = Path.of(jar.getAbsolutePath().replace(".jar", ".tinyv2"));
        write(p);
        return p;
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
