package com.wy.test.crypto;

import java.io.File;
import java.io.FileNotFoundException;

public class Md5SumTest {

	public Md5SumTest() {
	}

	public static void main(String[] args) throws FileNotFoundException {
		// String md5value=Md5Sum.produce(new
		// File("E:/transwarp-4.3.4-Final-el6/transwarp-4.3.4-Final-26854-zh.el6.x86_64.tar.gz"));
		File f = new File("E:/Soft/Xmanager4_setup.1410342608.exe");
		String md5value = Md5Sum.produce(f);

		System.out.println("" + md5value);

		System.out.println(Md5Sum.check(f, md5value));
	}

}
