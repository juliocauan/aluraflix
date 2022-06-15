package br.com.juliocauan.aluraflix.infrastructure.model;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cor == null) ? 0 : cor.hashCode());
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
        CategoriaEntity other = (CategoriaEntity) obj;
        if (cor != other.cor)
            return false;
        return true;
    }
    
}
