package com.xavier.preview.service;

import com.xavier.preview.bean.FileResponseData;

public interface ConverterService {

	/**
	 * 文件转换接口
	 *
	 * @param inputPath 文件服务器中文件路径
	 */
	FileResponseData converter(String inputPath);

	/**
	 * 根据原始路径获得预览文件路径
	 *
	 * @param sourcePath 文件服务器中参与预览的文件的路径
	 * @return
	 */
	String getPriviewFilePath(String sourcePath);
}
