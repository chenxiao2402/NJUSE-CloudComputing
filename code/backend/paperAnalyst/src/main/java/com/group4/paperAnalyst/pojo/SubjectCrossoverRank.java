package com.group4.paperAnalyst.pojo;

import javax.persistence.*;
@Entity
@Table(name = "subject_crossover_rank")
public class SubjectCrossoverRank {
  @EmbeddedId
  private SubjectCrossoverRankPK id;
  private double crossoverRank;

  public SubjectCrossoverRankPK getId() {
    return id;
  }

  public void setId(SubjectCrossoverRankPK id) {
    this.id = id;
  }

  public double getCrossoverRank() {
    return crossoverRank;
  }

  public void setCrossoverRank(double crossoverRank) {
    this.crossoverRank = crossoverRank;
  }

}
