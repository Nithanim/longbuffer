package me.nithanim.longbuffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;

public class RandomAccessFileBuffer extends AbstractBuffer implements Disposable {
    private final RandomAccessFile raf;

    public RandomAccessFileBuffer(RandomAccessFile raf) {
        this.raf = raf;
    }
    
    @Override
    protected byte _getByte(long index) {
        try {
            raf.seek(index);
            return raf.readByte();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    @Override
    protected short _getShort(long index) {
        try {
            raf.seek(index);
            return raf.readShort();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    @Override
    protected int _getInt(long index) {
        try {
            raf.seek(index);
            return raf.readInt();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    protected long _getLong(long index) {
        try {
            raf.seek(index);
            return raf.readLong();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    protected float _getFloat(long index) {
        try {
            raf.seek(index);
            return raf.readFloat();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    protected double _getDouble(long index) {
        try {
            raf.seek(index);
            return raf.readDouble();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    @Override
    public byte[] getBytes(long index, int length) {
        byte[] dest = new byte[length];
        getBytes(index, dest);
        return dest; 
    }
    
    @Override
    public void readBytes(byte[] dest) {
        getBytes(readerIndex, dest);
        readerIndex += dest.length;
    }
    
    @Override
    public Buffer getBytes(long index, byte[] dest) {
        ensureReadable(index, dest.length);
        try {
            raf.seek(index);
            raf.readFully(dest);
        } catch(IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return this;
    }
    
    
    @Override
    protected Buffer _setByte(long index, byte value) {
        try {
            raf.seek(index);
            raf.writeByte(value);
            return this;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    protected Buffer _setShort(long index, short value) {
        try {
            raf.seek(index);
            raf.writeShort(value);
            return this;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    protected Buffer _setInt(long index, int value) {
        try {
            raf.seek(index);
            raf.writeInt(value);
            return this;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    protected Buffer _setLong(long index, long value) {
        try {
            raf.seek(index);
            raf.writeLong(value);
            return this;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    protected Buffer _setFloat(long index, float value) {
        try {
            raf.seek(index);
            raf.writeFloat(value);
            return this;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    protected Buffer _setDouble(long index, double value) {
        try {
            raf.seek(index);
            raf.writeDouble(value);
            return this;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    @Override
    public Buffer setBytes(long index, byte[] src) {
        try {
            ensureWriteable(index, src.length);
            raf.write(src);
            return this;
        } catch(IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    @Override
    public Buffer setBytes(long index, Buffer src) {
        return setBytes(index, src, src.readableBytes());
    }
    
    @Override
    public Buffer setBytes(long index, Buffer src, long length) {
        setBytes(index, src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
        return this;
    }

    @Override
    public Buffer setBytes(long index, Buffer src, long srcIndex, long length) {
        try {
            long position = 0;
            long bytesRemaining = length;
            byte[] buffer = new byte[length > 1024 ? 1024 : (int)length];

            while(bytesRemaining > 0) {
                if(bytesRemaining < buffer.length) {
                    buffer = new byte[(int)bytesRemaining];
                }
                
                src.getBytes(srcIndex + position, buffer);
                raf.seek(index + position);
                raf.write(buffer);
                position += buffer.length;
                bytesRemaining -= buffer.length;
            }
        } catch(IOException ex) {
            throw new UncheckedIOException(ex);
        }
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
        long readableBytes = src.readableBytes();
        writeBytes(src, src.readerIndex(), readableBytes);
        src.readerIndex(src.readerIndex() + readableBytes);
        return this;
    }
    
    @Override
    public Buffer writeBytes(Buffer src, long srcIndex, long length) {
        setBytes(writerIndex, src, srcIndex, length);
        writerIndex = writerIndex + length;
        return this;
    }
    
    public void transferTo(long srcPos, SeekableByteChannel dest, long destPos, long length) throws IOException {
        long originalPos = dest.position();
        transferTo(srcPos, length, dest);
        dest.position(originalPos);
    }
    
    public void transferTo(final long srcPos, final long length, final SeekableByteChannel dest) throws IOException {
        //http://stackoverflow.com/questions/7379469/filechannel-transferto-for-large-file-in-windows
        FileChannel src = raf.getChannel();
        
        long progress = 0;
        long bytesRemaining = length;
        while(bytesRemaining > 0) {
            long bytesTransferred = src.transferTo(srcPos + progress, bytesRemaining, dest);
            if (bytesTransferred > 0) {
                progress += bytesTransferred;
                bytesRemaining -= bytesTransferred;
            }
        }
    }
    
    @Override
    protected void ensureWriteable(long index, long length) {
        //possible to get mode of RAF afterwards?
        //can write beyond capacity to extend file
    }
    
    @Override
    protected void ensureReadable(long index, long length) {
        if(index < 0) {
            throw new IndexOutOfBoundsException("Index must be greater or equal 0! Got " + index);
        } else if(length < 0) {
            throw new IndexOutOfBoundsException("Length must be greater or equal 0! Got " + length);
        } else if(index + length > capacity()) {
            throw new IndexOutOfBoundsException("Attempted to read " + length
                    + " bytes at index " + index + "! But capacity is only " + capacity());
        }
    }
    
    @Override
    public long readableBytes() {
        return capacity() - readerIndex;
    }
    
    /**
     * Maps to {@link RandomAccessFile#length()}
     * 
     * @return 
     */
    @Override
    public long capacity() {
        try {
            return raf.length();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    /**
     * Maps directly to {@link RandomAccessFile#setLength(long)}
     * 
     * @param capacity 
     */
    @Override
    public void capacity(long capacity) {
        try {
            raf.setLength(capacity);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
    
    @Override
    public void dispose() {
        try {
            raf.getChannel().force(true);
            raf.close();
        } catch (IOException ex) {
            //ignore
        }
    }

    @Override
    public boolean isValid() {
        try {
            return raf.getFD().valid();
        } catch (IOException ex) {
            return false;
        }
    }
    
    @Override
    public Buffer unwrap() {
        return null;
    }
    
    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("Not backed by array.");
    }

    /*package-protected*/ RandomAccessFile getRaf() {
        return raf;
    }
}
