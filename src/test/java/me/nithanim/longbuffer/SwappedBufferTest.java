package me.nithanim.longbuffer;

import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SwappedBufferTest {
    ByteArrayBuffer byteBuffer;
    SwappedBuffer swappedBuffer;
    
    public SwappedBufferTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        byteBuffer = new ByteArrayBuffer(20);
        swappedBuffer = new SwappedBuffer(byteBuffer);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testReadGet() {
        byteBuffer.setByte(0, (byte) 0xFF);
        assertEquals(0xFF, swappedBuffer.getByte(0) & 0xFF);
        
        byteBuffer.setShort(0, (short) 0x0123);
        assertEquals(0x2301, swappedBuffer.getShort(0) & 0xFFFF);
        
        byteBuffer.setInt(0, 0x01234567);
        assertEquals(0x67452301, swappedBuffer.getInt(0));
        
        byteBuffer.setLong(0, 0x0123456789ABCDEFL);
        assertEquals(0xEFCDAB8967452301L, swappedBuffer.getLong(0));
        
        byteBuffer.setFloat(0, Float.intBitsToFloat(0x47F12072));
        assertEquals(0x7220F147, Float.floatToRawIntBits(swappedBuffer.getFloat(0)));
        
        byteBuffer.setDouble(0, Double.longBitsToDouble(0x47F1207247F12072L));
        assertEquals(0x7220F1477220F147L, Double.doubleToRawLongBits(swappedBuffer.getDouble(0)));
    }
}
