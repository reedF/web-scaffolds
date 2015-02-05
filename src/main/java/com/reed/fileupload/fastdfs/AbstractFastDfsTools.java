/**
 * FileUploadController.java
 * Copyright (c) 2013 by lashou.com
 */
package com.reed.fileupload.fastdfs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * fastDfs文件上传&下载,抽象类，mvc下controller可直接继承本类扩展上传下载方法
 * 
 * @author reed
 * 
 */
public abstract class AbstractFastDfsTools {

	/** log */
	private Logger logger = LoggerFactory.getLogger(AbstractFastDfsTools.class);
	/** 单次上传图片不能超过50张 */
	public static final int MAX_COUNT = 50;
	/** 允许上传的文件类型 */
	public static final String FILE_TYPES = "png,gif,jpg,jpeg,rar,zip,7z,doc,docx,xls,xlsx";
	/** 图片加水印后的质量设置 */
	public static final float COMPRESSED_QUALITY = 0.85F;

	/**
	 * upload file to fastdfs
	 * 
	 * @param filename
	 *            file name
	 * @param fileExtName
	 *            file extensence
	 * @param fileLength
	 *            file size
	 * @param in
	 *            InputStream
	 * @param watermark
	 *            是否加水印
	 * @return file Id
	 */
	public String[] uploadToFastdfs(final String filename,
			final String fileExtName, final long fileLength,
			final InputStream in, boolean watermark, TrackerGroup trackerGroup) {
		TrackerClient trackerClient = new TrackerClient(trackerGroup);
		// results[0]: groupName, results[1]: remoteFilename.
		TrackerServer trackerServer = null;
		try {
			byte[] bytes;
			if (watermark) {// 添加水印
				BufferedImage image = ImageIO.read(in);
				BufferedImage overlay = ImageIO.read(this.getClass()
						.getClassLoader().getResource("watermark.png"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Thumbnails.of(image).size(image.getWidth(), image.getHeight())
						.watermark(Positions.CENTER, overlay, 1f)
						.outputQuality(COMPRESSED_QUALITY)
						.outputFormat(fileExtName).toOutputStream(baos);
				bytes = baos.toByteArray();
			} else {
				bytes = IOUtils.toByteArray(in);
			}
			trackerServer = trackerClient.getConnection();
			StorageClient1 storageClient = new StorageClient1(trackerServer,
					null);
			NameValuePair[] metaList = new NameValuePair[] {
					new NameValuePair("fileName", filename),
					new NameValuePair("fileExtName", fileExtName),
					new NameValuePair("fileLength", String.valueOf(fileLength)) };
			return storageClient.upload_file(bytes, fileExtName, metaList);
		} catch (Exception e) {
			logger.info("图片格式错误Unsupported Image Type");
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return null;
	}

	/**
	 * download from fastdfs
	 * 
	 * @param groupName
	 * @param fileName
	 * @param compress是否压缩
	 * @param quality图片质量
	 * @return
	 */
	public byte[] download(String groupName, String fileName, boolean compress,
			float quality, TrackerGroup trackerGroup) {

		String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1)
				.toLowerCase();
		TrackerClient trackerClient = new TrackerClient(trackerGroup);
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerClient.getConnection();
			StorageClient1 storageClient = new StorageClient1(trackerServer,
					null);
			byte[] bytes = storageClient.download_file(groupName, fileName);
			if (!compress
					|| (!fileExtName.equals("jpg") && !fileExtName
							.equals("jpeg"))) {// 只压缩JPG图片
				return bytes;
			}

			if (quality <= 0 || quality >= 1) {// 设置图片质量经验值
				if (bytes.length > 2 * FileUtils.ONE_MB) {// 2M以上只保留18%的质量
					quality = 0.18F;
				} else if (bytes.length > FileUtils.ONE_MB) {// 1M以上只保留25%的质量
					quality = 0.25F;
				} else if (bytes.length >= 500 * FileUtils.ONE_KB) {// 500KB以上只保留35%的质量
					quality = 0.35F;
				} else if (bytes.length >= 200 * FileUtils.ONE_KB) {// 200KB以上只保留55%的质量
					quality = 0.55F;
				} else {// 200KB以下只保留85%的质量
					quality = 0.85F;
				}
			}
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Thumbnails.of(image).size(image.getWidth(), image.getHeight())
					.outputFormat("jpg").outputQuality(quality)
					.toOutputStream(baos);
			return baos.toByteArray();
		} catch (Exception e) {
			logger.warn("download fail");
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return null;
	}

	/**
	 * 根据fastdfs path查询原文件名
	 * 
	 * @param filePath
	 * @param trackerGroup
	 */
	public void getFileName(String[] filePath, TrackerGroup trackerGroup) {
		TrackerClient trackerClient = new TrackerClient(trackerGroup);
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerClient.getConnection();
			StorageClient1 storageClient = new StorageClient1(trackerServer,
					null);
			findName(filePath, storageClient);
		} catch (Exception e) {
			logger.warn("获取图片时发生错误");
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (IOException e) {
					logger.error("关闭fastfs时发生错误", e);
				}
			}
		}

	}

	private void findName(String[] filePath, StorageClient1 storageClient)
			throws IOException, MyException {
		for (int i = 0; i < filePath.length; i++) {
			NameValuePair[] nvp = storageClient.get_metadata1(filePath[i]);
			if (nvp != null && nvp.length > 0) {
				for (int j = 0; j < nvp.length; j++) {
					if (nvp[j] != null && ("fileName").equals(nvp[j].getName())) {
						filePath[i] = filePath[i] + "&" + nvp[j].getValue();
						break;
					}
				}
			}
		}
	}

	/**
	 * 带上文件名返回
	 * 
	 * @param flag
	 *            是否带上文件名
	 * @param results
	 *            上传结果
	 * @param filename
	 *            上传得文件名
	 * @return
	 */
	public String withFileName(Boolean flag, String[] results, String filename) {
		if (!flag) {
			return StringUtils.join(results, "/");
		} else {
			return StringUtils.join(results, "/") + "&" + filename;
		}
	}

	/**
	 * 验证文件扩展文件名
	 * 
	 * @param mf文件
	 * @param type扩展文件名类型
	 * @return
	 * @throws IOException
	 */
	public boolean validateFileExtName(MultipartFile mf, String type,
			boolean isImage, String fileExtName) throws IOException {
		if (StringUtils.isBlank(fileExtName)
				|| !StringUtils.contains(type, fileExtName)) {
			return false;
		}
		if (isImage) {
			ImageInputStream imageStream = ImageIO.createImageInputStream(mf
					.getInputStream());
			Iterator<ImageReader> readers = ImageIO
					.getImageReaders(imageStream);
			if (readers.hasNext()) {
				String formatName = readers.next().getFormatName();
				if (StringUtils.isBlank(formatName)
						|| !StringUtils.containsIgnoreCase(type, formatName)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 校验文件尺寸
	 * 
	 * @param w
	 * @param h
	 * @param image
	 * @return
	 */
	public Boolean isPicSize(int w, int h, BufferedImage image) {
		Boolean wf = (image.getWidth() == w);
		Boolean hf = (image.getHeight() == h);
		if (w == Integer.MAX_VALUE) {
			wf = (image.getWidth() < w);
		}
		if (h == Integer.MAX_VALUE) {
			hf = (image.getHeight() < h);
		}
		return (wf && hf);
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param mf
	 * @return
	 */
	public String getFileExtName(MultipartFile mf) {
		String filename = mf.getOriginalFilename().replace(",", "_")
				.replace("&", "_");
		if (StringUtils.isNotBlank(filename) && filename.contains(".")
				&& !filename.endsWith(".")) {
			return filename.substring(filename.lastIndexOf(".") + 1)
					.toLowerCase();
		}
		return null;
	}
}
