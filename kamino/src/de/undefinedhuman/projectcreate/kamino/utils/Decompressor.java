package de.undefinedhuman.projectcreate.kamino.utils;

@FunctionalInterface
public interface Decompressor {
    byte[] decompress(byte[] input, int decompressedLength);
}
