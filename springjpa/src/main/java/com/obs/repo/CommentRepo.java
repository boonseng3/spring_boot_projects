package com.obs.repo;

import com.obs.entity.Comment;
import com.obs.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ongbo on 2/1/2017.
 */
public interface CommentRepo extends CrudRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
