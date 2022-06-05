package com.alexsobiek.tinymcp.rgs;

public class RGSMethodMapEntry extends RGSEntry {
    protected final String notchName;
    protected final String methodSignature;

    // .method_map av/a ([DDDDIIIDDDD)V func_646_a
    public RGSMethodMapEntry(String notchClass, String notchName, String methodSignature, String mappedName) {
        super(Type.METHOD, notchClass, mappedName);
        this.notchName = notchName;
        this.methodSignature = methodSignature;
    }

    public static RGSMethodMapEntry parse(String entry) {
        String[] parts = entry.split("\s+");
        if (!parts[0].equals(".method_map"))
            throw new RuntimeException("Given invalid entry " + entry + " for SRGMethodMapEntry");
        String[] methodParts = splitByLast("/", parts[1]);
        return new RGSMethodMapEntry(methodParts[0], methodParts[1], parts[2], parts[3]);
    }

    public String getNotchName() {
        return notchName;
    }

    public String getMethodSignature() {
        return methodSignature;
    }
}
