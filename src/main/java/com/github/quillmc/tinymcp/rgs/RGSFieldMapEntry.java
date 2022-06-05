package com.github.quillmc.tinymcp.rgs;

public class RGSFieldMapEntry extends RGSEntry {

    protected final String notchName;

    // .field_map av/b field_934_b
    public RGSFieldMapEntry(String notchClass, String notchName, String mappedName) {
        super(Type.FIELD, notchClass, mappedName);
        this.notchName = notchName;
    }

    public static RGSFieldMapEntry parse(String entry) {
        String[] parts = entry.split("\s+");
        if (!parts[0].equals(".field_map"))
            throw new RuntimeException("Given invalid entry " + entry + " for SRGFieldMapEntry");
        String[] fieldParts = splitByLast("/", parts[1]);
        return new RGSFieldMapEntry(fieldParts[0], fieldParts[1], parts[2]);
    }

    public String getNotchName() {
        return notchName;
    }
}
