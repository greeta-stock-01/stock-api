package net.greeta.blog.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;
import net.greeta.blog.model.User;

import java.util.UUID;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public  CommentEntity(String text, UserEntity author, PostEntity post) {
        this.text = text;
        this.author = author;
        this.post = post;
    }

}