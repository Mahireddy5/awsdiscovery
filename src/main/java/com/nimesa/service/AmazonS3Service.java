package com.nimesa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
public class AmazonS3Service {

	@Value("${aws.keyId}")
	private String keyId;
	@Value("${aws.secretKey}")
	private String secretKey;

	public List<String> getS3BucketNamesInMumbai() {
		S3Client client = S3Client.builder().region(Region.AP_SOUTH_1)
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(keyId, secretKey)))
				.build();
		ListBucketsResponse s3Res = client.listBuckets();
		return s3Res.buckets().stream().map(Bucket::name).collect(Collectors.toList());
	}
	
	public List<String> discoverS3BucketObjects(String bucketName) {
		S3Client s3Client = S3Client.builder().region(Region.AP_SOUTH_1) 
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(keyId, secretKey)))
				.build();

		ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder().bucket(bucketName).build();

		ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

		return listObjectsResponse.contents().stream().map(S3Object::key).collect(Collectors.toList());
	}


}
