package com.group4.paperAnalyst.pojo;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "author_connections_author")
public class AuthorConnectionsAuthor {
  @EmbeddedId
  private AuthorConnectionsAuthorPK id;
  private String authorName;
  private long paperCount;
  private long authorCategory;

  public AuthorConnectionsAuthorPK getId() {
    return id;
  }

  public void setId(AuthorConnectionsAuthorPK id) {
    this.id = id;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }


  public long getPaperCount() {
    return paperCount;
  }

  public void setPaperCount(long paperCount) {
    this.paperCount = paperCount;
  }


  public long getAuthorCategory() {
    return authorCategory;
  }

  public void setAuthorCategory(long authorCategory) {
    this.authorCategory = authorCategory;
  }

}
