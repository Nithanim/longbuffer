# longbuffer
This library is like a clone of the buffer library of netty with the exception that it uses long for positions and lengths. The motivation behind this was to be able to use the same method names and arguments when dealing with larger structures. I needed to access a RandomAccessFile (I needed to get away from memory mapped files in java) but I had already implemented my application with NettyBuffers so this was not possible. 

It is still a very basic implementation and a lot of methods that netty is providing with their buffers are missing. Furthermore there still might be some bugs in it so use it with care!

This is an example to read from a RandomAccessFile:
```java
Buffer buffer = new RandomAccessFileBuffer(new RandomAccessFile(file, "r")).order(ByteOrder.LITTLE_ENDIAN);
int dirCount = buffer.readInt();
buffer.dispose(); //closes the RandomAccessFile automatically
```


## License
This library is released under the Apache License Version 2.0.

Thanks to [netty](http://netty.io/) for their aweseome buffer library, which was and still is the base for this project.