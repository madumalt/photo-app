package com.hmlet.photoapp.repository;

import com.hmlet.photoapp.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("SELECT photo FROM Photo photo INNER JOIN photo.user user WHERE user.name = :userName")
    List<Photo> findAllByUser(@Param("userName") String userName);

    @Query("SELECT photo FROM Photo photo INNER JOIN photo.user user " +
            "WHERE user.name = :userName AND photo.published = :isPublished")
    List<Photo> findAllByUser(@Param("userName") String userName, @Param("isPublished") boolean isPublished);
}
