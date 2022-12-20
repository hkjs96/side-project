package com.sideproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_profile_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "profile_name")
    private String name;
    private String universal;
    private String major;
    private Long studentId;
    @Column(name = "description")
    private String explain;

    // TODO: https://spring.io/guides/gs/uploading-files/
    private String photoName;
    private Long photoSize; // Byte 크기로
    private String photoType;
}

