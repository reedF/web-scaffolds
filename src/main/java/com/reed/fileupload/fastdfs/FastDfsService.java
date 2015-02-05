package com.reed.fileupload.fastdfs;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.csource.fastdfs.TrackerGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Fastdfs service
 * 
 * @author reed
 * 
 */
@Service
public class FastDfsService extends AbstractFastDfsTools {

	private Logger logger = LoggerFactory.getLogger(FastDfsService.class);
	/** fastdfs group,可根据文件访问权限set不同的group */
	@Qualifier("trackerGroup")
	private TrackerGroup trackerGroup;

	public TrackerGroup getTrackerGroup() {
		return trackerGroup;
	}

	public void setTrackerGroup(TrackerGroup trackerGroup) {
		this.trackerGroup = trackerGroup;
	}

	/**
	 * 上传文件至fastdfs
	 * 
	 * @param mf文件
	 * @param watermark水印
	 * @param withname是否显示文件名
	 * @return
	 * @throws IOException
	 */
	public String uploadSinglePic(MultipartFile mf, boolean watermark,
			boolean withname, TrackerGroup trackerGroup, String fileExtName)
			throws IOException {
		String filename = mf.getOriginalFilename().replace(",", "_")
				.replace("&", "_");
		String[] results = uploadToFastdfs(filename, fileExtName, mf.getSize(),
				mf.getInputStream(), watermark, trackerGroup);
		if (results == null || results.length != 2) {
			logger.warn("Fail to upload file, resutl is " + results);
			return null;
		}
		return withFileName(withname, results, filename);
	}

	/**
	 * 上传多张图片 图片格式限制为:FILE_TYPES 图片个数限制为:MAX_COUNT
	 * 
	 * @param mfs文件数组
	 * @param withname是否带上文件名返回
	 * @return
	 */
	public String uploadMultiPic(@RequestParam final MultipartFile[] mfs,
			@RequestParam(value = "false", required = false) boolean withname) {

		String msg = "{\"success\":%b, \"filePath\":\"%s\", \"message\":\"%s\"}";

		if (mfs == null || mfs.length == 0) {
			return String.format(msg, false, null, "未接收到任何文件");
		}
		if (mfs.length > MAX_COUNT) {
			return String.format(msg, false, null, "上传图片不能超过50张");
		}
		String[] files = new String[mfs.length];
		int i = 0;
		for (MultipartFile mf : mfs) {
			String filename = mf.getOriginalFilename().replace(",", "_")
					.replace("&", "_");
			String fileExtName = getFileExtName(mf);
			try {
				if (!this.validateFileExtName(mf, FILE_TYPES, false,
						fileExtName)) {
					return String.format(msg, false, null, "文件格式错误，只支持"
							+ FILE_TYPES);
				}
				String file = uploadSinglePic(mf, false, withname,
						trackerGroup, fileExtName);
				if (StringUtils.isNotBlank(file)) {
					files[i] = file;
					i++;
				} else {
					return String.format(msg, false, null, "文件上传失败，请重试或与管理员联系");
				}
			} catch (IOException e) {
				logger.warn("Fail to upload file, filename is " + filename);
				return String.format(msg, false, null, "文件上传失败，请重试或与管理员联系");
			}
		}
		return String.format(msg, true, StringUtils.join(files, ","), "上传成功");
	}

}
