package org.yah.test.aoc.aoc2018;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import org.yah.test.aoc.utils.IOUtils;


public class IOUtils2018 {

	private static final IOUtils IO_UTILS = new IOUtils("2018/");

	public static String[] readLines(String resource) throws IOException {
		return IO_UTILS.readLines(resource);
	}

	public static Reader openReader(String resource, Charset charset) throws IOException {
		return IO_UTILS.openReader(resource, charset);
	}

	public static InputStream openResource(String resource) throws IOException {
		return IO_UTILS.openResource(resource);
	}

	public static String toString(String resource) throws IOException {
		return IO_UTILS.toString(resource);
	}



}
