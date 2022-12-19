package com.sideproject.service;

import com.sideproject.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private static ProjectRepository projectRepository;

    @Autowired
    ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


}
