package br.com.juliocauan.aluraflix.domain.model;

import br.com.juliocauan.openapi.model.Cor;

public abstract class Category {
    
    public abstract String getTitulo();
    public abstract Cor getCor();
    public abstract void setTitulo(String titulo);
    public abstract void setCor(Cor cor);

}
