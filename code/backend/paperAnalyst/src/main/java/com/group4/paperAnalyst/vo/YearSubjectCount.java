package com.group4.paperAnalyst.vo;

/**
 * Author: J.D. Liao
 * Date: 2020/11/24
 * Description:
 */
public class YearSubjectCount {

    private Long year;

    private String subject;

    private Long count;

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public YearSubjectCount(Long year, String subject, Long count) {
        this.year = year;
        this.subject = subject;
        this.count = count;
    }
}
