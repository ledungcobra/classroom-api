package com.ledungcobra.dto.email.postSendMail;

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
public class SendMailJoinToCourseRequest implements Serializable {
    @NotNull
    private Integer role;
    @NotBlank
    @NotNull
    private String mailPersonReceive;
    @NotNull
    private String classCode;
}
