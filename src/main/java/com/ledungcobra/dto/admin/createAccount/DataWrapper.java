package com.ledungcobra.dto.admin.createAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataWrapper implements Serializable {
    private Boolean hasMore;
    private long total;
    private List<? extends Serializable> data;
}
