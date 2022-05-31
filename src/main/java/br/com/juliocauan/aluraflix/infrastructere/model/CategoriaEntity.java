package br.com.juliocauan.aluraflix.infrastructere.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import br.com.juliocauan.aluraflix.domain.model.Categoria;
import br.com.juliocauan.openapi.model.Cor;

@Entity
@Table(name = "categorias")
public class CategoriaEntity implements Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cor cor;

    @Override
    public Integer getId(){
        return id;
    }
    @Override
    public String getTitulo() {
        return titulo;
    }
    @Override
    public Cor getCor() {
        return cor;
    }
    @Override
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    @Override
    public void setCor(Cor cor) {
        this.cor = cor;
    }
    
}
