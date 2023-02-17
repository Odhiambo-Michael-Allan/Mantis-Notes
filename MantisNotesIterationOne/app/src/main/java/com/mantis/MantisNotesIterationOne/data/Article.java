package com.mantis.MantisNotesIterationOne.data;

public class Article {

    private String headline, highlight;

    public Article( String title, String highlight ) {
        this.headline = title;
        this.highlight = highlight;
    }

    public String getHeadline() {
        return this.headline;
    }

    public String getHighlight() {
        return this.highlight;
    }
}
