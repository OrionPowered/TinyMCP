package com.github.quillmc.tinymcp.csv;

import java.util.function.BiConsumer;

public record CSVEntry(String[] header, String[] row) {
    public void forEach(BiConsumer<String, String> entries) {
        int hLen = header.length;
        int rLen = row.length;
        int loopLen = Math.max(hLen, rLen);
        for (int i = 0; i < loopLen; i++)
            entries.accept((hLen < i) ? header[i] : "", (rLen < i) ? row[i] : "");
    }
}
