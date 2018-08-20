package com.xavier.preview.api;

import com.xavier.preview.bean.FileResponseData;
import com.xavier.preview.service.ConverterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class ConverterController {

	@Autowired
	private ConverterService converterService;

	@ApiOperation(value = "转换office")
	@GetMapping(path = "/convert/file/sample")
	public FileResponseData converter(
			@ApiParam(name = "filePath", value = "路径(groupName/path)", required = true)
			@RequestParam(name = "filePath")
					String filePath) {
		return converterService.converter(filePath);
	}

	@ApiOperation(value = "获取预览文件路径")
	@GetMapping(path = "/convert/file/getPriviewFilePath")
	public String getPriviewFilePath(
			@ApiParam(name = "filePath", value = "路径(groupName/path)", required = true)
			@RequestParam(name = "filePath")
					String filePath) {
		return this.converterService.getPriviewFilePath(filePath);
	}
}
