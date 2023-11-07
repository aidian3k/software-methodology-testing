package aidian3k.pw.softwaremethodologytesting.service;

import aidian3k.pw.softwaremethodologytesting.domain.Status;
import aidian3k.pw.softwaremethodologytesting.dto.OrderCreationDTO;
import aidian3k.pw.softwaremethodologytesting.entity.Client;
import aidian3k.pw.softwaremethodologytesting.entity.Order;
import aidian3k.pw.softwaremethodologytesting.entity.Product;
import aidian3k.pw.softwaremethodologytesting.infrastructure.exception.OrderNotFoundException;
import aidian3k.pw.softwaremethodologytesting.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ClientService clientService;

    public Order createNewOrder(OrderCreationDTO orderCreationDTO) {
        Client client = clientService.getClientById(orderCreationDTO.getClientId());
        List<Product> products = productService.getProductsByIds(orderCreationDTO.getProductsId());
        validateProductAvailability(products);

        Order order = Order
                .builder()
                .client(client)
                .products(products)
                .status(Status.NEW)
                .build();

        client.getOrders().add(order);
        products.stream().map(product -> Product.builder().availability(product.getAvailability() - 1).build())
                .forEach(productService::updateProduct);
        orderRepository.save(order);

        return order;
    }

    public void cancelOrder(Long orderId) {
        Order cancelOrder = getOrderById(orderId);
        Client orderClient = cancelOrder.getClient();
        List<Product> productsOrder = cancelOrder.getProducts();

        productsOrder.stream().map(product -> product.toBuilder().availability(product.getAvailability() + 1).build())
                .forEach(productService::updateProduct);
        orderClient.setOrders(orderClient.getOrders().stream().filter(order -> !order.getId().equals(orderId)).toList());

        clientService.updateClient(orderClient);
        orderRepository.delete(cancelOrder);
    }

    public List<Order> getAllClientOrders(Long clientId) {
        return orderRepository.findByClient(clientId).orElseThrow();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order could not be found!"));
    }

    private void validateProductAvailability(List<Product> products) {
        Optional<Product> productOptional = products
                .stream()
                .filter(product -> product.getAvailability() - 1 < 0)
                .findAny();

        if(productOptional.isPresent()) {
            throw new IllegalStateException("Illegal state exception - there is a product which cannot be bought!");
        }
    }
}
