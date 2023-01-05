package com.dscatalog.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dscatalog.main.entities.Category;
import com.dscatalog.main.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository cr;
	
	public List<Category> findAll() {
		
		return cr.findAll();
	}
}
