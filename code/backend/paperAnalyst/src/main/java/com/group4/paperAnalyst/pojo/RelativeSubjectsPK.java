package com.group4.paperAnalyst.pojo;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RelativeSubjectsPK implements Serializable {
    private long startYear;

    private String subject1;

    private String subject2;

    public long getStartYear() {
        return startYear;
    }

    public void setStartYear(long startYear) {
        this.startYear = startYear;
    }

    public String getSubject1() {
        return subject1;
    }

    public void setSubject1(String subject1) {
        this.subject1 = subject1;
    }

    public String getSubject2() {
        return subject2;
    }

    public void setSubject2(String subject2) {
        this.subject2 = subject2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelativeSubjectsPK that = (RelativeSubjectsPK) o;
        return startYear == that.startYear &&
                subject1.equals(that.subject1) &&
                subject2.equals(that.subject2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startYear, subject1, subject2);
    }
}
