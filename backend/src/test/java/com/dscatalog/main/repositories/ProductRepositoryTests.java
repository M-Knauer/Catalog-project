package com.dscatalog.main.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.dscatalog.main.entities.Product;
import com.dscatalog.main.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private Long exsistingId;
	private Long nonId;
	
	@BeforeEach
	public void setUp() throws Exception {
		exsistingId = 1L;
		nonId = -1L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExist() {
		
		// Act
		repository.deleteById(exsistingId);
		
		// Assert
		Assertions.assertFalse(repository.existsById(exsistingId));
	}
	
	@Test
	public void deleteShouldThrowsEmptyResultDataAccessExceptionsWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonId);
			
		});
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		// Arrange
		Product product = Factory.createProduct();
		product.setId(null);
		
		// Act
		product = repository.save(product);
		
		// Assert
		Assertions.assertNotNull(product.getId());
	}
	
	@Test
	public void findByIdShouldReturnNotNullOptionalWhenIdExist() {
		// Arrange
		Optional<Product> product;
		
		// Act
		product = repository.findById(exsistingId);
		
		// Assert
		Assertions.assertNotNull(product.get());
	}
	
	@Test
	public void findByIdShouldReturnNullWhenIdDoesNotExist() {
		// Arrange
		Optional<Product> product;
		
		// Act
		product = repository.findById(nonId);
		
		// Assert
		Assertions.assertFalse(product.isPresent());
	}
}
