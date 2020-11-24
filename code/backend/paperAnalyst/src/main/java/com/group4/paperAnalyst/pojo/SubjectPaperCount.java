package com.group4.paperAnalyst.pojo;

import javax.persistence.*;
@Entity
@Table(name = "subject_paper_count")
public class SubjectPaperCount {
  @EmbeddedId
  private SubjectPaperCountPK id;

  private long paperCount;

  private long authorCount;

  public long getAuthorCount() {
    return authorCount;
  }

  public void setAuthorCount(long authorCount) {
    this.authorCount = authorCount;
  }

  public SubjectPaperCountPK getId() {
    return id;
  }

  public Long getMonth() {
    return id.getMonth();
  }

  public void setId(SubjectPaperCountPK id) {
    this.id = id;
  }

  public long getPaperCount() {
    return paperCount;
  }

  public void setPaperCount(long paperCount) {
    this.paperCount = paperCount;
  }
}
