/**
 * 
 */
package com.nimesa.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimesa.entity.ServiceEntity;

/**
 * @author hiamm
 *
 */
@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long>{

	int countByBucketName(String bucketName);


}
