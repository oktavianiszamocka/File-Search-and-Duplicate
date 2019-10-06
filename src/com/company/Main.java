package com.company;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
	    Path source = Paths.get("D:\\Documents\\PJATK\\4th\\TPO\\source");
	    Path dest = Paths.get("src/result.txt");
	    MyUtils.collectText(source, dest);

		System.out.println("Process is done!");
    }
}
