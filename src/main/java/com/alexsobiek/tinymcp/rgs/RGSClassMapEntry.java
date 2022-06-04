package com.alexsobiek.tinymcp.rgs;

public class RGSClassMapEntry extends RGSEntry {

    // Example: .class_map av NoiseGeneratorPerlin
    public RGSClassMapEntry(String notchClass, String mappedName) {
        super(Type.CLASS, notchClass, mappedName);
    }

    public static RGSClassMapEntry parse(String entry) {
        String[] parts = entry.split("\s+");
        if (!parts[0].equals(".class_map")) throw new RuntimeException("Given invalid entry " + entry + " for SRGClassMapEntry");
        return new RGSClassMapEntry(parts[1], parts[2]);
    }
}
