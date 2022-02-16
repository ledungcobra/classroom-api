package com.ledungcobra.common;

import lombok.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ObjectA {
    private int x;
    private String z;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ObjectB {
    private int x;
    private int y;
    public String c;
    public String z;
}

public class ReflectionUtilsTest {

    @Test
    public void mappingTestMapping() {
        var objectA = ObjectA.builder()
                .x(10)
                .z("#)")
                .build();
        ObjectB objectB = ReflectionUtils.mapFrom(objectA);
        Assertions.assertThat(objectB).isNotNull();
        Assertions.assertThat(objectB.getX()).isEqualTo(objectA.getX());
        Assertions.assertThat(objectB.getZ()).isEqualTo(objectA.getZ());
        Assertions.assertThat(objectB.getC()).isNull();
    }
}