package com.github.quillmc.tinymcp;

import com.alexsobiek.nexus.lazy.Lazy;
public enum Version {
    BETA1_1__02(new Lazy<>(TinyMCP::CLIENT_BETA1_1__02), new Lazy<>(TinyMCP::SERVER_BETA1_1__02)),
    // Quick note about Beta 1.2: Beta 1.2_02 exists for the client only. The server only goes to Beta 1.2_01
    BETA1_2__02(new Lazy<>(TinyMCP::CLIENT_BETA1_2__02), new Lazy<>(TinyMCP::SERVER_BETA1_2_01));

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
