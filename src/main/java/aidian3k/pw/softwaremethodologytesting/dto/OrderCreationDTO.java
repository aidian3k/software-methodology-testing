package aidian3k.pw.softwaremethodologytesting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderCreationDTO {
    private Long clientId;
    private List<Long> productsId;
}
