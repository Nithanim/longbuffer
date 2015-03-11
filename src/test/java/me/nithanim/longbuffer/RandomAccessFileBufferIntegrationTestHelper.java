package me.nithanim.longbuffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class RandomAccessFileBufferIntegrationTestHelper {
    public static byte[] getFileAsByteArray(RandomAccessFile raf) throws IOException {
        byte[] bytes = new byte[(int)raf.length()];
        raf.read(bytes);
        raf.close();
        return bytes;
    }
    
    public static RandomAccessFileBuffer getWriteFileBuffer() throws IOException {
        return new RandomAccessFileBuffer(getWriteFileRaf());
    }
    
    public static RandomAccessFile getWriteFileRaf() throws IOException {
        return new RandomAccessFile(getWriteFile(), "rw");
    }
    
    private static File getWriteFile() {
        return getFile("write.txt");
    }
    
    public static RandomAccessFileBuffer getReadFileBuffer() throws IOException {
        return new RandomAccessFileBuffer(getReadFileRaf());
    }
    
    public static RandomAccessFile getReadFileRaf() throws IOException {
        return new RandomAccessFile(getReadFile(), "r");
    }
    
    private static File getReadFile() {
        return getFile("read.txt");
    }
    
    private static File getFile(String file) {
        return new File(new File(getBaseFolder(), "buffer/"), file);
    }
    
    private static File getBaseFolder() {
        return getResourceAsFile("/");
    }
    
    private static File getResourceAsFile(String file) {
        URL url = RandomAccessFileBufferIntegrationReadTest.class.getResource(file);
        try {
          return new File(url.toURI());
        } catch(URISyntaxException e) {
          return new File(url.getPath());
        }
    }
}
