package com.sideproject.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stroage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "~/upload-profile";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
