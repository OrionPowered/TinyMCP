package com.alexsobiek.tinymcp.old.mcp;

import com.alexsobiek.tinymcp.old.MappingProvider;
import com.alexsobiek.tinymcp.csv.CSVReader;

public abstract class MCPMappingProvider extends MappingProvider {
    protected final CSVReader reader;

    protected MCPMappingProvider(CSVReader reader) {
        this.reader = reader;
    }
}
