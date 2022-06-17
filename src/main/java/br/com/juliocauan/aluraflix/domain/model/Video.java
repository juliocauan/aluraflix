package br.com.juliocauan.aluraflix.domain.model;

public interface Video {
    String getTitulo();
    String getDescricao();
    String getUrl();
    Categoria getCategoria();
}
