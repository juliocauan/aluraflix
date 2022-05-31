package br.com.juliocauan.aluraflix.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;

import br.com.juliocauan.aluraflix.config.TestContext;
import br.com.juliocauan.openapi.model.CategoriaGet;
import br.com.juliocauan.openapi.model.CategoriaPost;
import br.com.juliocauan.openapi.model.CategoriaPut;
import br.com.juliocauan.openapi.model.Cor;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
public class CategoriaControllerTest extends TestContext {

        private final String url = "/categorias";
        private final String urlId = "/categorias/{categoriaId}";
        private final String urlInvalidId = "/categorias/0";

        private CategoriaPost categoriaPost = new CategoriaPost();
        private CategoriaPut categoriaPut = new CategoriaPut();
        private Integer categoriaId;

        private void postCategoria() throws Exception {
                String response = getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPost)))
                                .andReturn().getResponse().getContentAsString();
                categoriaId = getObjectMapper().readValue(response, CategoriaGet.class).getId();
        }

        @BeforeEach
        public void setup() {
                categoriaPost
                                .titulo("Título teste POST")
                                .cor(Cor.TEAL);
                categoriaPut
                                .titulo("Título teste PUT")
                                .cor(Cor.WHITE);
                ;
        }

        @Test
        @DisplayName("Cadastra um Categoria válido")
        public void givenCategoria_WhenPostValidCategoria_Then201() throws Exception {

                getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(notNullValue()))
                                .andExpect(jsonPath("$.titulo").value(categoriaPost.getTitulo()))
                                .andExpect(jsonPath("$.cor").value(categoriaPost.getCor().getValue()));
        }

        @Test
        @DisplayName("Erro ao tentar cadastrar um Categoria inválido")
        public void givenCategoria_WhenPostInvalidCategoria_Then400() throws Exception {

                categoriaPost.titulo(
                                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                                .cor(null);
                getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("2001"))
                                .andExpect(jsonPath("$.fieldList", hasSize(2)));
        }

        @Test
        @DisplayName("Busca lista de Categorias")
        public void givenCategoria_WhenGetAllCategorias_Then200() throws Exception {

                postCategoria();
                getMockMvc().perform(
                                get(url))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.[0].id").value(notNullValue()))
                                .andExpect(jsonPath("$.[0].titulo").value(categoriaPost.getTitulo()))
                                .andExpect(jsonPath("$.[0].cor").value(categoriaPost.getCor().getValue()));
        }

        @Test
        @DisplayName("Busca Categoria por Id")
        public void givenCategoria_WhenGetCategoriaById_Then200() throws Exception {

                postCategoria();
                getMockMvc().perform(
                                get(urlId, categoriaId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(categoriaId))
                                .andExpect(jsonPath("$.titulo").value(categoriaPost.getTitulo()))
                                .andExpect(jsonPath("$.cor").value(categoriaPost.getCor().getValue()));
                ;
        }

        @Test
        @DisplayName("Erro ao tentar buscar Categoria por Id inválido")
        public void givenCategoria_WhenGetCategoriaByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                get(urlInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructere.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Atualiza Categoria válido")
        public void givenCategoria_WhenPutValidCategoria_Then200() throws Exception {

                postCategoria();
                getMockMvc().perform(
                                put(urlId, categoriaId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(nullValue()))
                                .andExpect(jsonPath("$.titulo").value(categoriaPut.getTitulo()))
                                .andExpect(jsonPath("$.cor").value(categoriaPut.getCor().getValue()));
                ;
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Categoria inválido")
        public void givenCategoria_WhenPutInvalidCategoria_Then400() throws Exception {

                postCategoria();
                categoriaPut
                                .titulo("a")
                                .cor(null);
                getMockMvc().perform(
                                put(urlId, categoriaId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("2001"))
                                .andExpect(jsonPath("$.fieldList", hasSize(1)));
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Categoria válido por Id inválido")
        public void givenCategoria_WhenPutValidCategoriaByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                put(urlInvalidId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructere.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Deleta Categoria")
        public void givenCategoria_WhenDeleteCategoria_Then200() throws Exception {

                postCategoria();
                getMockMvc().perform(
                                delete(urlId, categoriaId))
                                .andDo(print())
                                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Erro ao tentar deletar Categoria por Id inválido")
        public void givenCategoria_WhenDeleteCategoriaByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                delete(urlInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructere.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

}
