package com.ledungcobra.dto.user.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest implements Serializable {

    private String phoneNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String personalEmail;
    private String profileImageUrl;
    private String personalPhoneNumber;
    @JsonProperty("studentID")
    private String studentID;
    private String currentUser;

}
