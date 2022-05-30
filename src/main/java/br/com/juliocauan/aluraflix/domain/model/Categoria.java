package br.com.juliocauan.aluraflix.domain.model;

import br.com.juliocauan.openapi.model.Cor;

public interface Categoria { 
    Integer getId();
    void setId(Integer id);
    String getTitulo();
    Cor getCor();
    void setTitulo(String titulo);
    void setCor(Cor cor);
}
