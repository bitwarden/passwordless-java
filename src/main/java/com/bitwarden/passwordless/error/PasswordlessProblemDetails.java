package com.bitwarden.passwordless.error;

//import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Builder
public class PasswordlessProblemDetails {
//    @NotBlank
    String type;
    String title;
    Integer status;
    String detail;
    String instance;
}
