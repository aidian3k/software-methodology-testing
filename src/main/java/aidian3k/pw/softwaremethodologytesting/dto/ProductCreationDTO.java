package aidian3k.pw.softwaremethodologytesting.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductCreationDTO {
    @NotNull
    @Size(max=255)
    private String name;

    @NotNull
    @Positive
    private double price;

    @NotNull
    @Positive
    private int availability;
}
