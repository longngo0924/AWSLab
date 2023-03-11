package com.example.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.example.service.UploadFileService;

@RestController
public class UploadFileController {

	@Autowired
	private UploadFileService uploadFileService;

	@PostMapping("/v1/uploading")
	public Map<String, String> uploadFileV1(@RequestParam MultipartFile file)
			throws IllegalStateException, IOException {

		return uploadFileService.uploadFileV1(file);
	}

	@PostMapping("/v2/uploading")
	public Map<String, String> uploadFileV2(@RequestParam MultipartFile file)
			throws IllegalStateException, IOException,
			AmazonServiceException, AmazonClientException, InterruptedException {

		return uploadFileService.uploadFileV2(file);
	}
}
