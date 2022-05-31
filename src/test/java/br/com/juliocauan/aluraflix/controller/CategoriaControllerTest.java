package br.com.juliocauan.aluraflix.controller;

import static org.hamcrest.Matchers.hasSize;
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
public class CategoriaControllerTest extends TestContext {

        private final String url = "/categorias";
        private final String urlId = "/categorias/{categoriaId}";
        private final String urlGetVideoListByCategoriaId = "/categorias/{categoriaId}/videos";
        private final String urlGetVideoListByCategoriaInvalidId = "/categorias/0/videos";
        private final String urlInvalidId = "/categorias/0";

        private CategoriaPost categoriaPost = new CategoriaPost();
        private CategoriaPut categoriaPut = new CategoriaPut();
        private Integer lastCategoriaId;

        private ResultActions postCategoriaAndUpdateLastCategoriaId() throws Exception {
                ResultActions result = getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPost)));
                String videoGetString = result.andReturn().getResponse().getContentAsString();
                lastCategoriaId = getObjectMapper()
                                .readValue(videoGetString, CategoriaGet.class)
                                .getId();
                return result;
        }
        private void deleteCategoria() throws Exception {
                getMockMvc().perform(
                                delete(urlId, lastCategoriaId));
        }
        private VideoGet postVideo() throws Exception {
                postCategoriaAndUpdateLastCategoriaId();
                VideoPost videoPost = new VideoPost()
                                .descricao("Descricao teste POST in Categoria")
                                .titulo("Titulo teste POST in Categoria")
                                .url("https://www.testePOSTInCategoria.com/")
                                .categoriaId(lastCategoriaId);
                String response = getMockMvc().perform(
                                post("/videos")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)))
                                .andReturn().getResponse().getContentAsString();
                return getObjectMapper().readValue(response, VideoGet.class);
        }
        private void deleteVideo(VideoGet videoGet) throws Exception{
                getMockMvc().perform(
                                delete("/videos/{videoId}", videoGet.getId()));
                deleteCategoria();
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
        @Order(1)
        @DisplayName("Cadastra um Categoria válido")
        public void givenCategoria_WhenPostValidCategoria_Then201() throws Exception {

                postCategoriaAndUpdateLastCategoriaId()
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(lastCategoriaId))
                                .andExpect(jsonPath("$.titulo").value(categoriaPost.getTitulo()))
                                .andExpect(jsonPath("$.cor").value(categoriaPost.getCor().getValue()));
                deleteCategoria();
        }

        @Test
        @Order(1)
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
        @Order(2)
        @DisplayName("Busca lista de Categorias")
        public void givenCategoria_WhenGetAllCategorias_Then200() throws Exception {

                postCategoriaAndUpdateLastCategoriaId();
                getMockMvc().perform(
                                get(url))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.[0].id").value(lastCategoriaId))
                                .andExpect(jsonPath("$.[0].titulo").value(categoriaPost.getTitulo()))
                                .andExpect(jsonPath("$.[0].cor").value(categoriaPost.getCor().getValue()));
                deleteCategoria();
        }

        @Test
        @Order(2)
        @DisplayName("Busca Categoria por Id")
        public void givenCategoria_WhenGetCategoriaById_Then200() throws Exception {

                postCategoriaAndUpdateLastCategoriaId();
                getMockMvc().perform(
                                get(urlId, lastCategoriaId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastCategoriaId))
                                .andExpect(jsonPath("$.titulo").value(categoriaPost.getTitulo()))
                                .andExpect(jsonPath("$.cor").value(categoriaPost.getCor().getValue()));
                deleteCategoria();
        }

        @Test
        @Order(2)
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
        @Order(2)
        @DisplayName("Busca lista de Videos por Categoria")
        public void givenCategoria_WhenGetVideoListByCategoria_Then200() throws Exception {

                VideoGet videoGet = postVideo();
                getMockMvc().perform(
                                get(urlGetVideoListByCategoriaId, lastCategoriaId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.[0].id").value(videoGet.getId()))
                                .andExpect(jsonPath("$.[0].titulo").value(videoGet.getTitulo()))
                                .andExpect(jsonPath("$.[0].descricao").value(videoGet.getDescricao()))
                                .andExpect(jsonPath("$.[0].url").value(videoGet.getUrl()))
                                .andExpect(jsonPath("$.[0].categoriaId").value(videoGet.getCategoriaId()));
                deleteVideo(videoGet);
        }

        @Test
        @Order(2)
        @DisplayName("Erro ao tentar buscar lista de Videos por Categoria com Id inválido")
        public void givenCategoria_WhenGetVideoListByInvalidCategoriaId_Then404() throws Exception {

                VideoGet videoGet = postVideo();
                getMockMvc().perform(
                                get(urlGetVideoListByCategoriaInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructere.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
                deleteVideo(videoGet);
        }

        @Test
        @Order(3)
        @DisplayName("Atualiza Categoria válido")
        public void givenCategoria_WhenPutValidCategoria_Then200() throws Exception {

                postCategoriaAndUpdateLastCategoriaId();
                getMockMvc().perform(
                                put(urlId, lastCategoriaId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(nullValue()))
                                .andExpect(jsonPath("$.titulo").value(categoriaPut.getTitulo()))
                                .andExpect(jsonPath("$.cor").value(categoriaPut.getCor().getValue()));
                deleteCategoria();
        }

        @Test
        @Order(3)
        @DisplayName("Erro ao tentar atualizar Categoria inválido")
        public void givenCategoria_WhenPutInvalidCategoria_Then400() throws Exception {

                postCategoriaAndUpdateLastCategoriaId();
                categoriaPut
                                .titulo("a")
                                .cor(null);
                getMockMvc().perform(
                                put(urlId, lastCategoriaId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoriaPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("2001"))
                                .andExpect(jsonPath("$.fieldList", hasSize(1)));
                deleteCategoria();
        }

        @Test
        @Order(3)
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
        @Order(4)
        @DisplayName("Deleta Categoria")
        public void givenCategoria_WhenDeleteCategoria_Then200() throws Exception {

                postCategoriaAndUpdateLastCategoriaId();
                getMockMvc().perform(
                                delete(urlId, lastCategoriaId))
                                .andDo(print())
                                .andExpect(status().isOk());
        }

        @Test
        @Order(4)
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
