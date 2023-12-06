package net.greeta.blog.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;

    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @OneToMany(mappedBy = "post")
    private Set<CommentEntity> comments;

    public PostEntity(String title, String description, UserEntity author) {
        this.title = title;
        this.description = description;
        this.author = author;
    }

}