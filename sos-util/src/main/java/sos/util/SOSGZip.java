package sos.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * <p>Title: </p>
 * <p>Description: Java GZip client</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @version $Id$
 */

public class SOSGZip {

	/**
	 * @param inputFile
	 * @param outputFile
	 * @exception FileNotFoundException, IOException
	 */
	public static void compressFile(File inputFile, File outputFile) throws Exception {

		BufferedInputStream in = null;
		GZIPOutputStream out = null;

		try {
			in = new BufferedInputStream(new FileInputStream(inputFile));
			out = new GZIPOutputStream(new FileOutputStream(outputFile));

			byte buffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}

		}
		catch (Exception e) {
			throw e;
		}
		finally {
			try {
				in.close();
			}
			catch (Exception e) {
			}
			try {
				out.flush();
				out.close();
			}
			catch (Exception e) {
			}
		}
	}

	/**
	 * @param fileInputStream
	 * @param outputFile
	 * @exception FileNotFoundException, IOException
	 */
	public static void compressFile(FileInputStream fileInputStream, File outputFile) throws Exception {

		BufferedInputStream in = null;
		GZIPOutputStream out = null;
		try {

			in = new BufferedInputStream(fileInputStream);

			out = new GZIPOutputStream(new FileOutputStream(outputFile));

			byte buffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			try {
				in.close();
			}
			catch (Exception e) {
			}
			try {
				out.close();
			}
			catch (Exception e) {
			}
		}
	}

	/**
	 * Decomprimiert die Archivdatei
	 * @param inFilename
	 * @throws Exception
	 */
	public static void decompress(String inFilename) throws Exception {
		decompress(inFilename, null);
	}

	/**
	 * Decomprimiert die Archivdatei
	 * @param inFilename
	 * @param outFilename
	 * @throws Exception
	 */
	public static void decompress(String inFilename, String outFilename) throws Exception {
		try {
			if (outFilename == null) {
				outFilename = inFilename.substring(0, inFilename.lastIndexOf("."));
			}

			GZIPInputStream in = new GZIPInputStream(new FileInputStream(inFilename));

			java.io.OutputStream out = new FileOutputStream(outFilename);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();
		}
		catch (Exception e) {
			throw e;
		}

	}

	public static void main(String[] args) {
		try {
			compressFile(new File("C:/temp/a.txt"), new File("C:/temp/a.gz"));

			//decompress("C:/temp/a/RDN_20070511_000001.tar.gz", null);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}
