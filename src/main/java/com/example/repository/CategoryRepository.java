package com.example.repository;

import com.example.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<CategoryEntity,Integer> {
    @Query("from CategoryEntity where visible=true order by orderNumber asc")
    List<CategoryEntity> getAll();
}
