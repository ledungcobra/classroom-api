package com.ledungcobra.configuration.websocket;

import com.ledungcobra.common.CommonResponse;
import com.ledungcobra.dto.gradereview.getGradeReviewComments.ReviewCommentResponse;
import lombok.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageBody implements Serializable {
    private String channel;
    private Map<String, Object> data;

    public <T extends CommonResponse<? extends Serializable>> MessageBody(T data) {
        this.data = fromObject(data);
    }


    public <T extends ReviewCommentResponse> MessageBody(T data) {
        this.data = fromObject(data);
    }


    @SneakyThrows
    public static Map<String, Object> fromObject(Object o) {
        var result = new HashMap<String, Object>();
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            result.put(field.getName(), field.get(o));
        }
        return result;
    }
}
