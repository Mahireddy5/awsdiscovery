package com.nimesa.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimesa.service.AmazonEc2Service;
import com.nimesa.service.AmazonS3Service;
import com.nimesa.serviceimpl.DiscoverServices;

@RestController
@RequestMapping(name = "/service")
public class DiscoverServicesController {

	@Autowired
	private DiscoverServices service;

	@Autowired
	private AmazonEc2Service ec2Service;

	@Autowired
	private AmazonS3Service s3Service;

	@PostMapping(name = "/getJobId")
	public ResponseEntity<String> getJobId(@RequestBody List<String> services) {

		String jobId = service.discoverServices(services);

		return new ResponseEntity<>(jobId, HttpStatus.OK);

	}

	@GetMapping(name = "/jobResult/{jobId}")
	public ResponseEntity<String> getJobResult(@PathVariable String id) {

		String result = service.getJobResult(id);

		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@GetMapping("/discovery/{service}")
	public ResponseEntity<?> getServiceResult(@PathVariable String service) {
		// Use appropriate response type based on the service
		if ("S3".equalsIgnoreCase(service)) {
			List<String> s3Buckets = s3Service.getS3BucketNamesInMumbai();
			return new ResponseEntity<>(s3Buckets, HttpStatus.OK);
		} else if ("EC2".equalsIgnoreCase(service)) {
			List<String> ec2Instances = ec2Service.getEc2InstancesInMumbai();
			return new ResponseEntity<>(ec2Instances, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid service name", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/s3/{bucketName}")
	public ResponseEntity<String> getS3BucketObjects(@PathVariable String bucketName) {
		String jobId = service.getS3BucketObjects(bucketName);
		return new ResponseEntity<>(jobId, HttpStatus.OK);
	}

	@GetMapping("/s3Object/{bucketName}")
	public ResponseEntity<Integer> getS3BucketObjectCount(@PathVariable String bucketName) {
		int count = service.getS3BucketObjectCount(bucketName);
		return new ResponseEntity<>(count, HttpStatus.OK);
	}

	@GetMapping("/s3objects-like/{bucketName}/{pattern}")
	public ResponseEntity<List<String>> getS3BucketObjectLike(@PathVariable String bucketName,
			@PathVariable String pattern) {
		List<String> matchingFiles = service.getS3BucketObjectLike(bucketName, pattern);
		return new ResponseEntity<>(matchingFiles, HttpStatus.OK);
	}
}
