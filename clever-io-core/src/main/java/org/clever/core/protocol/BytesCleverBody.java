package org.clever.core.protocol;

public class BytesCleverBody implements CleverBody {
    private byte[] data;

    public BytesCleverBody(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }
}
