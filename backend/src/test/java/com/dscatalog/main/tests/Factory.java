package com.dscatalog.main.tests;

import java.time.Instant;

import com.dscatalog.main.dto.ProductDTO;
import com.dscatalog.main.entities.Category;
import com.dscatalog.main.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Descriçãaaaaaaaaaaaaaaaao", 800D, "Imagem", Instant.parse("2020-08-08T03:00:00Z"));
		product.getCategories().add(createCategory());
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

	public static Category createCategory() {
		return new Category(2L, "Eletronics");
	}
}
