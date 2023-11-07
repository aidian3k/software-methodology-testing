package aidian3k.pw.softwaremethodologytesting.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
