package com.hmlet.photoapp.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PhotoResponse {

    private Long id;

    private boolean published;

    private String caption;

    private Date publishedDateTime;

    private String userName;
}
