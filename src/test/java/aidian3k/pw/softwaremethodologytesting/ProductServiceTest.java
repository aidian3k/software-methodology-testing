package aidian3k.pw.softwaremethodologytesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aidian3k.pw.softwaremethodologytesting.dto.ProductCreationDTO;
import aidian3k.pw.softwaremethodologytesting.entity.Product;
import aidian3k.pw.softwaremethodologytesting.infrastructure.exception.ProductNotFoundException;
import aidian3k.pw.softwaremethodologytesting.service.ProductService;
import java.util.List;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

	private final Logger log = Logger.getLogger(
		ProductServiceTest.class.getName()
	);

	@Autowired
	private ProductService productService;

	@Test
	void shouldCreateNewProductsCorrectlyWithoutProblems() {
		// Given
		ProductCreationDTO productCreationDTO = new ProductCreationDTO(
			"Test Product",
			29.99,
			30
		);

		// When
		Product createdProduct = productService.createNewProduct(
			productCreationDTO
		);

		// Then
		assertThat(createdProduct).isNotNull();
		assertThat(createdProduct.getId()).isNotNull();
		assertThat(createdProduct.getName())
			.isEqualTo(productCreationDTO.getName());
		assertThat(createdProduct.getAvailability())
			.isEqualTo(productCreationDTO.getAvailability());
		assertThat(createdProduct.getPrice())
			.isEqualTo(productCreationDTO.getPrice());
	}

	@Test
	void shouldCorrectlyGetNewProductById() {
		// Given
		log.info("Testing getting product by id");
		ProductCreationDTO productCreationDTO = new ProductCreationDTO(
			"Test Product",
			10,
			30
		);
		Product createdProduct = productService.createNewProduct(
			productCreationDTO
		);

		// When
		Product retrievedProduct = productService.getProductById(
			createdProduct.getId()
		);
		log.info("Retrieved product with id=" + retrievedProduct.getId());

		// Then
		assertThat(retrievedProduct).isNotNull();
		assertThat(createdProduct.getId()).isEqualTo(retrievedProduct.getId());
		assertThat(createdProduct.getName()).isEqualTo(retrievedProduct.getName());
		assertThat(createdProduct.getAvailability())
			.isEqualTo(retrievedProduct.getAvailability());
		assertThat(createdProduct.getPrice())
			.isEqualTo(retrievedProduct.getPrice());
	}

	@Test
	void shouldCorrectlyUpdateExistingProduct() {
		// Given
		ProductCreationDTO productCreationDTO = new ProductCreationDTO(
			"Test Product",
			10,
			20
		);
		Product createdProduct = productService.createNewProduct(
			productCreationDTO
		);

		// When
		Product updatedProduct = productService.updateProductById(
			createdProduct.getId(),
			new ProductCreationDTO("Updated Product", 5, 20)
		);
		Product productAfterUpdate = productService.getProductById(
			updatedProduct.getId()
		);
		log.info("Product after update has name=" + productAfterUpdate.getName());

		// Then
		assertThat(updatedProduct).isNotNull();
		assertEquals(updatedProduct.getId(), productAfterUpdate.getId());
		assertThat(updatedProduct.getName())
			.isEqualTo(productAfterUpdate.getName());
		assertThat(updatedProduct.getAvailability())
			.isEqualTo(productAfterUpdate.getAvailability());
		assertThat(updatedProduct.getPrice())
			.isEqualTo(productAfterUpdate.getPrice());
	}

	@Test
	void shouldCorrectlyGetProductsByIds() {
		// Given
		ProductCreationDTO product1 = new ProductCreationDTO("Product 1", 10, 30);
		ProductCreationDTO product2 = new ProductCreationDTO("Product 2", 5, 20);
		Product createdProduct1 = productService.createNewProduct(product1);
		Product createdProduct2 = productService.createNewProduct(product2);

		// When
		List<Product> products = productService.getProductsByIds(
			List.of(createdProduct1.getId(), createdProduct2.getId())
		);

		// Then
		assertThat(products).hasSize(2);
		assertThat(products).extracting("name").contains("Product 1", "Product 2");
	}

	@Test
	void shouldThrowExceptionWhenTryingToFindNonExistingValue() {
		// Given a non-existing product ID
		Long nonExistentProductId = 9999L;

		// When trying to get the product by the non-existing ID
		// Then an exception should be thrown
		assertThrows(
			ProductNotFoundException.class,
			() -> {
				productService.getProductById(nonExistentProductId);
			}
		);
	}

	@Test
	void shouldThrowAnExceptionWhenTryingToUpdateNonExistingService() {
		// Given
		Long nonExistentProductId = 9999L;
		ProductCreationDTO updateDTO = new ProductCreationDTO(
			"Updated Product",
			5,
			20
		);

		// When trying to update the product by the non-existing ID
		// Then an exception should be thrown
		assertThrows(
			ProductNotFoundException.class,
			() -> {
				productService.updateProductById(nonExistentProductId, updateDTO);
			}
		);
	}

	@Test
	void shouldThrowAnExceptionWhenTryingToDeleteNonExistingProduct() {
		// Given a non-existing product ID
		Long nonExistentProductId = 9999L;

		// When trying to delete the product by the non-existing ID
		productService.deleteProductById(nonExistentProductId);

		// Check if the product was successfully deleted
		assertThrows(
			ProductNotFoundException.class,
			() -> {
				productService.getProductById(nonExistentProductId);
			}
		);
	}
}
