package org.yah.test.aoc.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class IOUtils {

	private final String path;

	public IOUtils(String path) {
		this.path = path.endsWith("/") ? path : path + "/";
	}

	public String[] readLines(String resource) throws IOException {
		String string = toString(resource);
		return StringUtils.nonEmptyLines(string);
	}

	public Reader openReader(String resource, Charset charset) throws IOException {
		return new InputStreamReader(openResource(resource), charset);
	}

	public InputStream openResource(String resource) throws IOException {
		InputStream is = IOUtils.class.getClassLoader().getResourceAsStream(path + resource);
		if (is == null)
			throw new FileNotFoundException("resource " + resource + " was not found");
		return is;
	}

	public String toString(String resource) throws IOException {
		try (InputStream is = IOUtils.class.getClassLoader().getResourceAsStream(path + resource)) {
			if (is == null)
				throw new FileNotFoundException("resource " + resource + " was not found");
			byte[] buffer = new byte[128 * 1024];
			int read, size = 0;
			StringBuilder sb = new StringBuilder();
			while ((read = is.read(buffer, size, buffer.length - size)) >= 0) {
				size += read;
				if (size == buffer.length) {
					sb.append(new String(buffer, StandardCharsets.US_ASCII));
					size = 0;
				}
			}
			if (size > 0)
				sb.append(new String(buffer, 0, size, StandardCharsets.US_ASCII));

			return sb.toString();
		}
	}

}
