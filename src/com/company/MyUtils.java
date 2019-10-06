package com.company;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class MyUtils implements FileVisitor<Path> {
    private static Path sourceDir;
    private static Path destDir;
    private static FileChannel fileChannel;
    private static ByteBuffer buf;


    public static void collectText(Path source, Path dest) {
        sourceDir = source;
        destDir = dest;
        buf = ByteBuffer.allocate(1024);

        MyUtils myutil = new MyUtils();
        try {
            Files.walkFileTree(source, myutil);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes arg1) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes arg1) throws IOException {

        String fileName = path.toString();

        if(fileName.endsWith(".txt")) {

            fileChannel = FileChannel.open(path);
            buf.put(fileName.getBytes());
            buf.put(":\n".getBytes());
            fileChannel.read(buf); //reading bytes from channel and put into bytebuffer
            buf.put("\n".getBytes());
            fileChannel.close();


        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException arg1) throws IOException {
        String failed = path.getFileName().toString() + " is failed to open";
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException arg1) throws IOException {
        File file = new File(destDir.toString());
        FileOutputStream fos = new FileOutputStream(file);
        Charset in = Charset.forName("Cp1250");
        Charset outencode = Charset.forName("UTF-8");
        boolean finishSearch = Files.isSameFile(dir, sourceDir);
        if(finishSearch){
           buf.flip();     //get the buffer
           CharBuffer cbuf = in.decode(buf);
           buf = outencode.encode(cbuf);
           fileChannel = fos.getChannel();
           fileChannel.write(buf);
           fileChannel.close();
            return FileVisitResult.TERMINATE;
        }
        return FileVisitResult.CONTINUE;
    }

}