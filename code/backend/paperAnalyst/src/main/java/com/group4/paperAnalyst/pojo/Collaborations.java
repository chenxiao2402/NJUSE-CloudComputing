package com.group4.paperAnalyst.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "collaborations")
public class Collaborations {

  private long startYear;
  private String subject;
  private long sourceAuthor;
  private long targetAuthor;
  private String path;
  @Id
  private long id;
  private long length;

  public void setId(long id) {
    this.id = id;
  }

  public long getLength() {
    return length;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public long getStartYear() {
    return startYear;
  }

  public void setStartYear(long startYear) {
    this.startYear = startYear;
  }


  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }


  public long getSourceAuthor() {
    return sourceAuthor;
  }

  public void setSourceAuthor(long sourceAuthor) {
    this.sourceAuthor = sourceAuthor;
  }


  public long getTargetAuthor() {
    return targetAuthor;
  }

  public void setTargetAuthor(long targetAuthor) {
    this.targetAuthor = targetAuthor;
  }


  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }


}
