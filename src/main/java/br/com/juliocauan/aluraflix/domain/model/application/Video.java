package br.com.juliocauan.aluraflix.domain.model.application;

public interface Video {
    String getTitle();
    String getDescription();
    String getUrl();
    Category getCategory();
}
