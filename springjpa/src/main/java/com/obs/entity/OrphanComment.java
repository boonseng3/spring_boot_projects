package com.obs.entity;

import javax.persistence.*;

/**
 * Created by ongbo on 2/1/2017.
 */
@Entity
public class OrphanComment extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private Post post;

    public Long getId() {
        return id;
    }

    public OrphanComment setId(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public OrphanComment setContent(String content) {
        this.content = content;
        return this;
    }

    public Post getPost() {
        return post;
    }

    public OrphanComment setPost(Post post) {
        this.post = post;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrphanComment that = (OrphanComment) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrphanComment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                "} " + super.toString();
    }
}
