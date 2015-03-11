package me.nithanim.longbuffer;

import java.io.RandomAccessFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;
import static me.nithanim.longbuffer.RandomAccessFileBufferIntegrationTestHelper.*;

@Category(IntegrationTest.class)
public class RandomAccessFileBufferIntegrationReadTest {
    private static byte[] FILE_BYTES;
    Buffer buffer;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        FILE_BYTES = getFileAsByteArray(getReadFileRaf());
    }
    
    @Before
    public void setUp() throws Exception {
        buffer = getReadFileBuffer();
    }
    
    @After
    public void tearDown() {
        buffer.dispose();
    }
    
    @Test
    public void testLength() throws Exception {
        RandomAccessFile raf = getReadFileRaf();
        assertEquals(raf.length(), buffer.capacity());
        raf.close();
    }
    
    @Test
    public void testFullRead() throws Exception {
        assertArrayEquals(FILE_BYTES, buffer.getBytes(0, (int)buffer.capacity()));
    }
    
    @Test
    public void testReadAtEnd() throws Exception {
        buffer.readerIndex(buffer.capacity()-1);
        assertEquals(FILE_BYTES[FILE_BYTES.length-1], buffer.readByte());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBeyondEndOneByte() throws Exception {
        buffer.readerIndex(buffer.capacity());
        assertEquals(FILE_BYTES[FILE_BYTES.length-1], buffer.readByte());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBeyondEndOneInt() throws Exception {
        buffer.readerIndex(buffer.capacity());
        assertEquals(FILE_BYTES[FILE_BYTES.length-2], buffer.readInt());
    }
}
