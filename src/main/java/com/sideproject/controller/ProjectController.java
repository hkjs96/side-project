package com.sideproject.controller;

import com.sideproject.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProjectController {

    private static ProjectService projectService;

    @Autowired
    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // TODO: 프로젝트 생성 컨트롤러 작성
/*
    @PostMapping("/project")
    public ResponseEntity<?> createProject(@RequestBody)
*/



}

