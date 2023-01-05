package com.dscatalog.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dscatalog.main.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
