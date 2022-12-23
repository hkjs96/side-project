package com.sideproject.controller;

import com.sideproject.dto.CreateProjectDTO;
import com.sideproject.dto.ResponseDTO;
import com.sideproject.entity.User;
import com.sideproject.service.ProjectService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProjectController {

    private static ProjectService projectService;

    @Autowired
    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/project")
    @ApiOperation(value = "프로젝트 생성", notes = "프로젝트 생성")
    public ResponseEntity<?> createProject(
            @RequestBody CreateProjectDTO createProjectDTO,
            @ApiParam(name = "파일", required = true) @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user
    ) {
        try {
            projectService.createProject(createProjectDTO, file, user);

            ResponseDTO responseDTO = ResponseDTO.builder()
                    .data((createProjectDTO))
                    .build();

            return ResponseEntity
                    .ok()
                    .body(responseDTO);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("잘못된 요청입니다.")
                    .data(createProjectDTO)
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
}

