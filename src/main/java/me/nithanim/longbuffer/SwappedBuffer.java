package me.nithanim.longbuffer;

import java.nio.ByteOrder;

public class SwappedBuffer implements Buffer {
    private final ByteOrder order;
    private final Buffer buffer;

    public SwappedBuffer(Buffer buffer) {
        this.buffer = buffer;
        if(buffer.order() == ByteOrder.LITTLE_ENDIAN) {
            this.order = ByteOrder.BIG_ENDIAN;
        } else {
            this.order = ByteOrder.LITTLE_ENDIAN;
        }
    }
    
    @Override
    public long capacity() {
        return buffer.capacity();
    }
    
    @Override
    public void capacity(long capacity) {
        buffer.capacity(capacity);
    }
    
    @Override
    public long readerIndex() {
        return buffer.readerIndex();
    }

    @Override
    public void readerIndex(long index) {
        buffer.readerIndex(index);
    }

    @Override
    public long writerIndex() {
        return buffer.writerIndex();
    }

    @Override
    public void writerIndex(long index) {
        buffer.writerIndex(index);
    }

    @Override
    public ByteOrder order() {
        return order;
    }

    @Override
    public Buffer order(ByteOrder order) {
        if(order == this.order) {
            return this;
        } else {
            return buffer;
        }
    }

    @Override
    public Buffer slice(long index, long length) {
        Buffer newBuffer = buffer.slice(index, length);
        return BufferUtil.ensureByteOrder(newBuffer, order);
    }

    @Override
    public byte getByte(long index) {
        return buffer.getByte(index);
    }

    @Override
    public short getShort(long index) {
        return Short.reverseBytes(buffer.getShort(index));
    }

    @Override
    public int getInt(long index) {
        return Integer.reverseBytes(buffer.getInt(index));
    }
    
    @Override
    public long getUnsignedInt(long index) {
        return getInt(index) & 0xFFFFFFFFL;
    }
    
    @Override
    public long getLong(long index) {
        return Long.reverseBytes(buffer.getLong(index));
    }

    @Override
    public float getFloat(long index) {
        return Float.intBitsToFloat(getInt(index));
    }

    @Override
    public double getDouble(long index) {
        return Double.longBitsToDouble(getLong(index));
    }
    
    @Override
    public byte[] getBytes(long index, int length) {
        return buffer.getBytes(index, length);
    }

    @Override
    public byte readByte() {
        return buffer.readByte();
    }

    @Override
    public short readShort() {
        return Short.reverseBytes(buffer.readShort());
    }

    @Override
    public int readInt() {
        return Integer.reverseBytes(buffer.readInt());
    }
    
    @Override
    public long readUnsignedInt() {
        return readInt() & 0xFFFFFFFFL;
    }
    
    @Override
    public long readLong() {
        return Long.reverseBytes(buffer.readLong());
    }
    
    @Override
    public void readBytes(byte[] dest) {
        buffer.readBytes(dest);
    }

    @Override
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }
    
    @Override
    public Buffer setByte(long index, byte value) {
        buffer.setByte(index, value);
        return this;
    }

    @Override
    public Buffer setShort(long index, short value) {
        buffer.setShort(index, Short.reverseBytes(value));
        return this;
    }

    @Override
    public Buffer setInt(long index, int value) {
        buffer.setInt(index, Integer.reverseBytes(value));
        return this;
    }

    @Override
    public Buffer setLong(long index, long value) {
        buffer.setLong(index, Long.reverseBytes(value));
        return this;
    }

    @Override
    public Buffer setFloat(long index, float value) {
        setInt(index, Float.floatToRawIntBits(value));
        return this;
    }

    @Override
    public Buffer setDouble(long index, double value) {
        setLong(index, Double.doubleToRawLongBits(value));
        return this;
    }

    @Override
    public Buffer writeByte(byte value) {
        buffer.writeByte(value);
        return this;
    }

    @Override
    public Buffer writeShort(short value) {
        buffer.writeShort(Short.reverseBytes(value));
        return this;
    }

    @Override
    public Buffer writeInt(int value) {
        buffer.writeInt(Integer.reverseBytes(value));
        return this;
    }

    @Override
    public Buffer writeLong(long value) {
        buffer.writeLong(Long.reverseBytes(value));
        return this;
    }

    @Override
    public Buffer writeFloat(float value) {
        writeInt(Float.floatToRawIntBits(value));
        return this;
    }

    @Override
    public Buffer writeDouble(double value) {
        writeLong(Double.doubleToRawLongBits(value));
        return this;
    }
    
    @Override
    public Buffer writeBytes(Buffer source, long offset, long length) {
        buffer.writeBytes(source, offset, length);
        return this;
    }
    
    @Override
    public void dispose() {
        buffer.dispose();
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
        return buffer.readableBytes();
    }

    @Override
    public Buffer getBytes(long index, byte[] dest) {
        buffer.getBytes(index, dest);
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src, long srcIndex, long length) {
        buffer.setBytes(index, src, srcIndex, length);
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src, long length) {
        buffer.setBytes(index, src, length);
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src) {
        buffer.setBytes(index, src);
        return this;
    }

    @Override
    public Buffer setBytes(long index, byte[] src) {
        buffer.setBytes(index, src);
        return this;
    }

    @Override
    public Buffer writeBytes(byte[] src) {
        buffer.writeBytes(src);
        return this;
    }

    @Override
    public Buffer writeBytes(Buffer src) {
        buffer.writeBytes(src);
        return this;
    }
    
    @Override
    public byte[] array() {
        return buffer.array();
    }
}
