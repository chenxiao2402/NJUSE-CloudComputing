package com.group4.paperAnalyst.pojo;

import javax.persistence.*;

@Entity
@Table(name = "author_citations")
public class AuthorCitations {
  @EmbeddedId
  private AuthorCitationsPK id;

  @Column(name = "citations")
  private long citations;

  public AuthorCitationsPK getId() {
    return id;
  }

  public void setId(AuthorCitationsPK id) {
    this.id = id;
  }

  public long getCitations() {
    return citations;
  }

  public void setCitations(long citations) {
    this.citations = citations;
  }

}
