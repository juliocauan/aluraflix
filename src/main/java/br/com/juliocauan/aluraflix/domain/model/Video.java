package br.com.juliocauan.aluraflix.domain.model;

public abstract class Video {
    public abstract String getTitulo();
    public abstract void setTitulo(String titulo);
    public abstract String getDescricao();
    public abstract void setDescricao(String descricao);
    public abstract String getUrl();
    public abstract void setUrl(String url);
    public abstract Categoria getCategoria(); 
    public abstract void setCategoria(Categoria categoria);
}
