package com.obs.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by ongbo on 2/1/2017.
 */
@Entity
public class Post extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    //  orphanRemoval will remove the comment when it is removed from the set and not associated to others
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private Set<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = false)
    private Set<OrphanComment> orphanComments;

    public Long getId() {
        return id;
    }

    public Post setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Post setContent(String content) {
        this.content = content;
        return this;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Post setComments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Set<OrphanComment> getOrphanComments() {
        return orphanComments;
    }

    public Post setOrphanComments(Set<OrphanComment> orphanComments) {
        this.orphanComments = orphanComments;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (id != null ? !id.equals(post.id) : post.id != null) return false;
        if (title != null ? !title.equals(post.title) : post.title != null) return false;
        if (content != null ? !content.equals(post.content) : post.content != null) return false;
        if (comments != null ? !comments.equals(post.comments) : post.comments != null) return false;
        return orphanComments != null ? orphanComments.equals(post.orphanComments) : post.orphanComments == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (orphanComments != null ? orphanComments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", comments=" + comments +
                ", orphanComments=" + orphanComments +
                "} " + super.toString();
    }
}
