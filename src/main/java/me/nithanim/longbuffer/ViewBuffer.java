package me.nithanim.longbuffer;

import java.nio.ByteOrder;

public class ViewBuffer extends AbstractBuffer {
    private final Buffer buffer;
    private final long start;
    private final long length;
    
    public ViewBuffer(Buffer buffer, long start, long length) {
        this.buffer = buffer;
        this.start = start;
        this.length = length;
    }
    
    @Override
    protected byte _getByte(long index) {
        return buffer.getByte(start + index);
    }

    @Override
    protected short _getShort(long index) {
        return buffer.getShort(start + index);
    }

    @Override
    protected int _getInt(long index) {
        return buffer.getInt(start + index);
    }

    @Override
    protected long _getLong(long index) {
        return buffer.getLong(start + index);
    }

    @Override
    protected float _getFloat(long index) {
        return buffer.getFloat(start + index);
    }

    @Override
    protected double _getDouble(long index) {
        return buffer.getDouble(start + index);
    }
    
    @Override
    public byte[] getBytes(long index, int length) {
        return buffer.getBytes(index, length);
    }
    
    @Override
    public void readBytes(byte[] dest) {
        buffer.readBytes(dest);
    }
    
    @Override
    protected Buffer _setByte(long index, byte value) {
        buffer.setByte(start + index, value);
        return this;
    }

    @Override
    protected Buffer _setShort(long index, short value) {
        buffer.setShort(start + index, value);
        return this;
    }

    @Override
    protected Buffer _setInt(long index, int value) {
        buffer.setInt(start + index, value);
        return this;
    }

    @Override
    protected Buffer _setLong(long index, long value) {
        buffer.setLong(start + index, value);
        return this;
    }

    @Override
    protected Buffer _setFloat(long index, float value) {
        buffer.setFloat(start + index, value);
        return this;
    }

    @Override
    protected Buffer _setDouble(long index, double value) {
        buffer.setDouble(start + index, value);
        return this;
    }
    
    @Override
    public Buffer writeBytes(Buffer source, long offset, long length) {
        buffer.writeBytes(source, offset, length);
        return this;
    }

    @Override
    protected void ensureWriteable(long index, long length) {
        if(!accessInBoundaries(index, length)) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    protected void ensureReadable(long index, long length) {
        ensureWriteable(index, length); //checks only for this buffer; real access is tested in wrapped buffer
    }
    
    @Override
    public ByteOrder order() {
        return buffer.order();
    }
    
    @Override
    public long capacity() {
        return length;
    }

    @Override
    public void capacity(long capacity) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Buffer order(ByteOrder order) {
        if(buffer.order() == order) {
            return this;
        } else {
            return new SwappedBuffer(this);
        }
    }

    @Override
    public Buffer slice(long index, long length) {
        if(!accessInBoundaries(index, length)) {
            throw new IndexOutOfBoundsException();
        }
        return new ViewBuffer(this, start + index, length);
    }
    
    private boolean accessInBoundaries(long index, long length) {
        if(index < 0 || length < 0) {
            return false;
        } else if(this.length < index + length) {
            return false;
        }
        return true;
    }

    @Override
    public void dispose() {
        //do not propergate dispose, because it is only a view
    }

    @Override
    public boolean isValid() {
        return buffer.isValid();
    }
    
    @Override
    public Buffer unwrap() {
        return buffer;
    }

    @Override
    public long readableBytes() {
        return length - readerIndex;
    }

    @Override
    public Buffer getBytes(long index, byte[] dest) {
        ensureWriteable(index, dest.length);
        buffer.getBytes(start + index, dest);
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src, long srcIndex, long length) {
        ensureWriteable(index, length);
        buffer.setBytes(start + index, src, srcIndex, length);
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src, long length) {
        ensureWriteable(index, length);
        buffer.setBytes(start + index, src, length);
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src) {
        ensureWriteable(index, src.readableBytes());
        buffer.setBytes(index, src);
        return this;
    }

    @Override
    public Buffer setBytes(long index, byte[] src) {
        ensureWriteable(index, src.length);
        buffer.setBytes(index, src);
        return this;
    }

    @Override
    public Buffer writeBytes(byte[] src) {
        ensureWriteable(src.length);
        buffer.writeBytes(src);
        writerIndex += src.length;
        return this;
    }

    @Override
    public Buffer writeBytes(Buffer src) {
        long readableBytes = src.readableBytes();
        ensureWriteable(readableBytes);
        buffer.setBytes(start + writerIndex, src);
        writerIndex += readableBytes;
        return this;
    }
    
    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("Not backed by array.");
    }
}
