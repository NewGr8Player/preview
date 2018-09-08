package com.xavier.preview.service.impl;

import com.alibaba.fastjson.JSON;
import com.xavier.preview.bean.FileResponseData;
import com.xavier.preview.config.FileServiceConfig;
import com.xavier.preview.service.ConverterService;
import com.xavier.preview.singleton.Singleton;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jodconverter.LocalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URL;

@Service
public class FileServiceConverterService implements ConverterService {

	public static final String POINT = ".";
	public static final String PATH_SEPARATOR = File.separator;
	public static final String TEMP_PATH = "temp";

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private FileServiceConfig fileServiceConfig;

	@Override
	public FileResponseData converter(String inputPath) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			String suffix = FilenameUtils.getExtension(inputPath);
			String filename = inputPath.hashCode() + POINT + suffix;
			String filepath = new File("").getAbsolutePath() + PATH_SEPARATOR + TEMP_PATH + PATH_SEPARATOR + inputPath.hashCode();

			URL url = new URL(fileServiceConfig.baseUrl + fileServiceConfig.downloadUrl + "?filePath=" + inputPath);
			File file = new File(filepath + PATH_SEPARATOR + filename);
			FileUtils.copyURLToFile(url, file);

			final File inputFile = new File(filepath + PATH_SEPARATOR + filename);
			final File outputFile = new File(inputPath + ".pdf");

			LocalConverter
					.builder()
					.officeManager(Singleton.INSTANCE.getInstance())
					.build()
					.convert(inputFile)
					.to(outputFile)
					.execute();

			/* 上传PDF */
			String uploadFullUrl = fileServiceConfig.baseUrl + fileServiceConfig.uploadUrl;
			MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
			param.add("file", new FileSystemResource(outputFile));

			String req = restTemplate.postForObject(uploadFullUrl, param, String.class);

			FileResponseData fileResponseData = JSON.parseObject(req, FileResponseData.class);
			if (fileResponseData.isSuccess()) {
				if (redisTemplate.hasKey(inputPath)) {
					restTemplate.delete(fileServiceConfig.baseUrl
							+ fileServiceConfig.deleteUrl
							+ "?filePath="
							+ redisTemplate.opsForValue().get(inputPath));
				}
				redisTemplate.opsForValue().set(inputPath, fileResponseData.getFilePath());
			}

			/* 删除临时文件 */
			inputFile.delete();
			outputFile.delete();
			/* 删除创建的文件夹 */
			FileUtils.deleteDirectory(new File(filepath));

			return fileResponseData;
		} catch (Exception e) {
			e.printStackTrace();
			return new FileResponseData(false);
		}
	}

	@Override
	public String getPriviewFilePath(String sourcePath) {
		if(!redisTemplate.hasKey(sourcePath)){
			converter(sourcePath);
		}
		return redisTemplate.opsForValue().get(sourcePath);

	}
}
