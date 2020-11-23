package com.group4.paperAnalyst.pojo;

import javax.persistence.*;
@Entity
@Table(name = "subject_paper_count")
public class SubjectPaperCount {
  @EmbeddedId
  private SubjectPaperCountPK id;

  private long paperCount;

  public SubjectPaperCountPK getId() {
    return id;
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
