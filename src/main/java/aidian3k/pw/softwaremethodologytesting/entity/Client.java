package aidian3k.pw.softwaremethodologytesting.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_seq")
    @SequenceGenerator(name = "clients_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Size(max=255)
    private String name;

    @NotNull
    @Size(max=255)
    private String surname;

    @NotNull
    @Size(max=255)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private List<Order> orders = new ArrayList<>();
}
