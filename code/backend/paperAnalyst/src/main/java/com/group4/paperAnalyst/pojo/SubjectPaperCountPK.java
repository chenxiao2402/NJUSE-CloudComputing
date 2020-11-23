package com.group4.paperAnalyst.pojo;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubjectPaperCountPK implements Serializable {
    private long month;

    private long year;

    private String subject;

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
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
        SubjectPaperCountPK that = (SubjectPaperCountPK) o;
        return month == that.month &&
                year == that.year &&
                subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, year, subject);
    }
}
