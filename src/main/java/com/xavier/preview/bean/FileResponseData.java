package com.xavier.preview.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 上传文件后的数据返回对象，便于前台获取数据.
 *
 * @author NewGr8Player
 */
@Getter
@Setter
@NoArgsConstructor
public class FileResponseData {

	/**
	 * 返回状态编码
	 */
	@JsonInclude(Include.NON_NULL)
	private String code;

	/**
	 * 返回信息
	 */
	@JsonInclude(Include.NON_NULL)
	private String message;

	/**
	 * 成功标识
	 */
	@JsonInclude
	private boolean success = true;

	/**
	 * 文件路径
	 */
	@JsonInclude(Include.NON_NULL)
	private String filePath;

	/**
	 * 文件名称
	 */
	@JsonInclude(Include.NON_NULL)
	private String fileName;

	/**
	 * 文件类型
	 */
	@JsonInclude(Include.NON_NULL)
	private String fileType;

	/**
	 * 文件大小(单位:B)
	 */
	@JsonInclude(Include.NON_NULL)
	private Long fileSize;

	/**
	 * 时间
	 */
	@JsonInclude(Include.NON_NULL)
	private String respDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	public FileResponseData(boolean success) {
		this.success = success;
	}
}

