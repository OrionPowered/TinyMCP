package com.alexsobiek.tinymcp;

import com.alexsobiek.tinymcp.rgs.RGS;

import java.nio.file.Path;

public class TinyMCP {

    public static void main(String[] args) {
        TinyMCP tmcp = serverWithRGS(Path.of("mcp").resolve("conf"));

        tmcp.getIntermediary().forEach((e, m) -> {
            System.out.printf("Found intermediary mapping %s -> %s%n", e, m.targetName());
        });

        System.out.println(tmcp.getIntermediary().findMethodEntry("func_240_b"));
    }


    protected MappingProvider intermediary;
    protected MappingProvider named;

    public static TinyMCP serverWithRGS(Path mcpConf) {
        TinyMCP tmcp = new TinyMCP();
        tmcp.intermediary = new RGS(mcpConf.resolve("minecraft_server.rgs").toFile());

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
}
