package com.hmlet.photoapp.cotroller;

import com.hmlet.photoapp.model.User;
import com.hmlet.photoapp.request.PublishPhotoRequest;
import com.hmlet.photoapp.request.UserRequest;
import com.hmlet.photoapp.response.PhotoResponse;
import com.hmlet.photoapp.response.UserResponse;
import com.hmlet.photoapp.service.PhotoService;
import com.hmlet.photoapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/photo_app")
@AllArgsConstructor
public class PhotoAppContorller {
    private final UserService userService;
    private final PhotoService photoService;

    @PostMapping("publish_photo")
    public ResponseEntity<PhotoResponse> publishPhoto(@RequestBody PublishPhotoRequest request) {

        return new ResponseEntity<>(PhotoResponse.builder().build(), HttpStatus.OK);
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

    @GetMapping("user")
    public ResponseEntity<UserResponse> getUser(@RequestParam(name = "name") String userName) {

        User user = userService.getUser(userName);
        return new ResponseEntity<>(UserResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .build(),
                HttpStatus.OK);
    }
}
