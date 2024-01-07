/**
 * 
 */
package com.nimesa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimesa.entity.S3BucketEntity;

/**
 * @author hiamm
 *
 */
@Repository
public interface S3ServiceRepository extends JpaRepository<S3BucketEntity, Long> {

	int countByBucketName(String bucketName);

	List<S3BucketEntity> findByBucketName(String bucketName);

	List<S3BucketEntity> findByBucketNameAndFileNameContaining(String bucketName, String pattern);

}
