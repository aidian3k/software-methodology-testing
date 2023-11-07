package aidian3k.pw.softwaremethodologytesting.service;

import aidian3k.pw.softwaremethodologytesting.dto.ProductCreationDTO;
import aidian3k.pw.softwaremethodologytesting.entity.Product;
import aidian3k.pw.softwaremethodologytesting.infrastructure.exception.ProductNotFoundException;
import aidian3k.pw.softwaremethodologytesting.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;

	public Product createNewProduct(ProductCreationDTO creationDTO) {
		Product newProduct = Product
			.builder()
			.name(creationDTO.getName())
			.availability(creationDTO.getAvailability())
			.price(creationDTO.getPrice())
			.build();

		return productRepository.save(newProduct);
	}

	public List<Product> getAllClients() {
		return productRepository.findAll();
	}

	public Product getProductById(Long productId) {
		return productRepository
			.findById(productId)
			.orElseThrow(() ->
				new ProductNotFoundException(
					String.format("Product with id=[%d] could not be found", productId)
				)
			);
	}

	public Product updateProductById(
		Long productId,
		ProductCreationDTO creationDTO
	) {
		Product currentClient = getProductById(productId);
		Product updatedClient = currentClient
			.toBuilder()
			.name(creationDTO.getName())
			.availability(creationDTO.getAvailability())
			.price(creationDTO.getPrice())
			.build();

		return productRepository.save(updatedClient);
	}

	public Product updateProduct(Product updatedProduct) {
		return productRepository.save(updatedProduct);
	}

	public List<Product> getProductsByIds(List<Long> productIds) {
		return productRepository
			.findAllByIds(productIds)
			.orElseThrow(() ->
				new ProductNotFoundException("Products could not be found!")
			);
	}
}
