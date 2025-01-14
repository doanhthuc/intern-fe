package com.mgmtp.easyquizy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDTO {
    @NotEmpty(message = "This is a required field")
    private String currentPassword;

    @NotNull
    private String newPassword;
}
