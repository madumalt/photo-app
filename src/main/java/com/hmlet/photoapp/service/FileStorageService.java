package com.hmlet.photoapp.service;

import com.hmlet.photoapp.config.FileStorageProperties;
import com.hmlet.photoapp.exception.FileNotFoundException;
import com.hmlet.photoapp.exception.FileStorageException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Data
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(String userName, String photoId, MultipartFile file) {
        String fileName = photoId;

        Path userPath = this.getFileStorageLocation().resolve(userName).normalize();

        try {
            Files.createDirectories(userPath);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the user directory (user-name: " + userName + ") " +
                    "where the uploaded files will be stored.", ex);
        }

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = userPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String userName, String photoId) {
        try {
            Path filePath = this.getFileStorageLocation().resolve(userName).resolve(photoId).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + userName + "/" + photoId);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + userName + "/" + photoId, ex);
        }
    }
}
