package com.obs.repo;

import com.obs.entity.OrphanComment;
import com.obs.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ongbo on 2/1/2017.
 */
public interface OrphanCommentRepo extends CrudRepository<OrphanComment, Long> {
    List<OrphanComment> findByPost(Post post);
}
