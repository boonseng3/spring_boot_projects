package com.obs.repo;

import com.obs.ApplicationServer;
import com.obs.TestUtil;
import com.obs.entity.Comment;
import com.obs.entity.OrphanComment;
import com.obs.entity.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ongbo on 2/1/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.config.name=server-test"}, classes = {ApplicationServer.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestPostRepo {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PostRepo postRepo;

    @Autowired
    CommentRepo commentRepo;

    @Autowired
    OrphanCommentRepo orphanCommentRepo;

    @Test
    @Transactional
    public void createPostCascadePersist() {

        Post post = TestUtil.createPost();
        Comment comment1 = (Comment) new Comment().setContent(UUID.randomUUID().toString()).setPost(post)
                .setCreatedBy(post.getCreatedBy()).setUpdatedBy(post.getUpdatedBy());
        Comment comment2 = (Comment) new Comment().setContent(UUID.randomUUID().toString()).setPost(post)
                .setCreatedBy(post.getCreatedBy()).setUpdatedBy(post.getUpdatedBy());
        post.getComments().add(comment1);
        post.getComments().add(comment2);

        postRepo.save(post);

        Comment dbComment1 = commentRepo.findOne(comment1.getId());
        verifyComment(dbComment1, comment1, post);

        Comment dbComment2 = commentRepo.findOne(comment2.getId());
        verifyComment(dbComment2, comment2, post);

        Post dbObject = postRepo.readById(post.getId()).get();
        verifyPost(dbObject, post, dbComment1, dbComment2);
        logger.debug("Created object {}", dbObject);
    }

    @Test
    @Transactional
    public void updatePostWithOrphanRemoval() {

        Post post = TestUtil.createPost();
        Comment comment1 = (Comment) new Comment().setContent(UUID.randomUUID().toString()).setPost(post)
                .setCreatedBy(post.getCreatedBy()).setUpdatedBy(post.getUpdatedBy());
        Comment comment2 = (Comment) new Comment().setContent(UUID.randomUUID().toString()).setPost(post)
                .setCreatedBy(post.getCreatedBy()).setUpdatedBy(post.getUpdatedBy());
        post.getComments().add(comment1);
        post.getComments().add(comment2);

        post = postRepo.save(post);

        Comment dbComment1 = commentRepo.findOne(comment1.getId());
        verifyComment(dbComment1, comment1, post);

        Comment dbComment2 = commentRepo.findOne(comment2.getId());
        verifyComment(dbComment2, comment2, post);

        Post dbObject = postRepo.readById(post.getId()).get();
        // remove previous comments
        dbObject.getComments().clear();
        Comment comment3 = (Comment) new Comment().setContent(UUID.randomUUID().toString()).setPost(dbObject)
                .setCreatedBy(dbObject.getCreatedBy()).setUpdatedBy(dbObject.getUpdatedBy());
        Comment comment4 = (Comment) new Comment().setContent(UUID.randomUUID().toString()).setPost(dbObject)
                .setCreatedBy(dbObject.getCreatedBy()).setUpdatedBy(dbObject.getUpdatedBy());

        // need to be created first as the post is not created together
        commentRepo.save(comment3);
        commentRepo.save(comment4);

        dbObject.getComments().add(comment3);
        dbObject.getComments().add(comment4);
        postRepo.save(dbObject);


        Comment dbComment3 = commentRepo.findOne(comment3.getId());
        verifyComment(dbComment3, comment3, dbObject);

        Comment dbComment4 = commentRepo.findOne(comment4.getId());
        verifyComment(dbComment4, comment4, dbObject);

        dbObject = postRepo.readById(post.getId()).get();
        verifyPost(dbObject, dbComment3, dbComment4);
        logger.debug("Created object {}", dbObject);

        // orphan records get deleted
        assertThat(commentRepo.findOne(comment1.getId())).isNull();
        assertThat(commentRepo.findOne(comment2.getId())).isNull();
        assertThat(commentRepo.findAll()).hasSize(2);
    }

    @Test
    @Transactional
    public void updatePostWithoutOrphanRemoval() {

        Post post = TestUtil.createPost();
        OrphanComment comment1 = (OrphanComment) new OrphanComment().setContent(UUID.randomUUID().toString()).setPost(post)
                .setCreatedBy(post.getCreatedBy()).setUpdatedBy(post.getUpdatedBy());
        OrphanComment comment2 = (OrphanComment) new OrphanComment().setContent(UUID.randomUUID().toString()).setPost(post)
                .setCreatedBy(post.getCreatedBy()).setUpdatedBy(post.getUpdatedBy());
        post.getOrphanComments().add(comment1);
        post.getOrphanComments().add(comment2);

        postRepo.save(post);

        OrphanComment dbComment1 = orphanCommentRepo.findOne(comment1.getId());
        verifyComment(dbComment1, comment1, post);

        OrphanComment dbComment2 = orphanCommentRepo.findOne(comment2.getId());
        verifyComment(dbComment2, comment2, post);

        Post dbObject = postRepo.readById(post.getId()).get();
        // remove previous comments
        dbObject.getComments().clear();
        OrphanComment comment3 = (OrphanComment) new OrphanComment().setContent(UUID.randomUUID().toString()).setPost(dbObject)
                .setCreatedBy(dbObject.getCreatedBy()).setUpdatedBy(dbObject.getUpdatedBy());
        OrphanComment comment4 = (OrphanComment) new OrphanComment().setContent(UUID.randomUUID().toString()).setPost(dbObject)
                .setCreatedBy(dbObject.getCreatedBy()).setUpdatedBy(dbObject.getUpdatedBy());

        // need to be created first as the post is not created together
        orphanCommentRepo.save(comment3);
        orphanCommentRepo.save(comment4);

        dbObject.getOrphanComments().add(comment3);
        dbObject.getOrphanComments().add(comment4);
        postRepo.save(dbObject);


        OrphanComment dbComment3 = orphanCommentRepo.findOne(comment3.getId());
        verifyComment(dbComment3, comment3, dbObject);

        OrphanComment dbComment4 = orphanCommentRepo.findOne(comment4.getId());
        verifyComment(dbComment4, comment4, dbObject);

        dbObject = postRepo.readById(post.getId()).get();
        verifyPost(dbObject, dbComment1, dbComment2, dbComment3, dbComment4);
        logger.debug("Created object {}", dbObject);

        // orphan records get deleted
        assertThat(orphanCommentRepo.findOne(comment1.getId())).isNotNull();
        assertThat(orphanCommentRepo.findOne(comment2.getId())).isNotNull();
        assertThat(orphanCommentRepo.findAll()).hasSize(4);
    }

    @Test
    @Transactional
    public void deletePost() {

        Post post = TestUtil.createPost();
        Comment comment1 = (Comment) new Comment().setContent(UUID.randomUUID().toString()).setPost(post)
                .setCreatedBy(post.getCreatedBy()).setUpdatedBy(post.getUpdatedBy());
        Comment comment2 = (Comment) new Comment().setContent(UUID.randomUUID().toString()).setPost(post)
                .setCreatedBy(post.getCreatedBy()).setUpdatedBy(post.getUpdatedBy());
        post.getComments().add(comment1);
        post.getComments().add(comment2);

        postRepo.save(post);

        Comment dbComment1 = commentRepo.findOne(comment1.getId());
        verifyComment(dbComment1, comment1, post);

        Comment dbComment2 = commentRepo.findOne(comment2.getId());
        verifyComment(dbComment2, comment2, post);

        Post dbObject = postRepo.readById(post.getId()).get();
        verifyPost(dbObject, post, dbComment1, dbComment2);

        postRepo.delete(dbObject);
        assertThat(postRepo.findOne(post.getId())).isNull();
        assertThat(commentRepo.findOne(dbComment1.getId())).isNull();
        assertThat(commentRepo.findOne(comment2.getId())).isNull();
    }

    private void verifyComment(Comment dbObject, Comment comment, Post post) {
        assertThat(dbObject).isEqualToIgnoringGivenFields(comment, "createdDateTime", "updatedDateTime", "post");
        assertThat(dbObject.getPost().getId()).isEqualTo(post.getId());
    }

    private void verifyPost(Post dbObject, Post post, Comment... comments) {
        assertThat(dbObject).isEqualToIgnoringGivenFields(post, "createdDateTime", "updatedDateTime", "comments");
        assertThat(dbObject.getComments()).containsExactlyInAnyOrder(comments);
    }

    private void verifyPost(Post dbObject, Comment... comments) {
        assertThat(dbObject.getComments()).containsExactlyInAnyOrder(comments);
    }

    private void verifyComment(OrphanComment dbObject, OrphanComment comment, Post post) {
        assertThat(dbObject).isEqualToIgnoringGivenFields(comment, "createdDateTime", "updatedDateTime", "post");
        assertThat(dbObject.getPost().getId()).isEqualTo(post.getId());
    }

    private void verifyPost(Post dbObject, Post post, OrphanComment... comments) {
        assertThat(dbObject).isEqualToIgnoringGivenFields(post, "createdDateTime", "updatedDateTime");
        assertThat(dbObject.getOrphanComments()).containsExactlyInAnyOrder(comments);
    }

    private void verifyPost(Post dbObject, OrphanComment... comments) {
        assertThat(dbObject.getOrphanComments()).containsExactlyInAnyOrder(comments);
    }
}
