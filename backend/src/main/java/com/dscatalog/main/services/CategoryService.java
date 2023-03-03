package com.dscatalog.main.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dscatalog.main.dto.CategoryDTO;
import com.dscatalog.main.entities.Category;
import com.dscatalog.main.repositories.CategoryRepository;
import com.dscatalog.main.services.exceptions.ControllerNotFoundException;
import com.dscatalog.main.services.exceptions.DatabaseException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository cr;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
	
		return cr.findAll(pageable).map(x -> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = cr.findById(id);
		
		return new CategoryDTO(obj.orElseThrow(() -> new ControllerNotFoundException("Entity not found "+id)));
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		
		Category category = new Category();
		category.setName(dto.getName());
		
		category = cr.save(category);
		
		return new CategoryDTO(category);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		Category entity = cr.getReferenceById(id);
		entity.setName(dto.getName());
		
		return new CategoryDTO(cr.save(entity));
		
		} catch(EntityNotFoundException e) {
			throw new ControllerNotFoundException("ID not found "+id);
		}
	}

	
	public void delete(Long id) {
		
		try {
			cr.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ControllerNotFoundException("Id not found "+id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
		
		
	}
}
