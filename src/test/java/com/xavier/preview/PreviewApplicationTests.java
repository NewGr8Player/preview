package com.xavier.preview;

import com.xavier.preview.config.JodConverterConfig;
import org.jodconverter.LocalConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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

	@Test
	public void test02() throws IOException{
		RestTemplate restTemplate = new RestTemplate();
		String url = ("http://192.168.183.38:8081/download/file/sample?filePath=group1%2FM00%2F00%2F02%2FwKi3Jlt6f7iAXE4UAABISTmfiYs50.xlsx");

		downLoadFromUrl(url,"ggggg.xls","D://temp");

	}

	public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(3*1000);
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		InputStream inputStream = conn.getInputStream();
		byte[] getData = readInputStream(inputStream);
		File saveDir = new File(savePath);
		if(!saveDir.exists()){
			saveDir.mkdir();
		}
		File file = new File(saveDir+File.separator+fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if(fos!=null){
			fos.close();
		}
		if(inputStream!=null){
			inputStream.close();
		}
	}

	public static  byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

}
