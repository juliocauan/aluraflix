package br.com.juliocauan.aluraflix.infrastructure.model.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import br.com.juliocauan.aluraflix.domain.model.auth.Profile;
import br.com.juliocauan.openapi.model.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProfileEntity implements Profile, GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Short id;
	
    @Enumerated(EnumType.STRING)
	private ProfileType value;

    @Override
    public String getAuthority() {
        return this.value.getValue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        ProfileEntity other = (ProfileEntity) obj;
        if (value != other.value)
            return false;
        return true;
    }

}
