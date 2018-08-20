package com.xavier.preview.singleton;

import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Value;

/**
 * 枚举
 *
 * @author NewGr8Player
 */
public enum Singleton {

	INSTANCE;

	@Value("${spring.converter.office-home}")
	private String officePath;

	OfficeManager officeManager;

	Singleton() {
		officeManager = LocalOfficeManager.builder()
				.officeHome(officePath)
				.install()
				.build();
		try {
			officeManager.start();
		} catch (OfficeException e) {
			e.printStackTrace();
		}
	}

	public OfficeManager getInstance() {
		return officeManager;
	}
}
