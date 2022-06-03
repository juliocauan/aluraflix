package br.com.juliocauan.aluraflix.domain.model;

import br.com.juliocauan.openapi.model.Cor;

public interface Categoria { 
    Integer getId();
    String getTitulo();
    Cor getCor();
    void setTitulo(String titulo);
    void setCor(Cor cor);
}
