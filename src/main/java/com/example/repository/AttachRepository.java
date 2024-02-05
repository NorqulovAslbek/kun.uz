package com.example.repository;

import com.example.entity.AttachEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AttachRepository extends CrudRepository<AttachEntity, String> , PagingAndSortingRepository<AttachEntity, String> {

}
