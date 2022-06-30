package br.com.juliocauan.aluraflix.infrastructure.mapper.config;

import br.com.juliocauan.aluraflix.domain.mapper.BaseMapper;
import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;

public interface BaseMapStruct<E, GET, POST, PUT> extends BaseMapper<E, GET, POST, PUT>, ServiceMapper<E> {  
}
