/*
 * File: GzipUtil.java
 * ProjectName: FO-API-PKI
 * Description: 
 * 
 * Copyright 2004-2009 99Bill Corporation. All rights reserved.
 * -----------------------------------------------------------
 * Date              Author              Changes
 * 2009-6-30         craig.cheng         created
 */
package com.reed.pay.bill99;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/** 
 * <p>GZIP utility</p> 
 * 
 * 提供压缩，解压缩功能
 * 
 * @version $ Revision: 1.2 $  $ Date: 2009-6-30 上午09:51:11 $
 */
public class GzipUtil {

	public static final String BYTE_ENCODE = "UTF-8";

	/**
	 * gzip压缩字符串
	 * @param str
	 * @return
	 */
	public static byte[] gzip(String str) {
		return gzip(str, BYTE_ENCODE);
	}

	/**
	 * gzip压缩字符串
	 * @param bl
	 * @return
	 */
	public static byte[] gzip(byte[] b1) {
		byte[] b = null;
		ByteArrayOutputStream bo = null;
		GZIPOutputStream gzipo = null;
		try {
			bo = new ByteArrayOutputStream();
			gzipo = new GZIPOutputStream(bo);
			gzipo.write(b1);
			gzipo.finish();
			b = bo.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (gzipo != null)
					gzipo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (bo != null)
					bo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	/**
	 * gzip压缩字符串
	 * @param str
	 * @return
	 */
	public static byte[] gzip(String str, String encode) {
		byte[] b = null;
		ByteArrayOutputStream bo = null;
		GZIPOutputStream gzipo = null;
		try {
			bo = new ByteArrayOutputStream();
			gzipo = new GZIPOutputStream(bo);
			gzipo.write(str.getBytes(encode));
			gzipo.finish();
			b = bo.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (gzipo != null)
					gzipo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (bo != null)
					bo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	/**
	 * @param b
	 * @return
	 */
	public static String unGzip(byte[] b) {
		return unGzip(b, BYTE_ENCODE);
	}

	/**
	 * @param b
	 * @param encode
	 * @return
	 */
	public static String unGzip(byte[] b, String encode) {
		ByteArrayInputStream bi = null;
		GZIPInputStream gzipi = null;
		byte[] bBuf = new byte[4096];
		StringBuffer buf = new StringBuffer();
		try {
			bi = new ByteArrayInputStream(b);
			gzipi = new GZIPInputStream(bi);
			int i = 0;
			while ((i = gzipi.read(bBuf)) != -1) {
				buf.append(new String(bBuf, 0, i, encode));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (gzipi != null)
					gzipi.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (bi != null)
					bi.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new String(buf);
	}

	public static byte[] unBGzip(byte[] b) {
		if (b == null||b.length==0)
			return "".getBytes();
		byte[] retb = null;
		ByteArrayOutputStream bo = null;
		ByteArrayInputStream bi = null;
		GZIPInputStream gzipi = null;
		byte[] bBuf = new byte[4096];
		try {
			bo = new ByteArrayOutputStream();
			bi = new ByteArrayInputStream(b);
			gzipi = new GZIPInputStream(bi);
			int i = 0;
			while ((i = gzipi.read(bBuf)) != -1) {
				//
				bo.write(bBuf, 0, i);
				
			}
			retb = bo.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (gzipi != null)
					gzipi.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (bi != null)
					bi.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (bo != null)
					bo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retb;
	}
	
}
