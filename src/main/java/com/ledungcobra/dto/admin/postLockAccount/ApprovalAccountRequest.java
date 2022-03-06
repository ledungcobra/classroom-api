package com.ledungcobra.dto.admin.postLockAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalAccountRequest implements Serializable {

    @NotNull
    @NotBlank
    private String username;

    private String currentUser;

}
