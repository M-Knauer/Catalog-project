package com.dscatalog.main.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dscatalog.main.dto.ProductDTO;
import com.dscatalog.main.entities.Product;
import com.dscatalog.main.repositories.CategoryRepository;
import com.dscatalog.main.repositories.ProductRepository;
import com.dscatalog.main.services.exceptions.ControllerNotFoundException;
import com.dscatalog.main.services.exceptions.DatabaseException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
	
		return repository.findAll(pageable).map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ControllerNotFoundException("Entity not found "+id));
		
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		entity = toEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
		Product entity = repository.getReferenceById(id);
	
		return new ProductDTO(repository.save(toEntity(dto, entity)));
		
		} catch(EntityNotFoundException e) {
			throw new ControllerNotFoundException("ID not found "+id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		
		try {
			repository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ControllerNotFoundException("Id not found "+id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}	
	}
	
	private Product toEntity(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName()); 
		entity.setDescription(dto.getDescription()); 
		entity.setPrice(dto.getPrice()); 
		entity.setImgUrl(dto.getImgUrl()); 
		entity.setDate(dto.getDate());
		
		entity.getCategories().clear();
		dto.getCategories().forEach(cat -> {
			entity.getCategories().add(
					categoryRepository.getReferenceById(cat.getId()));
		});
		
		return entity;
	}
}
