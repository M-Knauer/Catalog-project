package com.dscatalog.main.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dscatalog.main.dto.CategoryDTO;
import com.dscatalog.main.entities.Category;
import com.dscatalog.main.repositories.CategoryRepository;
import com.dscatalog.main.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository cr;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {	
		return cr.findAll().stream()
				.map(x -> new CategoryDTO(x))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = cr.findById(id);
		
		return new CategoryDTO(obj.orElseThrow(() -> new EntityNotFoundException("Entity not found")));
	}
}
