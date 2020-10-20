package com.jff.ims.bam_basemanagement.vo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadedFiles {
	private CommonsMultipartFile file;

	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}
}
