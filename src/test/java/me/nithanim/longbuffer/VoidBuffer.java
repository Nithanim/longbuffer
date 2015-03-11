package me.nithanim.longbuffer;

import java.nio.ByteOrder;
import java.util.Arrays;

public class VoidBuffer extends AbstractBuffer {
    private long capacity;
    
    public VoidBuffer() {
        this(2048);
    }
    
    public VoidBuffer(long capacity) {
        this.capacity = capacity;
    }
    
    @Override
    protected byte _getByte(long index) {
        return 0;
    }

    @Override
    protected short _getShort(long index) {
        return 0;
    }

    @Override
    protected int _getInt(long index) {
        return 0;
    }

    @Override
    protected long _getLong(long index) {
        return 0;
    }

    @Override
    protected float _getFloat(long index) {
        return 0;
    }

    @Override
    protected double _getDouble(long index) {
        return 0;
    }

    @Override
    protected Buffer _setByte(long index, byte value) {
        return this;
    }

    @Override
    protected Buffer _setShort(long index, short value) {
        return this;
    }

    @Override
    protected Buffer _setInt(long index, int value) {
        return this;
    }

    @Override
    protected Buffer _setLong(long index, long value) {
        return this;
    }

    @Override
    protected Buffer _setFloat(long index, float value) {
        return this;
    }

    @Override
    protected Buffer _setDouble(long index, double value) {
        return this;
    }

    @Override
    protected void ensureWriteable(long index, long length) {
    }

    @Override
    protected void ensureReadable(long index, long length) {
    }

    @Override
    public long capacity() {
        return capacity;
    }

    @Override
    public void capacity(long capacity) {
        this.capacity = capacity;
    }

    @Override
    public long readableBytes() {
        return capacity() - readerIndex;
    }

    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    @Override
    public Buffer unwrap() {
        return null;
    }

    @Override
    public byte[] getBytes(long index, int length) {
        return new byte[length];
    }

    @Override
    public Buffer getBytes(long index, byte[] dest) {
        Arrays.fill(dest, (byte)0);
        return this;
    }

    @Override
    public void readBytes(byte[] dest) {
        Arrays.fill(dest, (byte)0);
    }

    @Override
    public Buffer setBytes(long index, Buffer src, long srcIndex, long length) {
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src, long length) {
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src) {
        return this;
    }

    @Override
    public Buffer setBytes(long index, byte[] src) {
        return this;
    }

    @Override
    public Buffer writeBytes(byte[] src) {
        return this;
    }

    @Override
    public Buffer writeBytes(Buffer src) {
        return this;
    }

    @Override
    public Buffer writeBytes(Buffer src, long srcIndex, long length) {
        return this;
    }

    @Override
    public void dispose() {
    }
    
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("Not backed by array.");
    }
}
