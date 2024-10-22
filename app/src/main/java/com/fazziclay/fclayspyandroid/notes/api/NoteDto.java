package com.fazziclay.fclayspyandroid.notes.api;

public class NoteDto {
    private String text;
    private Long latestEdit;

    public NoteDto(String text, Long latestEdit) {
        this.text = text;
        this.latestEdit = latestEdit;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getLatestEdit() {
        return latestEdit;
    }

    public void setLatestEdit(Long latestEdit) {
        this.latestEdit = latestEdit;
    }
}
