package com.hmlet.photoapp.cotroller;

import com.hmlet.photoapp.exception.InvalidPhotoTypeException;
import com.hmlet.photoapp.exception.PhotoDoesNotExistsException;
import com.hmlet.photoapp.model.Photo;
import com.hmlet.photoapp.model.User;
import com.hmlet.photoapp.request.PhotoRequest;
import com.hmlet.photoapp.request.UserRequest;
import com.hmlet.photoapp.response.PhotoResponse;
import com.hmlet.photoapp.response.UploadFileResponse;
import com.hmlet.photoapp.response.UserResponse;
import com.hmlet.photoapp.service.FileStorageService;
import com.hmlet.photoapp.service.PhotoService;
import com.hmlet.photoapp.service.UserService;
import com.hmlet.photoapp.util.constants.PhotoType;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/photo_app")
@AllArgsConstructor
public class PhotoAppController {
    private final UserService userService;
    private final PhotoService photoService;
    private FileStorageService fileStorageService;

    private final String downloadPhotoPathMapping = "download_photo";

    @PostMapping("add_photo")
    // Add a new photo
    public ResponseEntity<PhotoResponse> addPhoto(@RequestBody PhotoRequest photoRequest,
                                                       @RequestParam("photo") MultipartFile file) {

        Photo photo = createNewPhoto(getPublishDateTime(photoRequest), photoRequest);

        String fileName = fileStorageService.storeFile(photo.getUser().getName(), photo.getId().toString(), file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(downloadPhotoPathMapping)
                .path(photo.getUser().getName())
                .path(photo.getId().toString())
                .toUriString();

        photo.setUrl(fileDownloadUri);
        PhotoResponse response = getPhotoResponse(photoService.save(photo));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("update_photo")
    // Update photo details
    public ResponseEntity<PhotoResponse> publishPhoto(@RequestBody PhotoRequest photoRequest) {

        Photo photo;
        Date publishedDateTime = getPublishDateTime(photoRequest);

        if (photoRequest.getId() != null) {
            try {
                photo = photoService.findById(photoRequest.getId());
                photo.setCaption(photoRequest.getCaption() == null ? photo.getCaption() : photoRequest.getCaption());
                photo.setPublishedDateTime(photo.isPublished() ? photo.getPublishedDateTime() : publishedDateTime);
                photo.setPublished(photoRequest.isPublished());

                photo = photoService.save(photo);
            } catch (NoSuchElementException ex) {
                throw new PhotoDoesNotExistsException(photoRequest.getId());
            }
        } else {
            photo = createNewPhoto(publishedDateTime, photoRequest);
        }

        PhotoResponse response = getPhotoResponse(photo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(downloadPhotoPathMapping+"/{userName}/{photoId}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable(value = "userName", required = true) String userName,
                                                  @PathVariable(value = "photoId", required = true) String photoId,
                                                  HttpServletRequest request) {

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(userName, photoId);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // TODO log("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("photos")
    // Delete photos
    public ResponseEntity<List<PhotoResponse>> deletePhotos(@RequestBody List<PhotoRequest> photoList) {
        return null; // TODO
    }

    @GetMapping("photos/{userName}/{type}")
    // List photos (all, my photos, my drafts), ASC/DESC Sort photos on publishing date, Filter photos by user.
    public ResponseEntity<List<PhotoResponse>> getPhotos(
            @PathVariable(value = "userName", required = true) String userName,
            @PathVariable(value = "type", required = true) String type) {

        PhotoType photoType;
        try {
            photoType = PhotoType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidPhotoTypeException(type);
        }

        switch (photoType) {
            case ALL:
                // Retrieve both published and draft photos
                break;
            case PUBLISHED:
                // Retrieve only published photos
                break;
            case DRAFT:
                // Retrieve only draft photos
                break;
            default:
                // Should not reach this
                throw new InvalidPhotoTypeException(type);
        }

        return null; // TODO
    }


    @PutMapping("user")
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest) {

        User user = this.userService.addUser(User.builder()
                .name(userRequest.getUserName())
                .photos(null)
                .build());
        return new ResponseEntity<>(UserResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("user/{userName}")
    public ResponseEntity<UserResponse> getUser(@PathVariable(name = "userName") String userName) {

        User user = userService.getUser(userName);
        return new ResponseEntity<>(UserResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .build(),
                HttpStatus.OK);
    }

    private Photo createNewPhoto(Date publishDateTime, PhotoRequest photoRequest) {

        User user = userService.getUser(photoRequest.getUserName());

        Photo photo = Photo.builder()
                .published(photoRequest.isPublished())
                .caption(photoRequest.getCaption())
                .publishedDateTime(publishDateTime)
                .user(user)
                .build();

        return photoService.save(photo);
    }

    private Date getPublishDateTime(PhotoRequest photoRequest) {
        // TODO use timezone aware datetime instead just Date()
        return photoRequest.getPublishedDateTime() == null ? new Date() : photoRequest.getPublishedDateTime();
    }

    private PhotoResponse getPhotoResponse(Photo photo) {
        return PhotoResponse.builder()
                .id(photo.getId())
                .published(photo.isPublished())
                .caption(photo.getCaption())
                .publishedDateTime(photo.getPublishedDateTime())
                .userName(photo.getUser().getName())
                .url(photo.getUrl())
                .build();
    }
}
