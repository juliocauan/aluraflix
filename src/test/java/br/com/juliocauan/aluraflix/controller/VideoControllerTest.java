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

import org.junit.jupiter.api.AfterEach;
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
import br.com.juliocauan.openapi.model.Cor;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
public class VideoControllerTest extends TestContext {

        private final String url = "/videos";
        private final String urlId = "/videos/{videoId}";
        private final String urlInvalidId = "/videos/0";

        private VideoPost videoPost = new VideoPost();
        private VideoPut videoPut = new VideoPut();
        private Integer videoId;
        private Integer categoriaId;

        private void postVideo() throws Exception {
                String response = getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)))
                                .andReturn().getResponse().getContentAsString();
                videoId = getObjectMapper().readValue(response, VideoGet.class).getId();
        }

        private Integer createCategoriaAndGetId() throws Exception {
                CategoriaPost categoriaPost = new CategoriaPost()
                                .cor(Cor.TEAL)
                                .titulo("Título teste Categoria POST");
                String response = getMockMvc().perform(
                                post("/categorias")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper()
                                                                .writeValueAsString(categoriaPost)))
                                .andReturn().getResponse().getContentAsString();
                return getObjectMapper().readValue(response, CategoriaGet.class).getId();
        }

        @BeforeEach
        public void setup() throws Exception {
                categoriaId = createCategoriaAndGetId();
                videoPost
                                .descricao("Descrição teste POST")
                                .titulo("Título teste POST")
                                .url("https://www.testePOST.com/")
                                .categoriaId(categoriaId);
                videoPut
                                .descricao("Descrição teste PUT")
                                .titulo("Título teste PUT")
                                .url("https://www.testePUT.com/")
                                .categoriaId(categoriaId);
        }

        @AfterEach
        public void clean() throws Exception {
                getMockMvc().perform(delete("/categorias/{categoriaId}", categoriaId));
        }

        @Test
        @DisplayName("Cadastra um Video válido")
        public void givenVideo_WhenPostValidVideo_Then201() throws Exception {

                getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(notNullValue()))
                                .andExpect(jsonPath("$.descricao").value(videoPost.getDescricao()))
                                .andExpect(jsonPath("$.titulo").value(videoPost.getTitulo()))
                                .andExpect(jsonPath("$.url").value(videoPost.getUrl()))
                                .andExpect(jsonPath("$.categoriaId").value(videoPost.getCategoriaId()));
        }

        @Test
        @DisplayName("Erro ao tentar cadastrar um Video inválido")
        public void givenVideo_WhenPostInvalidVideo_Then400() throws Exception {

                videoPost.titulo(
                                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                                .descricao(null)
                                .url("url");
                getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("2001"))
                                .andExpect(jsonPath("$.fieldList", hasSize(3)));
        }

        @Test
        @DisplayName("Busca lista de Videos")
        public void givenVideo_WhenGetAllVideos_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                get(url))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.[0].id").value("1"))
                                .andExpect(jsonPath("$.[0].titulo").value(videoPost.getTitulo()))
                                .andExpect(jsonPath("$.[0].descricao").value(videoPost.getDescricao()))
                                .andExpect(jsonPath("$.[0].url").value(videoPost.getUrl()))
                                .andExpect(jsonPath("$.[0].categoriaId").value(videoPost.getCategoriaId()));
        }

        @Test
        @DisplayName("Busca Video por Id")
        public void givenVideo_WhenGetVideoById_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                get(urlId, videoId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value("1"))
                                .andExpect(jsonPath("$.titulo").value(videoPost.getTitulo()))
                                .andExpect(jsonPath("$.descricao").value(videoPost.getDescricao()))
                                .andExpect(jsonPath("$.url").value(videoPost.getUrl()))
                                .andExpect(jsonPath("$.categoriaId").value(videoPost.getCategoriaId()));
        }

        @Test
        @DisplayName("Erro ao tentar buscar Video por Id inválido")
        public void givenVideo_WhenGetVideoByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                get(urlInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Atualiza Video válido")
        public void givenVideo_WhenPutValidVideo_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                put(urlId, videoId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(nullValue()))
                                .andExpect(jsonPath("$.descricao").value(videoPut.getDescricao()))
                                .andExpect(jsonPath("$.titulo").value(videoPut.getTitulo()))
                                .andExpect(jsonPath("$.url").value(videoPut.getUrl()))
                                .andExpect(jsonPath("$.categoriaId").value(videoPost.getCategoriaId()));
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Video inválido")
        public void givenVideo_WhenPutInvalidVideo_Then400() throws Exception {

                postVideo();
                videoPut.titulo(
                                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                                .descricao(null)
                                .url("url");
                getMockMvc().perform(
                                put(urlId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("2001"))
                                .andExpect(jsonPath("$.fieldList", hasSize(2)));
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Video válido por Id inválido")
        public void givenVideo_WhenPutValidVideoByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                put(urlInvalidId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Deleta Video")
        public void givenVideo_WhenDeleteVideo_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                delete(urlId, videoId))
                                .andDo(print())
                                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Erro ao tentar deletar Video por Id inválido")
        public void givenVideo_WhenDeleteVideoByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                delete(urlInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "Unable to find br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

}
