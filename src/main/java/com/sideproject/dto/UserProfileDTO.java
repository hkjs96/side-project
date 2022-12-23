package com.sideproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    // TODO: 파일 업로드 문서 - 업로드 제한 조정 등 : https://spring.io/guides/gs/uploading-files/
//    private Long id;
//    private User user;
    private String name;
    private String universal;
    private String major;
    private Long studentId;
    private String explain;

    private String projectRole;

    private String photoName;
    private Long photoSize;
    private String photoType;
}
