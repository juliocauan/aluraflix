package br.com.juliocauan.aluraflix.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import br.com.juliocauan.aluraflix.config.TestContext;
import br.com.juliocauan.openapi.model.CategoriaGet;
import br.com.juliocauan.openapi.model.CategoriaPost;
import br.com.juliocauan.openapi.model.CategoriaPut;
import br.com.juliocauan.openapi.model.Cor;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
@TestInstance(Lifecycle.PER_CLASS)
public class CategoriaControllerTest extends TestContext {

        private final String url = "/categorias";
        private final String urlWithId = "/categorias/{categoriaId}";
        private final String urlWithInvalidId = "/categorias/0";
        private final String urlGetVideoListByCategoriaId = "/categorias/{categoriaId}/videos";
        private final String urlGetVideoListByCategoriaInvalidId = "/categorias/0/videos";
        private final Integer categoriaDefaultId = 1;

        private CategoriaPost categoriaPost = new CategoriaPost();
        private CategoriaPut categoriaPut = new CategoriaPut();
        private List<Integer> categoriaIdList = new ArrayList<>();
        private Integer lastCategoriaId = 0;
        private VideoGet videoGet = new VideoGet();

        private ResultActions postCategoria() throws Exception {
                ResultActions result = getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPost)));
                String videoGetString = result.andReturn().getResponse().getContentAsString();
                lastCategoriaId = getObjectMapper()
                                .readValue(videoGetString, CategoriaGet.class)
                                .getId();
                categoriaIdList.add(lastCategoriaId);
                return result;
        }

        @BeforeAll
        private void init() throws Exception {
                VideoPost videoPost = new VideoPost()
                                .descricao("Descricao teste POST in Categoria")
                                .titulo("Titulo teste POST in Categoria")
                                .url("https://www.testePOSTInCategoria.com/")
                                .categoriaId(categoriaDefaultId);
                String response = getMockMvc().perform(
                                post("/videos")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)))
                                .andReturn().getResponse().getContentAsString();
                videoGet = getObjectMapper().readValue(response, VideoGet.class);
        }

        @AfterAll
        private void clean() throws Exception {
                getMockMvc().perform(delete("/videos/{videoId}", videoGet.getId()));
                categoriaIdList.forEach(categoriaId -> {
                        try {
                                getMockMvc().perform(delete(urlWithId, categoriaId));
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });
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
                postCategoria()
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(lastCategoriaId))
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
                String pos = String.format("$.[%d].", categoriaIdList.size());
                getMockMvc().perform(
                                get(url))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath(pos + "id").value(lastCategoriaId))
                                .andExpect(jsonPath(pos + "titulo").value(categoriaPost.getTitulo()))
                                .andExpect(jsonPath(pos + "cor").value(categoriaPost.getCor().getValue()))
                                .andExpect(jsonPath("$", hasSize(categoriaIdList.size() + 1)));
        }

        @Test
        @DisplayName("Busca Categoria por Id")
        public void givenCategoria_WhenGetCategoriaById_Then200() throws Exception {
                postCategoria();
                getMockMvc().perform(
                                get(urlWithId, lastCategoriaId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastCategoriaId))
                                .andExpect(jsonPath("$.titulo").value(categoriaPost.getTitulo()))
                                .andExpect(jsonPath("$.cor").value(categoriaPost.getCor().getValue()));
        }

        @Test
        @DisplayName("Erro ao tentar buscar Categoria por Id inválido")
        public void givenCategoria_WhenGetCategoriaByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                get(urlWithInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Busca lista de Videos por Categoria")
        public void givenCategoria_WhenGetVideoListByCategoria_Then200() throws Exception {
                getMockMvc().perform(
                                get(urlGetVideoListByCategoriaId, categoriaDefaultId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.[0].id").value(videoGet.getId()))
                                .andExpect(jsonPath("$.[0].titulo").value(videoGet.getTitulo()))
                                .andExpect(jsonPath("$.[0].descricao").value(videoGet.getDescricao()))
                                .andExpect(jsonPath("$.[0].url").value(videoGet.getUrl()))
                                .andExpect(jsonPath("$.[0].categoriaId").value(videoGet.getCategoriaId()));
        }

        @Test
        @DisplayName("Erro ao tentar buscar lista de Videos por Categoria com Id inválido")
        public void givenCategoria_WhenGetVideoListByInvalidCategoriaId_Then404() throws Exception {
                getMockMvc().perform(
                                get(urlGetVideoListByCategoriaInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Atualiza Categoria válido")
        public void givenCategoria_WhenPutValidCategoria_Then200() throws Exception {
                postCategoria();
                getMockMvc().perform(
                                put(urlWithId, lastCategoriaId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastCategoriaId))
                                .andExpect(jsonPath("$.titulo").value(categoriaPut.getTitulo()))
                                .andExpect(jsonPath("$.cor").value(categoriaPut.getCor().getValue()));
        }

        @Test
        @DisplayName("Atualiza Categoria com Titulo nulo")
        public void givenCategoria_WhenPutCategoriaWithTituloNull_Then200() throws Exception {
                postCategoria();
                String titulo = categoriaPost.getTitulo();
                categoriaPut.titulo(null);
                getMockMvc().perform(
                                put(urlWithId, lastCategoriaId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastCategoriaId))
                                .andExpect(jsonPath("$.titulo").value(titulo))
                                .andExpect(jsonPath("$.cor").value(categoriaPut.getCor().getValue()));
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Categoria inválido")
        public void givenCategoria_WhenPutInvalidCategoria_Then400() throws Exception {
                postCategoria();
                categoriaPut.titulo("a").cor(null);
                getMockMvc().perform(
                                put(urlWithId, lastCategoriaId)
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
                                put(urlWithInvalidId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Deleta Categoria")
        public void givenCategoria_WhenDeleteCategoria_Then200() throws Exception {
                postCategoria();
                getMockMvc().perform(
                                delete(urlWithId, lastCategoriaId))
                                .andDo(print())
                                .andExpect(status().isOk());
                categoriaIdList.remove(categoriaIdList.size()-1);
        }

        @Test
        @DisplayName("Erro ao tentar deletar Categoria por Id inválido")
        public void givenCategoria_WhenDeleteCategoriaByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                delete(urlWithInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

}
