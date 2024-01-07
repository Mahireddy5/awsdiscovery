package com.nimesa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class S3BucketEntity {

	@Id
	private Long id;

	private String bucketName;
	private String fileName;

	public S3BucketEntity(String bucketName, String fileName) {
		this.bucketName = bucketName;
		this.fileName = fileName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
