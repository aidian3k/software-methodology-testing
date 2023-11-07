package aidian3k.pw.softwaremethodologytesting.infrastructure.exception;

import java.time.LocalDateTime;
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
public class ExceptionResponse {

	private String throwableName;
	private LocalDateTime time;
	private int responseStatus;
}
