package br.com.juliocauan.aluraflix.domain.model;

public abstract class Categoria {
    
    public abstract String getTitulo();
    public abstract Cor getCor();
    public abstract void setTitulo(String titulo);
    public abstract void setCor(Cor cor);

}
