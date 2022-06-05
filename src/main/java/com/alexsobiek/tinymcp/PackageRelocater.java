package com.alexsobiek.tinymcp;

public interface PackageRelocater {
    PackageRelocater DEFAULT = (notchClass, namedClass) -> String.format("net/minecraft/src/%s", namedClass);

    String relocate(String notchClass, String namedClass);
}
