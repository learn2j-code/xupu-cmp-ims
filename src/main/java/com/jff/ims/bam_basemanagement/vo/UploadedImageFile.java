package com.jff.ims.bam_basemanagement.vo;

import org.springframework.web.multipart.MultipartFile;

public class UploadedImageFile {
	MultipartFile image;

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

}
