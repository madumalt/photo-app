package com.hmlet.photoapp.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String userName;
    private Long  userId;
}
