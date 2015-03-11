package me.nithanim.longbuffer;

import java.nio.ByteOrder;

public interface Buffer extends Disposable {
    long capacity();
    void capacity(long capacity);
    long readerIndex();
    void readerIndex(long index);
    long writerIndex();
    void writerIndex(long index);
    
    /**
     * Returns the remaining readable bytes.
     * In other words it returns (capacity() - readerIndex).
     * @return 
     */
    long readableBytes();
    ByteOrder order();
    Buffer order(ByteOrder order);
    Buffer slice(long index, long length);
    boolean isValid();
    Buffer unwrap();
    
    byte getByte(long index);
    short getShort(long index);
    int getInt(long index);
    long getUnsignedInt(long index);
    long getLong(long index);
    float getFloat(long index);
    double getDouble(long index);
    byte[] getBytes(long index, int length);
    Buffer getBytes(long index, byte[] dest);
    
    byte readByte();
    short readShort();
    int readInt();
    long readUnsignedInt();
    long readLong();
    float readFloat();
    double readDouble();
    void readBytes(byte[] dest);
    
    Buffer setByte(long index, byte value);
    Buffer setShort(long index, short value);
    Buffer setInt(long index, int value);
    Buffer setLong(long index, long value);
    Buffer setFloat(long index, float value);
    Buffer setDouble(long index, double value);
    
    /**
     * Does not modify the readerIndex of src (since index is provided)
     * 
     * @param index
     * @param src
     * @param srcIndex
     * @param length
     * @return 
     */
    Buffer setBytes(long index, Buffer src, long srcIndex, long length);
    
    /**
     * Modifies readerIndex of src
     * 
     * @param index
     * @param src
     * @param length
     * @return 
     */
    Buffer setBytes(long index, Buffer src, long length);
    
    /**
     * Modifies readerIndex
     * 
     * @param index
     * @param src
     * @return 
     */
    Buffer setBytes(long index, Buffer src);
    Buffer setBytes(long index, byte[] src);
    
    Buffer writeByte(byte value);
    Buffer writeShort(short value);
    Buffer writeInt(int value);
    Buffer writeLong(long value);
    Buffer writeFloat(float value);
    Buffer writeDouble(double value);
    Buffer writeBytes(byte[] src);
    
    /**
     * Reads all readable bytes from src and writes it to this buffer.
     * 
     * @param src
     * @return 
     */
    Buffer writeBytes(Buffer src);
    Buffer writeBytes(Buffer src, long srcIndex, long length);
    
    /**
     * Returns the underlying array of the buffer if used.
     * 
     * @throws UnsupportedOperationException if not backed by array
     * @return the array
     */
    byte[] array();
}
