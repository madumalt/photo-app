package com.hmlet.photoapp.service;

import com.hmlet.photoapp.model.Photo;
import com.hmlet.photoapp.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }

    public List<Photo> findAllByUserName(String userName) {
        return photoRepository.findAllByUser(userName);
    }

    public List<Photo> findAllByUserName(String userName, boolean isPublished) {
        return photoRepository.findAllByUser(userName, isPublished);
    }
}
