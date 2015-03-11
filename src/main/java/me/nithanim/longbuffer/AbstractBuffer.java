package me.nithanim.longbuffer;

import java.nio.ByteOrder;

public abstract class AbstractBuffer implements Buffer {
    protected long readerIndex;
    protected long writerIndex;
    
    @Override
    public long readerIndex() {
        return readerIndex;
    }

    @Override
    public void readerIndex(long index) {
        readerIndex = index;
    }

    @Override
    public long writerIndex() {
        return writerIndex;
    }

    @Override
    public void writerIndex(long index) {
        writerIndex = index;
    }
    
    /**
     * If the ByteOrder is the same it returns "this". Otherwise
     * a new Buffer with the specified ByteOrder is created.
     * 
     * @param order
     * @return 
     */
    @Override
    public Buffer order(ByteOrder order) {
        return BufferUtil.ensureByteOrder(this, order);
    }
    
    @Override
    public byte getByte(long index) {
        ensureReadable(index, Byte.SIZE/8);
        return _getByte(index);
    }

    @Override
    public short getShort(long index) {
        ensureReadable(index, Short.SIZE/8);
        return _getShort(index);
    }

    @Override
    public int getInt(long index) {
        ensureReadable(index, Integer.SIZE/8);
        return _getInt(index);
    }
    
    @Override
    public long getUnsignedInt(long index) {
        return getLong(index) & 0xFFFFFFFF;
    }

    @Override
    public long getLong(long index) {
        ensureReadable(index, Long.SIZE/8);
        return _getLong(index);
    }

    @Override
    public float getFloat(long index) {
        ensureReadable(index, Float.SIZE/8);
        return _getFloat(index);
    }

    @Override
    public double getDouble(long index) {
        ensureReadable(index, Double.SIZE/8);
        return _getDouble(index);
    }
    
    @Override
    public byte readByte() {
        long i = readerIndex;
        byte v = getByte(i);
        readerIndex += Byte.SIZE/8;
        return v;
    }

    @Override
    public short readShort() {
        long i = readerIndex;
        short v = getShort(i);
        readerIndex += Short.SIZE/8;
        return v;
    }

    @Override
    public int readInt() {
        long i = readerIndex;
        int v = getInt(i);
        readerIndex += Integer.SIZE/8;
        return v;
    }
    
    @Override
    public long readUnsignedInt() {
        return readInt() & 0xFFFFFFFFL;
    }

    @Override
    public long readLong() {
        long i = readerIndex;
        long v = getLong(i);
        readerIndex += Long.SIZE/8;
        return v;
    }

    @Override
    public float readFloat() {
        long i = readerIndex;
        float v = getFloat(i);
        readerIndex += Float.SIZE/8;
        return v;
    }

    @Override
    public double readDouble() {
        long i = readerIndex;
        double v = getDouble(i);
        readerIndex += Double.SIZE/8;
        return v;
    }
    
    @Override
    public Buffer writeByte(byte value) {
        setByte(writerIndex, value);
        writerIndex += Byte.SIZE/8;
        return this;
    }

    @Override
    public Buffer writeShort(short value) {
        setShort(writerIndex, value);
        writerIndex += Short.SIZE/8;
        return this;
    }

    @Override
    public Buffer writeInt(int value) {
        setInt(writerIndex, value);
        writerIndex += Integer.SIZE/8;
        return this;
    }

    @Override
    public Buffer writeLong(long value) {
        setLong(writerIndex, value);
        writerIndex += Long.SIZE/8;
        return this;
    }

    @Override
    public Buffer writeFloat(float value) {
        setFloat(writerIndex, value);
        writerIndex += Float.SIZE/8;
        return this;
    }

    @Override
    public Buffer writeDouble(double value) {
        setDouble(writerIndex, value);
        writerIndex += Double.SIZE/8;
        return this;
    }

    @Override
    public Buffer setByte(long index, byte value) {
        ensureWriteable(index, Byte.SIZE/8);
        _setByte(index, value);
        return this;
    }

    @Override
    public Buffer setShort(long index, short value) {
        ensureWriteable(index, Short.SIZE/8);
        _setShort(index, value);
        return this;
    }

    @Override
    public Buffer setInt(long index, int value) {
        ensureWriteable(index, Integer.SIZE/8);
        _setInt(index, value);
        return this;
    }

    @Override
    public Buffer setLong(long index, long value) {
        ensureWriteable(index, Long.SIZE/8);
        _setLong(index, value);
        return this;
    }

    @Override
    public Buffer setFloat(long index, float value) {
        ensureWriteable(index, Float.SIZE/8);
        _setFloat(index, value);
        return this;
    }

    @Override
    public Buffer setDouble(long index, double value) {
        ensureWriteable(index, Double.SIZE/8);
        _setDouble(index, value);
        return this;
    }
    
    protected abstract byte _getByte(long index);
    protected abstract short _getShort(long index);
    protected abstract int _getInt(long index);
    protected abstract long _getLong(long index);
    protected abstract float _getFloat(long index);
    protected abstract double _getDouble(long index);
    
    protected abstract Buffer _setByte(long index, byte value);
    protected abstract Buffer _setShort(long index, short value);
    protected abstract Buffer _setInt(long index, int value);
    protected abstract Buffer _setLong(long index, long value);
    protected abstract Buffer _setFloat(long index, float value);
    protected abstract Buffer _setDouble(long index, double value);
    
    protected void ensureWriteable(long length) {
        ensureReadable(writerIndex, length);
    }
    
    protected abstract void ensureWriteable(long index, long length);
    
    protected void ensureReadable(long length) {
        ensureReadable(readerIndex, length);
    }
    
    protected abstract void ensureReadable(long index, long length);
    
    /**
     * Creates a view of a part of this buffer. In other words:
     * Creates a new Buffer which acts as a window for only the
     * specified part of the underlying buffer. It cannot be read
     * or written outside of this range.
     * 
     * @param index
     * @param length
     * @return 
     */
    @Override
    public Buffer slice(long index, long length) {
        return new ViewBuffer(this, index, length);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('<').append(getClass().getSimpleName());
        sb.append('(').append("rIdx: ").append(readerIndex());
        sb.append(", wIdx: ").append(writerIndex());
        sb.append(", cap: ").append(capacity());
        sb.append(')').append('>');
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return BufferUtil.equals(this, obj);
    }
}