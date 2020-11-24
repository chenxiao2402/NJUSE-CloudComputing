package com.group4.paperAnalyst.pojo;

import javax.persistence.Embeddable;
import javax.persistence.SecondaryTable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubjectCrossoverRankPK implements Serializable {
    private long startYear;
    private String subject;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectCrossoverRankPK that = (SubjectCrossoverRankPK) o;
        return startYear == that.startYear &&
                subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startYear, subject);
    }
}
