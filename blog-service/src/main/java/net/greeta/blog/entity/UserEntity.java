package net.greeta.blog.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;

    @Column
    private String name;

    @Column
    private String roles;

    @OneToMany(mappedBy = "author")
    private Set<PostEntity> posts;

    @OneToMany(mappedBy = "author")
    private Set<CommentEntity> comments;

    public UserEntity(String name, String roles) {
        this.name = name;
        this.roles = roles;
    }

}