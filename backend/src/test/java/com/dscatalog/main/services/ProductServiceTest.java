package com.dscatalog.main.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dscatalog.main.dto.ProductDTO;
import com.dscatalog.main.entities.Category;
import com.dscatalog.main.entities.Product;
import com.dscatalog.main.repositories.CategoryRepository;
import com.dscatalog.main.repositories.ProductRepository;
import com.dscatalog.main.services.exceptions.ControllerNotFoundException;
import com.dscatalog.main.services.exceptions.DatabaseException;
import com.dscatalog.main.tests.Factory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private Long exsistingId;
	private Long nonId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO dto;
	private Category category;
	
	@BeforeEach
	public void setUp() throws Exception {
		exsistingId = 1L;
		nonId = -1L;
		dependentId = 2L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		category = Factory.createCategory();
		dto = Factory.createProductDTO();
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.findById(exsistingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.getReferenceById(exsistingId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getReferenceById(exsistingId)).thenReturn(category);
		Mockito.when(categoryRepository.getReferenceById(nonId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.doNothing().when(repository).deleteById(exsistingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
	}
	
	@Test
	public void findAllPageShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 5);
		
		Page<ProductDTO> results = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(results);
		
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnEntityDtoWhenIdExist() {
		dto = service.findById(exsistingId);
		
		Assertions.assertNotNull(dto);
		
		Mockito.verify(repository, Mockito.times(1)).findById(exsistingId);
	}
	
	@Test
	public void findByIdShouldThrowsControllerNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ControllerNotFoundException.class, () -> {
			service.findById(nonId);
		});
		Mockito.verify(repository, Mockito.times(1)).findById(nonId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExist() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(exsistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(exsistingId);
	}
	
	@Test
	public void deleteShoudlThrowControllerNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ControllerNotFoundException.class, () -> {
			service.delete(nonId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
	
	@Test
	public void updateShouldReturnEntityDtoWhenIdExist() {
		ProductDTO result = service.update(exsistingId, dto);
		
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository).getReferenceById(exsistingId);
	}
	
	@Test
	public void updateShoudlThrowControllerNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ControllerNotFoundException.class, () -> {
			service.update(nonId, dto);
		});
		
		Mockito.verify(repository).getReferenceById(nonId);
		
	}
	
}
