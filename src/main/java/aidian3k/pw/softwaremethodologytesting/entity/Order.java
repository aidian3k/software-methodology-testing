package aidian3k.pw.softwaremethodologytesting.entity;

import aidian3k.pw.softwaremethodologytesting.domain.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	private Client client;

	@OneToMany(
		fetch = FetchType.LAZY,
		mappedBy = "order",
		cascade = { CascadeType.PERSIST }
	)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private List<Product> products = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private Status status;
}
