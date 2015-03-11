package me.nithanim.longbuffer;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;
import static me.nithanim.longbuffer.RandomAccessFileBufferIntegrationTestHelper.*;

@Category(IntegrationTest.class)
public class RandomAccessFileBufferIntegrationWriteTest {
    RandomAccessFileBuffer buffer;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
    @Before
    public void setUp() throws Exception {
        buffer = getWriteFileBuffer();
        buffer.capacity(0);
    }
    
    @After
    public void tearDown() {
        buffer.dispose();
    }
    
    
    @Test
    public void testWriteSequential() throws Exception {
        byte[] nioArray = new byte[1+2+4+8+4+8];
        ByteBuffer nioBuffer = java.nio.ByteBuffer.wrap(nioArray);
        nioBuffer.put((byte)(Byte.MAX_VALUE/2));
        nioBuffer.putShort((short)(Short.MAX_VALUE/2));
        nioBuffer.putInt(Integer.MAX_VALUE/2);
        nioBuffer.putLong(Long.MAX_VALUE/2);
        nioBuffer.putFloat(Float.MAX_VALUE/2);
        nioBuffer.putDouble(Double.MAX_VALUE/2);
        
        buffer.writeByte((byte)(Byte.MAX_VALUE/2));
        buffer.writeShort((short)(Short.MAX_VALUE/2));
        buffer.writeInt(Integer.MAX_VALUE/2);
        buffer.writeLong(Long.MAX_VALUE/2);
        buffer.writeFloat(Float.MAX_VALUE/2);
        buffer.writeDouble(Double.MAX_VALUE/2);
        buffer.dispose();
        
        assertArrayEquals(nioArray, getFileAsByteArray(getWriteFileRaf()));
    }
    
    @Test
    public void testWriteSet() throws Exception {
        RandomAccessFile raf = buffer.getRaf();
        buffer.readerIndex(0);
        
        buffer.setByte(0, (byte)0x12);
        raf.seek(0);
        assertEquals((byte)0x12, raf.readByte());
        assertEquals(0, buffer.readerIndex());
        
        buffer.setShort(0, (short)0x1234);
        raf.seek(0);
        assertEquals((short)0x1234, raf.readShort());
        assertEquals(0, buffer.readerIndex());
        
        buffer.setInt(0, 0x12345678);
        raf.seek(0);
        assertEquals(0x12345678, raf.readInt());
        assertEquals(0, buffer.readerIndex());
        
        buffer.setLong(0, 0x123456789ABCDEFL);
        raf.seek(0);
        assertEquals(0x123456789ABCDEFL, raf.readLong());
        assertEquals(0, buffer.readerIndex());
        
        buffer.setFloat(0, Float.MAX_VALUE/2);
        raf.seek(0);
        assertEquals(Float.MAX_VALUE/2, raf.readFloat(), 5);
        assertEquals(0, buffer.readerIndex());
        
        buffer.setDouble(0, Double.MAX_VALUE/2);
        raf.seek(0);
        assertEquals(Double.MAX_VALUE/2, raf.readDouble(), 5);
        assertEquals(0, buffer.readerIndex());
        
        Buffer v = new VoidBuffer();
        
        v.capacity(8); v.readerIndex(0);
        buffer.setBytes(0, v);
        assertEquals(0, buffer.readerIndex());
        assertEquals(8, v.readerIndex());
        
        v.capacity(8); v.readerIndex(0);
        buffer.setBytes(0, v, 4);
        assertEquals(0, buffer.readerIndex());
        assertEquals(4, v.readerIndex());
        
        v.capacity(12); v.readerIndex(0);
        buffer.setBytes(0, v, 4, 4);
        assertEquals(0, buffer.readerIndex());
        assertEquals(0, v.readerIndex());
    }
    
    
    
    @Test
    public void testWriteWriterIndex() throws Exception {
        VoidBuffer v = new VoidBuffer();
        
        buffer.writeByte((byte)0);
        assertEquals(1, buffer.writerIndex());
        buffer.writeShort((short) 60000);
        assertEquals(1+2, buffer.writerIndex());
        buffer.writeInt(123435256);
        assertEquals(1+2+4, buffer.writerIndex());
        buffer.writeLong(1234355464524256L);
        assertEquals(1+2+4+8, buffer.writerIndex());
        buffer.writeFloat(123435256);
        assertEquals(1+2+4+8+4, buffer.writerIndex());
        buffer.writeDouble(1234355464524256L);
        assertEquals(1+2+4+8+4+8, buffer.writerIndex());
        
        buffer.capacity(0); buffer.writerIndex(0);
        buffer.writeBytes(new byte[5]);
        assertEquals(5, buffer.writerIndex());
        
        buffer.capacity(0); buffer.writerIndex(0);
        v.capacity(5); v.readerIndex(0);
        buffer.writeBytes(v);
        assertEquals(5, buffer.writerIndex());
        assertEquals(5, v.readerIndex());
        
        buffer.capacity(0); buffer.writerIndex(0);
        v.capacity(10); v.readerIndex(0);
        buffer.writeBytes(v, 5, 5);
        assertEquals(5, buffer.writerIndex());
        assertEquals(0, v.readerIndex());
    }
}
