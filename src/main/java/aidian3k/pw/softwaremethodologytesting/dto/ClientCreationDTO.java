package aidian3k.pw.softwaremethodologytesting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class ClientCreationDTO {
    @NotNull
    @Size(max=255)
    private String name;

    @NotNull
    @Size(max=255)
    private String surname;

    @Email
    private String email;
}
