package com.dscatalog.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dscatalog.main.entities.Category;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
	
	@GetMapping
	public ResponseEntity<List<Category>> findAll() {
		List<Category> categories = new ArrayList<>();
		
		categories.add(new Category(1L, "Eletronics"));
		categories.add(new Category(2L, "Books"));
		
		return ResponseEntity.ok().body(categories);
		
	}
}
