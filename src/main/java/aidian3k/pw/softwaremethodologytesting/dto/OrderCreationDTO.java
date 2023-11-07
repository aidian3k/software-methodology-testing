package aidian3k.pw.softwaremethodologytesting.dto;

import aidian3k.pw.softwaremethodologytesting.domain.Status;
import java.util.List;
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
public class OrderCreationDTO {

	private Long clientId;
	private List<Long> productsIds;
	private Status status;
}
