package aidian3k.pw.softwaremethodologytesting.repository;

import aidian3k.pw.softwaremethodologytesting.entity.Order;
import aidian3k.pw.softwaremethodologytesting.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	Optional<List<Order>> findByClient(Long clientId);
}
