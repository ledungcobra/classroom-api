package com.ledungcobra.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

    @Lob
    @Column(name = "createby")
    protected String createBy;

    @Lob
    @Column(name = "updateby")
    protected String updateBy;

    @Column(name = "createon")
    protected Instant createOn;

    @Column(name = "updateon")
    protected Instant updateOn;

//    @PrePersist
//    public void preSave() {
//        createOn = Instant.now();
//        updateOn = createOn;
//        updateBy = "";
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        updateOn = Instant.now();
//    }

}
