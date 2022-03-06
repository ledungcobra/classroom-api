package com.ledungcobra.dto.course.postUpdateGradeFromFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormDataUpload implements Serializable {
    @JsonProperty("file")
    @NotNull
    private MultipartFile file;
    @JsonProperty("CurrentUser")
    private String currentUser;
}
