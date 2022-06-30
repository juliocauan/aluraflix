package br.com.juliocauan.aluraflix.domain.model;

public interface Video {
    String getTitle();
    String getDescription();
    String getUrl();
    Category getCategory();
}
