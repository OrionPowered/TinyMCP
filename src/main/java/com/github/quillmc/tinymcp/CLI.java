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
            if (this.args.remove("-m") || this.args.remove("-mappings")) mappings();
            else if (this.args.remove("-r") || this.args.remove("-remap")) remap();
            else {
                System.err.println("Unsupported action! Valid actions:");
                System.err.println("* -m, -mappings     Generates mappings");
                System.err.println("* -r, -remap        Remaps JAR");
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

    public void mappings() throws ParseException {
        HashMap<String, Version> versions = new HashMap<>() {{
            put("b1.1_02", Version.BETA1_1__02);
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

    public void remap() {

    }
}
