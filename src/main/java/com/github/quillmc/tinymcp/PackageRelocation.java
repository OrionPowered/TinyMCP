package com.github.quillmc.tinymcp;

public interface PackageRelocation {
    PackageRelocation DEFAULT = (notchClass, namedClass) -> String.format("net/minecraft/%s", namedClass);

    String relocate(String notchClass, String namedClass);
}
