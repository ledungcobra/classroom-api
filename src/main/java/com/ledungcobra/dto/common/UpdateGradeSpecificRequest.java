package com.ledungcobra.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGradeSpecificRequest implements Serializable {
    private String mssv;
    private Float grade;
    private Boolean isFinalized;
    private String currentUser;
}