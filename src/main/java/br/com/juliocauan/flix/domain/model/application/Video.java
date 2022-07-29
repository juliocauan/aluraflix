package br.com.juliocauan.flix.domain.model.application;

public interface Video {
    String getTitle();
    String getDescription();
    String getUrl();
    Category getCategory();
}
