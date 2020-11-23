package com.group4.paperAnalyst.pojo;


import javax.persistence.*;

@Entity
@Table(name = "paper_citations")
public class PaperCitations {
  @EmbeddedId
  private PaperCitationsPK id;

  @Column(name = "year")
  private long year;

  @Column(name = "month")
  private long month;

  @Column(name = "citations")
  private long citations;

}
