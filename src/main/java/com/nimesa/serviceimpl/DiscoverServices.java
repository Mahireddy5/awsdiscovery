package com.nimesa.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nimesa.entity.S3BucketEntity;
import com.nimesa.entity.ServiceEntity;
import com.nimesa.repository.S3ServiceRepository;
import com.nimesa.repository.ServiceRepository;
import com.nimesa.service.AmazonEc2Service;
import com.nimesa.service.AmazonS3Service;

@Service
public class DiscoverServices {

	@Autowired
	private AmazonEc2Service ec2Service;

	@Autowired
	private AmazonS3Service s3Service;

	@Autowired
	private ServiceRepository repo;

	@Autowired
	private S3ServiceRepository s3repo;

//	@Autowired
//	private DiscoveryResultRepository discoveryResultRepository;

	@Async
	public String discoverServices(List<String> services) {
		CompletableFuture<Void> ec2Discovery = CompletableFuture.runAsync(() -> {
			List<String> ec2Instances = ec2Service.getEc2InstancesInMumbai();
//			discoveryResultRepository.save(new DiscoveryResult("EC2", ec2Instances));
		});

		CompletableFuture<Void> s3Discovery = CompletableFuture.runAsync(() -> {
			List<String> s3Buckets = s3Service.getS3BucketNamesInMumbai();
//			discoveryResultRepository.save(new DiscoveryResult("S3", s3Buckets));
		});

		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(ec2Discovery, s3Discovery);

		// Wait for both threads to complete
		combinedFuture.join();

		return UUID.randomUUID().toString(); // Generate and return a JobId
	}

	public String getJobResult(String id) {
		Optional<ServiceEntity> isPresent = repo.findById(Long.parseLong(id));
		if (isPresent.isPresent()) {
			ServiceEntity service = isPresent.get();
			return service.getResult().isEmpty() ? "In Progress" : "Success";

		} else {
			return "Failed";
		}
	}

	public String getS3BucketObjects(String bucketName) {
		List<String> fileNames = s3Service.discoverS3BucketObjects(bucketName);
		saveS3BucketObjects(bucketName, fileNames);
		return UUID.randomUUID().toString();
	}

	private void saveS3BucketObjects(String bucketName, List<String> fileNames) {
		List<S3BucketEntity> s3BucketObjects = fileNames.stream()
				.map(fileName -> new S3BucketEntity(bucketName, fileName)).collect(Collectors.toList());
//		repo.saveAll(s3BucketObjects);
	}

	public int getS3BucketObjectCount(String bucketName) {
		return repo.countByBucketName(bucketName);
	}

	public List<String> getS3BucketObjectLike(String bucketName, String pattern) {
		return s3repo.findByBucketNameAndFileNameContaining(bucketName, pattern).stream()
				.map(S3BucketEntity::getFileName).collect(Collectors.toList());
	}

}
