package com.github.quillmc.tinymcp;

import com.alexsobiek.async.util.Lazy;

public enum Version {
    BETA1_1__02(new Lazy<>(TinyMCP::CLIENT_BETA1_1__02), new Lazy<>(TinyMCP::SERVER_BETA1_1__02));

    private final Lazy<TinyMCP> client;
    private final Lazy<TinyMCP> server;

    Version(Lazy<TinyMCP> client, Lazy<TinyMCP> server) {
        this.server = server;
        this.client = client;
    }

    public TinyMCP client() {
        return client.get();
    }

    public TinyMCP server() {
        return server.get();
    }
}
