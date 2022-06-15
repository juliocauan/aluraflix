package br.com.juliocauan.aluraflix.infrastructure.model;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "videos")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
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
    public void setCategoria(Categoria categoria) {
        this.categoria = (CategoriaEntity) categoria;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VideoEntity other = (VideoEntity) obj;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

}
