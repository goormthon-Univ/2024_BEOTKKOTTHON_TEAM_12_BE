package com.backend.wear.dto.login;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginRequestDto {
    @NotNull(message = "아이디 입력은 필수입니다.")
    private String loginId;

    @NotNull(message = "패스워드 입력은 필수입니다.")
    private String password;
}