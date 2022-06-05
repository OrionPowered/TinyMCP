package com.github.quillmc.tinymcp.rgs;

public abstract class RGSEntry {

    public enum Type {
        CLASS,
        METHOD,
        FIELD
    }

    public static RGSEntry parse(String entry) {
        if (entry.startsWith(".class_map")) return RGSClassMapEntry.parse(entry);
        else if (entry.startsWith(".method_map")) return RGSMethodMapEntry.parse(entry);
        else if (entry.startsWith(".field_map")) return RGSFieldMapEntry.parse(entry);
        else throw new RuntimeException("Given invalid entry " + entry + " for RGSEntry");
    }

    protected final Type type;
    protected final String notchClass;
    protected final String mappedName;

    public RGSEntry(Type type, String notchClass, String mappedName) {
        this.type = type;
        this.notchClass = notchClass;
        this.mappedName = mappedName;
    }

    protected static String[] splitByLast(String splitter, String input) {
        int i = input.lastIndexOf(splitter);
        return new String[]{input.substring(0, i), input.substring(i + 1)};
    }

    public Type getType() {
        return type;
    }

    public String getNotchClass() {
        return notchClass;
    }

    public String getMappedName() {
        return mappedName;
    }
}
