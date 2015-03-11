package me.nithanim.longbuffer;

import java.nio.ByteOrder;

public class BufferUtil {
    public static boolean equals(Buffer a, Object b) {
        return b instanceof Buffer ? equals(a, (Buffer)b) : false;
    }
    
    public static boolean equals(Buffer a, Buffer b) {
        long length = a.readableBytes();
        if(length != b.readableBytes()) {
            return false;
        }
        
        boolean reversed = a.order() != b.order();
        long aReaderIndex = a.readerIndex();
        long bReaderIndex = a.readerIndex();
        
        try {
            long remainingBytes = length;
            
            //reading 8byte at onece is more efficient than byte per byte
            while(remainingBytes/8 > 0) {
                if(a.readLong() != (reversed ? Long.reverseBytes(b.readLong()) : b.readLong())) {
                    return false;
                }
                remainingBytes -= 8;
            }
            
            //compare the remaining %8
            for(int i=0; i<remainingBytes; i++) {
                if(a.readByte() != b.readByte()) {
                    return false;
                }
            }
            
            return true;
        } finally {
            //we need to revert the readerIndex in ever case!
            a.readerIndex(aReaderIndex);
            b.readerIndex(bReaderIndex);
        }
    }
    
    public static Buffer ensureByteOrder(Buffer buffer, ByteOrder order) {
        if(buffer.order() == order) {
            return buffer;
        } else {
            if(buffer instanceof SwappedBuffer) {
                return buffer.unwrap();
            } else {
                return new SwappedBuffer(buffer);
            }
        }
    }

    private BufferUtil() {
    }
}
