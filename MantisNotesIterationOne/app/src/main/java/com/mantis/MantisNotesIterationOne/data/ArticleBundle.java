package com.mantis.MantisNotesIterationOne.data;

import java.util.ArrayList;
import java.util.List;

public class ArticleBundle {

    private List<Article> articles = new ArrayList<>();

    public void addArticle( Article article ) {
        articles.add( article );
    }

    public List<Article> getArticles() {
        return articles;
    }
}
