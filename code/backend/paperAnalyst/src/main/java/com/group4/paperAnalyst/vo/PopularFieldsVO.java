package com.group4.paperAnalyst.vo;

public class PopularFieldsVO {

    private String field;

    private Long paperNumber;

    private Long authorNumber;

    public PopularFieldsVO(String field, Long paperNumber, Long authorNumber) {
        this.field = field;
        this.paperNumber = paperNumber;
        this.authorNumber = authorNumber;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Long getPaperNumber() {
        return paperNumber;
    }

    public void setPaperNumber(Long paperNumber) {
        this.paperNumber = paperNumber;
    }

    public Long getAuthorNumber() {
        return authorNumber;
    }

    public void setAuthorNumber(Long authorNumber) {
        this.authorNumber = authorNumber;
    }
}
