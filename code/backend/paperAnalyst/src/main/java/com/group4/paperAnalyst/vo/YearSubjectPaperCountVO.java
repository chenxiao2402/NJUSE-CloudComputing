package com.group4.paperAnalyst.vo;

public class YearSubjectPaperCountVO {

    private Long year;

    private String field;

    private Long count;

    public YearSubjectPaperCountVO(Long year, String field, Long count) {
        this.year = year;
        this.field = field;
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }
}
