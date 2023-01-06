package com.dscatalog.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dscatalog.main.dto.CategoryDTO;
import com.dscatalog.main.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService cs;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		
		return ResponseEntity.ok().body(cs.findAll());
		
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		
		return ResponseEntity.ok().body(cs.findById(id));
	}
}
