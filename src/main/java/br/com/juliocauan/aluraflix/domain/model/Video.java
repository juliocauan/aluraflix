package br.com.juliocauan.aluraflix.domain.model;

public interface Video {
    Integer getId();
    String getTitulo();
    void setTitulo(String titulo);
    String getDescricao();
    void setDescricao(String descricao);
    String getUrl();
    void setUrl(String url);
    Categoria getCategoria(); 
    void setCategoria(Categoria categoria);
}
