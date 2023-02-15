package com.dscatalog.main.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.dscatalog.main.dto.ProductDTO;
import com.dscatalog.main.repositories.ProductRepository;
import com.dscatalog.main.services.exceptions.ControllerNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	ProductService service;
	
	@Autowired
	ProductRepository repository;
	
	private Long existingId;
	private Long nonId;
	private Long countTotalProducts;
	
	@BeforeEach
	public void setUp() {
		existingId = 1L;
		nonId = -1L;
		countTotalProducts = 25L;
		
	}
	
	@Test
	public void deleteShouldDeleteControllerWhenIdExists() {
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
		
	}
	
	@Test
	public void deleteShouldReturnControllerNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ControllerNotFoundException.class, () -> {
			service.delete(nonId);
		});
	}
	
	@Test
	public void findByIdShouldReturnProductDtoWhenIdExist() {
		ProductDTO dto = service.findById(existingId);
		
		Assertions.assertNotNull(dto);
	}
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());		
	}
}
