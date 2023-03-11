package com.example.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadFileService {

	private AmazonS3 s3client;

	private String bucketName = "multipart-uploading0924";

	private AmazonS3 getS3ClientInstance() {
		if (s3client != null)
			return s3client;
		return AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider("longngo0924"))
				.withRegion(Regions.AP_SOUTHEAST_2).build();
	}

	public Map<String, String> uploadFileV1(MultipartFile multipartFile) throws IllegalStateException, IOException {

		Map<String, String> map = new HashMap<>();

		s3client = getS3ClientInstance();

		File file = convertToFile(multipartFile);

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, file.getName(), file);

		long start = System.currentTimeMillis();

		PutObjectResult result = s3client.putObject(putObjectRequest);

		long end = System.currentTimeMillis();
		log.info("Complete Normal Uploading {}s", (end - start) / 1000);

		if (result != null) {
			map.put("fileSize", String.valueOf(multipartFile.getSize() / 1000000) + "MB");
			map.put("time", String.valueOf((end - start) / 1000) + "s");
		} else {
			map.put("message", "Upload Failed");

		}
		return map;
	}

	public Map<String, String> uploadFileV2(MultipartFile multipartFile)
			throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {
		Map<String, String> map = new HashMap<>();
		s3client = getS3ClientInstance();

		File file = convertToFile(multipartFile);

		TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3client)
				.withMultipartUploadThreshold((long) (50 * 1024 * 1025)).build();

		long start = System.currentTimeMillis();
		Upload result = tm.upload(bucketName, file.getName(), file);
		result.waitForCompletion();
		long end = System.currentTimeMillis();
		log.info("Complete Multipart Uploading {}s", (end - start) / 1000);

		map.put("fileSize", String.valueOf(multipartFile.getSize() / 1000000) + "MB");
		map.put("time", String.valueOf((end - start) / 1000) + "s");

		return map;

	}

	private File convertToFile(MultipartFile multipartFile) throws IOException {
		File file = File.createTempFile(multipartFile.getOriginalFilename(), ".pdf");

		FileOutputStream fos = new FileOutputStream(file);
		try {
			fos.write(multipartFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fos.close();
		}
		return file;
	}

}
