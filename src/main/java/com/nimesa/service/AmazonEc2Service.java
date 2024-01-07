package com.nimesa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;

@Service
public class AmazonEc2Service {

	@Value("${aws.keyId}")
	private String keyId;
	@Value("${aws.secretKey}")
	private String secretKey;

	public List<String> getEc2InstancesInMumbai() {
		Ec2Client client = Ec2Client.builder().region(Region.AP_SOUTH_1)
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(keyId, secretKey)))
				.build();
		DescribeInstancesResponse ec2Res = client.describeInstances();
		List<String> instancs = ec2Res.reservations().stream().flatMap(reserve -> reserve.instances().stream())
				.map(software.amazon.awssdk.services.ec2.model.Instance::instanceId).collect(Collectors.toList());
		return instancs;
	}

}
