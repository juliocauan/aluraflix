package br.com.juliocauan.aluraflix.infrastructere.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import br.com.juliocauan.aluraflix.domain.model.Categoria;
import br.com.juliocauan.aluraflix.domain.model.Video;

@Entity
@Table(name = "videos")
public class VideoEntity implements Video{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Size(min = 10, max = 255)
    @Column(nullable = false, unique = true)
    private String url;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = false)
    private CategoriaEntity categoria;

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public String getTitulo() {
        return titulo;
    }
    @Override
    public void setTitulo(String titulo) {
        this.titulo = titulo;        
    }
    @Override
    public String getDescricao() {
        return descricao;
    }
    @Override
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    @Override
    public String getUrl() {
        return url;
    }
    @Override
    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public Categoria getCategoria() {
        return categoria;
    }
    @Override
    public void setCategoria(Categoria categoria) {
        this.categoria = (CategoriaEntity) categoria;
    }

}
