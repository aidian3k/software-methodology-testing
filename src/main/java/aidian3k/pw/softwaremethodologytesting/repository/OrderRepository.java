package aidian3k.pw.softwaremethodologytesting.repository;

import aidian3k.pw.softwaremethodologytesting.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
