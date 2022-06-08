package com.github.quillmc.tinymcp;

import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CLI {
    public static void main(String[] args) {
        new CLI(args);
    }

    private final CommandLineParser parser;
    private final Options options;
    private final List<String> args;
    private CommandLine cmd;

    public CLI(String[] args) {
        options = new Options();
        parser = new DefaultParser();
        this.args = Arrays.stream(args).collect(Collectors.toList());
        try {
            if (this.args.contains("-c") || this.args.contains("-create")) createMappings();
            else if (this.args.contains("-a") || this.args.contains("-apply")) applyMappings();
            else {
                System.err.println("Unsupported action! Valid actions:");
                System.err.println("* -c, -create       Creates mappings");
                System.err.println("* -a, -apply        Apply mappings to JAR");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validType(String type) {
        return type.equalsIgnoreCase("client") || type.equalsIgnoreCase("server");
    }

    private void unsupportedType() {
        System.err.println("Unsupported type! Supported types: client, server");
        System.exit(1);
    }

    private void parse() throws ParseException {
        cmd = parser.parse(options, args.toArray(new String[]{}));
    }

    public void createMappings() throws ParseException {
        args.remove("-c");
        args.remove("-create");
        HashMap<String, Version> versions = new HashMap<>() {{
            put("b1.1_02", Version.BETA1_1__02);
            put("b1.2_02", Version.BETA1_2__02);
        }};

        options.addRequiredOption("t", "type", true, "Sets the type of mappings (client/server)");
        options.addRequiredOption("v", "version", true, "Sets the version used for generating mappings");
        options.addOption("o", "output", true, "Sets the output file");
        parse();

        String type = cmd.getOptionValue("type");
        String version = cmd.getOptionValue("version").toLowerCase();

        if (!validType(type)) unsupportedType();

        if (versions.containsKey(version)) {
            Version v = versions.get(version);
            TinyMCP tinymcp = type.equalsIgnoreCase("client") ? v.client() : v.server();
            tinymcp.write(Path.of(cmd.getOptionValue("output", String.format("%s.tinyv2", type))));
        } else {
            System.err.println("Unsupported version! Supported versions: " + versions.keySet());
            System.exit(1);
        }
    }

    public void applyMappings() throws ParseException {
        args.remove("-a");
        args.remove("-apply");
        options.addRequiredOption("i", "in", true, "Sets the input JAR");
        options.addRequiredOption("m", "mappings", true, "Sets the TinyV2 mappings file");
        options.addRequiredOption("t", "type", true, "Sets the type of mapping - mcp or notch");
        options.addOption("o", "out", true, "Sets the output JAR");
        parse();

        String inString = cmd.getOptionValue("in");

        Path in = Path.of(inString);
        Path out = Path.of(cmd.getOptionValue("out", inString.replace(".jar", "-out.jar")));
        Path mappings = Path.of(cmd.getOptionValue("mappings"));

        Mappings.remap(in, out, cmd.getOptionValue("type").equalsIgnoreCase("MCP")
                ? Mappings.notchToMCP(mappings)
                : Mappings.MCPtoNotch(mappings));
    }
}
