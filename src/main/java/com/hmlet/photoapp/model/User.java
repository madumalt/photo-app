package com.hmlet.photoapp.model;

import com.hmlet.photoapp.exception.LinkedPhotoExistException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(nullable = true)
    private Set<Photo> photos;

    @PreRemove
    private void preventDeleteIfPhotosAssociated() {
        if (!photos.isEmpty()) {
            throw new LinkedPhotoExistException(this.getId(), this.getName());
        }
    }
}
