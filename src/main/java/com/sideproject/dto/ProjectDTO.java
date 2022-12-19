package com.sideproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String topic; /* 주제 */
    private String goal; /* 목표 */
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private List<ProjectRoleDTO> ProjectRoleDTOList = new ArrayList<>();

    // TODO: User 관련 데이터 어떻게 처리?
//    private List<>
}
