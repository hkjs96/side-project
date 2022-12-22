package com.sideproject.service;

import com.sideproject.exception.StorageException;
import com.sideproject.properties.StorageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSystemStorageServiceTests {

    private StorageProperties properties = new StorageProperties();
    private FileSystemStorageService service;

    @BeforeEach
    public void init() {
        properties.setLocation("target/files/" + Math.abs(new Random().nextLong()));
        service = new FileSystemStorageService(properties);
        service.init();
    }

    @Test
    @DisplayName("존재하지 않는 파일 불러오기")
    public void loadNonExistent() {
        assertThat(service.load("foo.txt")).doesNotExist();
    }

    @Test
    @DisplayName("파일 저장하고 불러오기")
    public void saveAndLoad() {
        service.store(new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE,
                "Hello, World".getBytes()), UUID.randomUUID().toString());
        assertThat(service.load("foo.txt")).exists();
    }

    @Test
    @DisplayName("허용되지 않는 상대 경로 저장")
    public void saveRelativePathNotPermitted() {
        assertThrows(StorageException.class, () -> {
            service.store(new MockMultipartFile("foo", "../foo.txt",
                    MediaType.TEXT_PLAIN_VALUE, "Hello, World".getBytes()), UUID.randomUUID().toString());
        });
    }

    @Test
    @DisplayName("허용되지 않는 상대 경로 저장")
    public void saveAbsolutePathNotPermitted() {
        assertThrows(StorageException.class, () -> {
            service.store(new MockMultipartFile("foo", "/etc/passwd",
                    MediaType.TEXT_PLAIN_VALUE, "Hello, World".getBytes()), UUID.randomUUID().toString());
        });
    }

    @Test
    @EnabledOnOs({OS.LINUX})
    @DisplayName("허가되는 절대 경로 저장 상의 파일이름 저장")
    public void saveAbsolutePathInFilenamePermitted() {
        String fileName="\\etc\\passwd";
        service.store(new MockMultipartFile(fileName, fileName,
                MediaType.TEXT_PLAIN_VALUE, "Hello, World".getBytes()), UUID.randomUUID().toString());
        assertTrue(Files.exists(
                Paths.get(properties.getLocation()).resolve(Paths.get(fileName))
        ));
    }

    @Test
    @DisplayName("저장이 허용된 경우")
    public void savePermitted() {
        service.store(new MockMultipartFile("foo", "bar/../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World".getBytes()), UUID.randomUUID().toString());
    }
}
