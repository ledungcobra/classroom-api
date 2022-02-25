package com.ledungcobra.dto.common;

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
public class DataResponse<T extends Serializable> implements Serializable {
    private List<? extends T> data;
    private Integer total;
}
