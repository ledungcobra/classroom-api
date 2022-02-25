package com.ledungcobra.dto.email.postSendMail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMailJoinToCourseRequest implements Serializable {
    private Integer role;
    private String mailPersonReceive;
    private String classCode;
}
