package com.ledungcobra.dto.admin.postLockAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalAccountRequest implements Serializable {
    private String username;
    private String currentUser;
}
