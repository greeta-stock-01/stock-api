package net.greeta.blog.service;

import net.greeta.blog.entity.UserEntity;
import net.greeta.blog.model.AddUserInput;
import net.greeta.blog.model.User;
import net.greeta.blog.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByPostId(UUID postId) {
        UserEntity userEntity = userRepository.findByPostsId(postId);

        return new User(userEntity.getId(), userEntity.getName());
    }

    public UUID addUser(AddUserInput userInput) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userInput.getName());
        userEntity.setRoles(userInput.getRoles());

        UserEntity user = userRepository.save(userEntity);

        if (user.getId() == null) {
            throw new RuntimeException("User id cannot be null");
        }

        return user.getId();
    }

    public List<User> getUsers(int page, int size) {
        Page<UserEntity> users = userRepository.findAll(PageRequest.of(page, size));
        return users.stream()
                .map(userEntity -> new User(userEntity.getId(), userEntity.getName()))
                .collect(Collectors.toList());
    }

    public User findByCommentId(UUID commentId) {
        if (commentId == null) {
            throw new RuntimeException("CommentId cannot be null");
        }

        UserEntity userEntity = userRepository.findByCommentsId(commentId);
        return new User(userEntity.getId(), userEntity.getName());
    }

}