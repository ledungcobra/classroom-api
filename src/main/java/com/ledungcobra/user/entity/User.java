package com.ledungcobra.user.entity;

import com.ledungcobra.common.Audit;
import com.ledungcobra.common.Provider;
import com.ledungcobra.course.entity.Course;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Audit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "firstname")
    private String firstName;

    @Lob
    @Column(name = "middlename")
    private String middleName;

    @Lob
    @Column(name = "lastname")
    private String lastName;

    @Column(name = "gender", nullable = false)
    private Integer gender;

    @Lob
    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "normalizeddisplayname")
    private String normalizedDisplayName;

    @Lob
    @Column(name = "personalemail")
    private String personalEmail;

    @Column(name = "personalemailconfirmed", nullable = false)
    private Byte personalEmailConfirmed;

    @Lob
    @Column(name = "normalizedpersonalemail")
    private String normalizedPersonalEmail;

    @Lob
    @Column(name = "personalphonenumber")
    private String personalPhoneNumber;

    @Column(name = "userstatus", nullable = false)
    private Integer userStatus;

    @Column(name = "username", length = 256)
    private String userName;

    @Column(name = "normalizedusername", length = 256)
    private String normalizedUserName;

    @Column(name = "email", length = 256)
    private String email;

    @Column(name = "normalizedemail", length = 256)
    private String normalizedEmail;

    @Column(name = "emailconfirmed", nullable = false)
    private Byte emailConfirmed;

    @Lob
    @Column(name = "passwordhash")
    private String passwordHash;

    @Lob
    @Column(name = "securitystamp")
    private String securityStamp;

    @Lob
    @Column(name = "concurrencystamp")
    private String concurrencyStamp;

    @Lob
    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "phonenumberconfirmed", nullable = false)
    private Byte phoneNumberConfirmed;

    @Column(name = "twofactorenabled", nullable = false)
    private Byte twoFactorEnabled;

    @Column(name = "lockoutend")
    private Instant lockoutEnd;

    @Column(name = "lockoutenabled", nullable = false)
    private Byte lockoutEnabled;

    @Column(name = "accessfailedcount", nullable = false)
    private Integer accessFailedCount;

    @Lob
    @Column(name = "studentid")
    private String studentID;

    @Lob
    @Column(name = "profileimageurl")
    private String profileImageUrl;

    @Column(name = "roleaccount", nullable = false)
    private Integer roleAccount;

    @OneToOne
    @JoinColumn(name = "role_id")
    private AppRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

}