package com.xavier.preview.service.impl;

import com.alibaba.fastjson.JSON;
import com.xavier.preview.bean.FileResponseData;
import com.xavier.preview.config.FileServiceConfig;
import com.xavier.preview.service.ConverterService;
import com.xavier.preview.singleton.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.jodconverter.LocalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class FileServiceConverterService implements ConverterService {

	public static final String POINT = ".";

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private FileServiceConfig fileServiceConfig;


	@Override
	public FileResponseData converter(String inputPath) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			String suffix = getFilenameSuffix(inputPath);
			String filename = "temp" + POINT + suffix;
			String filepath = new File("").getAbsolutePath() + inputPath.replace(POINT + suffix, "");

			downLoadFromUrl(
					fileServiceConfig.baseUrl + fileServiceConfig.downloadUrl + "?filePath=" + inputPath
					, filename
					, filepath);

			final File inputFile = new File(inputPath + File.separator + filename);
			/*(
					fileServiceConfig.baseUrl + fileServiceConfig.downloadUrl + "?filePath=" + inputPath);*/

			final File outputFile = new File(inputPath + ".pdf");

			LocalConverter
					.builder()
					.officeManager(Singleton.INSTANCE.getInstance())
					.build()
					.convert(inputFile)
					.to(outputFile)
					.execute();

			/**
			 * 上传PDF
			 */
			String url = fileServiceConfig.baseUrl + fileServiceConfig.uploadUrl;
			MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
			param.add("file", new FileSystemResource(outputFile));

			String req = restTemplate.postForObject(url, param, String.class);

			FileResponseData fileResponseData = JSON.parseObject(req, FileResponseData.class);
			if (fileResponseData.isSuccess()) {
				redisTemplate.opsForValue().set(inputPath, fileResponseData.getFilePath());
			}

			/* 删除临时文件 */
			outputFile.delete();

			return fileResponseData;
		} catch (Exception e) {
			e.printStackTrace();
			return new FileResponseData(false);
		}
	}

	@Override
	public String getPriviewFilePath(String sourcePath) {
		return redisTemplate.opsForValue().get(sourcePath);
	}

	/**
	 * 从URL下载文件
	 *
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	private static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3 * 1000);
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		InputStream inputStream = conn.getInputStream();
		byte[] getData = readInputStream(inputStream);
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
	}

	/**
	 * 读输入流
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * 获取文件后缀名
	 *
	 * @param filename
	 * @return
	 */
	private String getFilenameSuffix(String filename) {


		String suffix = null;
		if (StringUtils.isNotBlank(filename) && filename.contains(POINT)) {
			suffix = filename.substring(filename.lastIndexOf(POINT) + 1).toLowerCase();
		}
		return suffix;
	}
}
