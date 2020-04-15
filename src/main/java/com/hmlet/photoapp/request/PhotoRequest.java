package com.hmlet.photoapp.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoRequest {
    private Long id;
    private boolean published;
    private String caption;
    private Date publishedDateTime;
    private String userName;
    private String Url;
}
