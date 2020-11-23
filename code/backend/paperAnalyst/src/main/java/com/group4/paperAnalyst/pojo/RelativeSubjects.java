package com.group4.paperAnalyst.pojo;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "relative_subjects")
public class RelativeSubjects {
  @EmbeddedId
  private RelativeSubjectsPK id;
  @Column(name="paper_count")
  private long paperCount;

  public RelativeSubjectsPK getId() {
    return id;
  }

  public void setId(RelativeSubjectsPK id) {
    this.id = id;
  }

  public long getPaperCount() {
    return paperCount;
  }

  public void setPaperCount(long paperCount) {
    this.paperCount = paperCount;
  }

}
