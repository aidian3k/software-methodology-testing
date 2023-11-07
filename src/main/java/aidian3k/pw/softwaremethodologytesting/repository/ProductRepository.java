package aidian3k.pw.softwaremethodologytesting.repository;

import aidian3k.pw.softwaremethodologytesting.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query("select p from products p where p.id in :productIds")
	Optional<List<Product>> findAllByIds(List<Long> productIds);
}
