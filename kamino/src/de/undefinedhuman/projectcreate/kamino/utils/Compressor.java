package de.undefinedhuman.projectcreate.kamino.utils;

@FunctionalInterface
public interface Compressor {
    byte[] compress(byte[] input);
}
