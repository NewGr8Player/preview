package com.xavier.preview;

import org.jodconverter.LocalConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PreviewApplicationTests {

	@Test
	public void contextLoads() throws OfficeException {
		final File inputFile = new File("E:\\Temp\\a.pptx");
		final File outputFile = new File("E:\\Temp\\a.pdf");

		OfficeManager officeManager = LocalOfficeManager.builder()
				.officeHome("C:\\Program Files\\LibreOffice")
				.install()
				.build();
		officeManager.start();

		LocalConverter
				.builder()
				.officeManager(officeManager)
				.build()
				.convert(inputFile)
				.to(outputFile)
				.execute();

		String url = "http://192.168.183.38:8081/upload/file/sample";
		//http://192.168.183.38:8081/download/file/sample?filePath=group1%2FM00%2F00%2F02%2FwKi3Jlt6WMOAPYmdAAGD9IQPse4060.pdf
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("file", new FileSystemResource(outputFile));

		RestTemplate restTemplate = new RestTemplate();
		String string = restTemplate.postForObject(url, param, String.class);
		System.out.println(string);
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void test02() throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		String a = (String) redisTemplate.opsForValue().get("fdsfadsfasfsadfsa");
		/* get 不存在的key返回null */
		System.out.println(a);
	}


}
