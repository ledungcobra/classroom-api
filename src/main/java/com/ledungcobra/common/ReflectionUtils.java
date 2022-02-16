package com.ledungcobra.common;


import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ReflectionUtils {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <I, O> O mapFrom(I input,O ...val) {

        Field[] fields = input.getClass().getDeclaredFields();
        var type = val.getClass().getComponentType();
        var output = (O)type.newInstance();
        Field[] outputFields = output.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(input);
            for (Field outputField : outputFields) {
                if (field.getName().equals(outputField.getName())) {
                    outputField.setAccessible(true);
                    outputField.set(output, value);
                    break;
                }
            }
        }
        return output;
    }
}
