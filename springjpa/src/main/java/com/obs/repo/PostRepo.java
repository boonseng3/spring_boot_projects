package com.obs.repo;

import com.obs.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by ongbo on 2/1/2017.
 */
public interface PostRepo extends CrudRepository<Post, Long> {
    /**
     * Similar to findOne but eager load the list of attribute.
     *
     * @param id
     * @return
     */
    @EntityGraph(attributePaths = {"comments", "orphanComments"})
    Optional<Post> readById(Long id);
}
