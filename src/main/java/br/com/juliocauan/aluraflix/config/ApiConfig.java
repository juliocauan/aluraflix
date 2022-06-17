package br.com.juliocauan.aluraflix.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.juliocauan.aluraflix.infrastructure.repository")
@EntityScan(basePackages = {"br.com.juliocauan.aluraflix.infrastructure.model"})
public class ApiConfig {
    
}