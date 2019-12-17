package com.stylopay.controller;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class KYCFileUpload {
	
	private List<MultipartFile> files;

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
	
	@Override
    public String toString()
    {
        return "Files [files=" + files + "]";
    }
}
