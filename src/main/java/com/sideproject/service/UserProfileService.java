package com.sideproject.service;

import com.sideproject.dto.UserProfileDTO;
import com.sideproject.entity.User;
import com.sideproject.entity.UserProfile;
import com.sideproject.properties.StorageProperties;
import com.sideproject.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@EnableConfigurationProperties(StorageProperties.class)
public class UserProfileService {

    private static FileSystemStorageService fileSystemStorageService;
    private static UserProfileRepository userProfileRepository;

    @Autowired
    UserProfileService(UserProfileRepository userProfileRepository, FileSystemStorageService fileSystemStorageService) {
        this.userProfileRepository = userProfileRepository;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @Transactional
    public UserProfile createProfile(UserProfileDTO userProfileDto, User user) {
        String photoName = UUID.randomUUID().toString();

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .name(userProfileDto.getName())
                .universal(userProfileDto.getUniversal())
                .studentId(userProfileDto.getStudentId())
                .major(userProfileDto.getMajor())
                .explain(userProfileDto.getExplain())
                .photoName(photoName)
                .photoOriginalName(userProfileDto.getPhotoName())
                .photoType(userProfileDto.getPhotoType())
                .photoSize(userProfileDto.getPhotoSize())
                .build();

        return userProfileRepository.save(userProfile);
    }

    public String saveProfileImage(MultipartFile file, String photoName) {
        return fileSystemStorageService.store(file, photoName);
    }

}
