package aidian3k.pw.softwaremethodologytesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import aidian3k.pw.softwaremethodologytesting.domain.Status;
import aidian3k.pw.softwaremethodologytesting.dto.OrderCreationDTO;
import aidian3k.pw.softwaremethodologytesting.entity.Client;
import aidian3k.pw.softwaremethodologytesting.entity.Order;
import aidian3k.pw.softwaremethodologytesting.entity.Product;
import aidian3k.pw.softwaremethodologytesting.repository.OrderRepository;
import aidian3k.pw.softwaremethodologytesting.service.ClientService;
import aidian3k.pw.softwaremethodologytesting.service.OrderService;
import aidian3k.pw.softwaremethodologytesting.service.ProductService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	private final Logger log = Logger.getLogger(OrderServiceTest.class.getName());

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductService productService;

	@Mock
	private ClientService clientService;

	private OrderService orderService;

	@BeforeEach
	void setUpTest() {
		orderService =
			new OrderService(orderRepository, productService, clientService);
	}

	@Test
	@DisplayName("Successful mocking cancel order")
	void shouldSuccessfullyDeleteTheOrderWhenDataIsCorrect() {
		// Given
		Long orderId = 1L;
		Client client = Client
			.builder()
			.orders(List.of(Order.builder().id(1L).build()))
			.build();
		Order cancelOrder = Order
			.builder()
			.client(client)
			.products(
				List.of(
					Product.builder().id(1L).build(),
					Product.builder().id(2L).build()
				)
			)
			.build();

		log.info("Stubbing the methods in the cancel order method");
		when(orderRepository.findById(orderId))
			.thenReturn(Optional.of(cancelOrder));
		when(productService.updateProduct(any(Product.class)))
			.thenReturn(new Product());
		when(clientService.updateClient(any(Client.class))).thenReturn(client);

		doNothing().when(orderRepository).delete(any(Order.class));

		// When
		orderService.cancelOrder(orderId);
		log.info("Canceled order in test");

		// Then
		verify(productService, times(2)).updateProduct(any(Product.class));
		verify(clientService, times(1)).updateClient(any(Client.class));
		verify(orderRepository, times(1)).delete(any(Order.class));
	}

	@Test
	@DisplayName("Good scenario of creating")
	void shouldCorrectlyCreateNewOrderWhenAvailabilityIsOkay() {
		// Given
		Long clientId = 1L;
		List<Long> productIds = Arrays.asList(1L, 2L, 3L);
		Client client = Client.builder().id(clientId).name("John Doe").build();
		List<Product> products = Arrays.asList(
			Product.builder().id(1L).name("Product 1").availability(1).build(),
			Product.builder().id(2L).name("Product 2").availability(2).build(),
			Product.builder().id(3L).name("Product 3").availability(100).build()
		);

		when(clientService.getClientById(clientId)).thenReturn(client);
		when(productService.getProductsByIds(productIds)).thenReturn(products);

		// Then
		OrderCreationDTO orderCreationDTO = new OrderCreationDTO(
			clientId,
			productIds,
			Status.NEW
		);
		Order createdOrder = orderService.createNewOrder(orderCreationDTO);

		verify(orderRepository, times(1)).save(any(Order.class));

		Assertions.assertAll(
			() -> assertThat(createdOrder.getStatus()).isEqualTo(Status.NEW),
			() -> assertThat(createdOrder.getProducts()).hasSize(products.size()),
			() ->
				assertThat(createdOrder.getClient().getName())
					.isEqualTo(client.getName())
		);
	}

	@Test
	@DisplayName("Bad scenario of creating order of products")
	void shouldThrowAnExceptionWhenProductAvailabilityIsLessThanOne() {
		// Given
		Long clientId = 1L;
		List<Long> productIds = Arrays.asList(1L, 2L, 3L);
		Client client = Client.builder().id(clientId).name("John Doe").build();
		List<Product> products = Arrays.asList(
			Product.builder().id(1L).name("Product 1").availability(0).build(),
			Product.builder().id(2L).name("Product 2").availability(1).build(),
			Product.builder().id(3L).name("Product 3").availability(-2).build()
		);

		when(clientService.getClientById(clientId)).thenReturn(client);
		when(productService.getProductsByIds(productIds)).thenReturn(products);

		// Then
		OrderCreationDTO orderCreationDTO = new OrderCreationDTO(
			clientId,
			productIds,
			Status.NEW
		);
		assertThatException()
			.isThrownBy(() -> orderService.createNewOrder(orderCreationDTO))
			.isInstanceOf(IllegalStateException.class)
			.withMessage(
				"Illegal state exception - there is a product which cannot be bought!"
			);
	}
}
