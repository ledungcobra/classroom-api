package com.ledungcobra.dto.admin.postChangeStudentId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeStudentIDRequest implements Serializable {
    private String mssv;
    private String username;
    private String currentUser;
}
