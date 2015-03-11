package me.nithanim.longbuffer;

import java.nio.ByteOrder;
import java.util.Arrays;

public class ByteArrayBuffer extends AbstractBuffer {
    private byte[] arr;
    
    public ByteArrayBuffer(int capacity) {
        this(new byte[capacity]);
    }
    
    public ByteArrayBuffer(byte[] arr) {
        this.arr = arr;
    }
    
    @Override
    protected byte _getByte(long index) {
        return arr[(int)index];
    }
    
    @Override
    protected short _getShort(long index) {
        int i = (int)index;
        return (short)(((arr[i++] & 0xFF) << 8) | (arr[i++] & 0xFF));
    }
    
    @Override
    protected int _getInt(long index) {
        int i = (int)index;
        return (((arr[i++] & 0xFF) << 3*8)
                | ((arr[i++] & 0xFF) << 2*8)
                | ((arr[i++] & 0xFF) << 1*8)
                | (arr[i++] & 0xFF));
    }
    
    @Override
    protected long _getLong(long index) {
        int i = (int)index;
        return (((arr[i++] & 0xFFL) << 7*8)
                | ((arr[i++] & 0xFFL) << 6*8)
                | ((arr[i++] & 0xFFL) << 5*8)
                | ((arr[i++] & 0xFFL) << 4*8)
                | ((arr[i++] & 0xFFL) << 3*8)
                | ((arr[i++] & 0xFFL) << 2*8)
                | ((arr[i++] & 0xFFL) << 1*8)
                | (arr[i++] & 0xFFL));
    }
    
    @Override
    protected float _getFloat(long index) {
        return Float.intBitsToFloat(getInt(index));
    }
    
    @Override
    protected double _getDouble(long index) {
        return Double.longBitsToDouble(getLong(index));
    }
    
    @Override
    protected Buffer _setByte(long index, byte value) {
        arr[(int)index] = value;
        return this;
    }

    @Override
    protected Buffer _setShort(long index, short value) {
        int i = (int)index;
        arr[i++] = (byte)((value >> 8) & 0xFF);
        arr[i++] = (byte)(value & 0xFF);
        return this;
    }

    @Override
    protected Buffer _setInt(long index, int value) {
        int i = (int)index;
        arr[i++] = (byte)((value >> 3*8) & 0xFF);
        arr[i++] = (byte)((value >> 2*8) & 0xFF);
        arr[i++] = (byte)((value >> 1*8) & 0xFF);
        arr[i++] = (byte)((value) & 0xFF);
        return this;
    }

    @Override
    protected Buffer _setLong(long index, long value) {
        int i = (int)index;
        arr[i++] = (byte)((value >> 7*8) & 0xFFL);
        arr[i++] = (byte)((value >> 6*8) & 0xFFL);
        arr[i++] = (byte)((value >> 5*8) & 0xFFL);
        arr[i++] = (byte)((value >> 4*8) & 0xFFL);
        arr[i++] = (byte)((value >> 3*8) & 0xFFL);
        arr[i++] = (byte)((value >> 2*8) & 0xFFL);
        arr[i++] = (byte)((value >> 1*8) & 0xFFL);
        arr[i++] = (byte)((value) & 0xFFL);
        return this;
    }

    @Override
    protected Buffer _setFloat(long index, float value) {
        _setInt(index, Float.floatToRawIntBits(value));
        return this;
    }

    @Override
    protected Buffer _setDouble(long index, double value) {
        _setLong(index, Double.doubleToRawLongBits(value));
        return this;
    }

    @Override
    protected void ensureWriteable(long index, long length) {
        if(index > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Index is greater than allowed for an integer!");
        }
        if(index + length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Index (" + index + ") + length ("
                    + length + ") is greater than allowed for an integer!");
        }
        if(index + length > arr.length) {
            throw new IllegalArgumentException("Index " + index + " and length "
                    + length + " would get out of bounds (" + arr.length + ")!");
        }
    }

    @Override
    protected void ensureReadable(long index, long length) {
        ensureWriteable(index, length);
    }
    
    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    @Override
    public long capacity() {
        return arr.length;
    }

    @Override
    public void capacity(long capacity) {
        arr = Arrays.copyOf(arr, (int)capacity);
    }
    
    @Override
    public void dispose() {
    }

    @Override
    public long readableBytes() {
        return arr.length - readerIndex;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Buffer unwrap() {
        return null;
    }

    @Override
    public byte[] getBytes(long index, int length) {
        ensureReadable(index, length);
        byte[] ba = new byte[length];
        System.arraycopy(arr, (int)index, ba, 0, length);
        return ba;
    }

    @Override
    public Buffer getBytes(long index, byte[] dest) {
        ensureReadable(index, dest.length);
        System.arraycopy(arr, (int)index, dest, 0, dest.length);
        return this;
    }

    @Override
    public void readBytes(byte[] dest) {
        ensureReadable(readerIndex, dest.length);
        System.arraycopy(arr, (int)readerIndex, dest, 0, dest.length);
    }

    @Override
    public Buffer setBytes(long index, Buffer src, long srcIndex, long length) {
        ensureWriteable(index, src.readableBytes());
        byte[] ba = new byte[(int)src.readableBytes()];
        src.getBytes(index, ba);
        System.arraycopy(ba, 0, arr, (int)index, ba.length);
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src, long length) {
        setBytes(index, src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src) {
        setBytes(index, src, src.readableBytes());
        return this;
    }

    @Override
    public Buffer setBytes(long index, byte[] src) {
        ensureWriteable(index, src.length);
        System.arraycopy(src, 0, arr, (int)index, src.length);
        return this;
    }

    @Override
    public Buffer writeBytes(byte[] src) {
        setBytes(writerIndex, src);
        writerIndex += src.length;
        return this;
    }

    @Override
    public Buffer writeBytes(Buffer src) {
        setBytes(writerIndex, src);
        writerIndex += src.readableBytes();
        return this;
    }

    @Override
    public Buffer writeBytes(Buffer src, long srcIndex, long length) {
        setBytes(writerIndex, src, srcIndex, length);
        return this;
    }

    @Override
    public byte[] array() {
        return arr;
    }
}
