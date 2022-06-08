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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import br.com.juliocauan.aluraflix.config.TestContext;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
@TestInstance(Lifecycle.PER_CLASS)
public class VideoControllerTest extends TestContext {

        private final String url = "/videos";
        private final String urlWithId = "/videos/{videoId}";
        private final String urlWithInvalidId = "/videos/0";
        private final Integer categoriaDefaultId = 1;
        private final Integer pageSize = 5;

        private VideoPost videoPost = new VideoPost();
        private VideoPut videoPut = new VideoPut();
        private List<Integer> videoIdList = new ArrayList<>();
        private Integer lastVideoId = 0;

        private ResultActions postVideo() throws Exception {
                ResultActions result = getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)));
                String videoGetString = result.andReturn().getResponse().getContentAsString();
                lastVideoId = getObjectMapper()
                                .readValue(videoGetString, VideoGet.class)
                                .getId();
                videoIdList.add(lastVideoId);
                return result;
        }

        @BeforeEach
        public void setup() throws Exception {
                videoPost
                                .descricao("Descrição teste POST")
                                .titulo("Título teste POST")
                                .url("https://www.testePOST.com/" + lastVideoId)
                                .categoriaId(categoriaDefaultId);
                videoPut
                                .descricao("Descrição teste PUT")
                                .titulo("Título teste PUT")
                                .url("https://www.testePUT.com/" + lastVideoId)
                                .categoriaId(categoriaDefaultId);
        }

        @AfterAll
        private void deleteAllVideos() {
                videoIdList.forEach(videoId -> {
                        try {
                                getMockMvc().perform(delete(urlWithId, videoId));
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });
        }

        @Test
        @DisplayName("Cadastra um Video válido")
        public void givenVideo_WhenPostValidVideo_Then201() throws Exception {

                postVideo()
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(lastVideoId))
                                .andExpect(jsonPath("$.descricao").value(videoPost.getDescricao()))
                                .andExpect(jsonPath("$.titulo").value(videoPost.getTitulo()))
                                .andExpect(jsonPath("$.url").value(videoPost.getUrl()))
                                .andExpect(jsonPath("$.categoriaId").value(videoPost.getCategoriaId()));
        }

        @Test
        @DisplayName("Cadastra um Video com Categoria padrão quando insere Categoria nula")
        public void givenVideo_WhenPostVideoWithNullCategoryId_ThenCategoryDefaultId201() throws Exception {

                videoPost.categoriaId(null);
                postVideo()
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(lastVideoId))
                                .andExpect(jsonPath("$.descricao").value(videoPost.getDescricao()))
                                .andExpect(jsonPath("$.titulo").value(videoPost.getTitulo()))
                                .andExpect(jsonPath("$.url").value(videoPost.getUrl()))
                                .andExpect(jsonPath("$.categoriaId").value(categoriaDefaultId));
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
        @DisplayName("Erro ao tentar cadastrar um Video com CategoriaId inválido")
        public void givenVideo_WhenPostVideoWithInvalidCategoriaId_Then400() throws Exception {

                videoPost.categoriaId(0);
                getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("4001"))
                                .andExpect(jsonPath("$.message").value(
                                                "POST/PUT method: Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Busca lista de todos os Videos")
        public void givenVideo_WhenGetAllVideos_Then200() throws Exception {
                postVideo();
                String page = String.valueOf(videoIdList.size() / pageSize);
                String content = String.format("$.content[%d].", videoIdList.size() - 1 - Integer.valueOf(page) * pageSize);
                getMockMvc().perform(
                                get(url)
                                        .queryParam("page", page))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath(content + "id").value(lastVideoId))
                                .andExpect(jsonPath(content + "titulo").value(videoPost.getTitulo()))
                                .andExpect(jsonPath(content + "descricao").value(videoPost.getDescricao()))
                                .andExpect(jsonPath(content + "url").value(videoPost.getUrl()))
                                .andExpect(jsonPath(content + "categoriaId").value(videoPost.getCategoriaId()));
        }

        @Test
        @DisplayName("Busca lista de Videos por Titulo")
        public void givenVideo_WhenGetVideosByTitle_Then200() throws Exception {
                postVideo();
                String page = String.valueOf(videoIdList.size() / pageSize);
                String content = String.format("$.content[%d].", videoIdList.size() - 1 - Integer.valueOf(page) * pageSize);
                getMockMvc().perform(
                                get(url)
                                                .queryParam("search", "tes")
                                                .queryParam("page", page))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath(content + "id").value(lastVideoId))
                                .andExpect(jsonPath(content + "titulo").value(videoPost.getTitulo()))
                                .andExpect(jsonPath(content + "descricao").value(videoPost.getDescricao()))
                                .andExpect(jsonPath(content + "url").value(videoPost.getUrl()))
                                .andExpect(jsonPath(content + "categoriaId").value(videoPost.getCategoriaId()));
        }

        @Test
        @DisplayName("Busca lista de Videos por Titulo não presente")
        public void givenVideo_WhenGetVideosByNotPresentTitle_Then200() throws Exception {
                postVideo();
                getMockMvc().perform(
                                get(url)
                                                .param("search", "jogos"))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content").isEmpty());
        }

        @Test
        @DisplayName("Busca Video por Id")
        public void givenVideo_WhenGetVideoById_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                get(urlWithId, lastVideoId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastVideoId))
                                .andExpect(jsonPath("$.titulo").value(videoPost.getTitulo()))
                                .andExpect(jsonPath("$.descricao").value(videoPost.getDescricao()))
                                .andExpect(jsonPath("$.url").value(videoPost.getUrl()))
                                .andExpect(jsonPath("$.categoriaId").value(videoPost.getCategoriaId()));
        }

        @Test
        @DisplayName("Erro ao tentar buscar Video por Id inválido")
        public void givenVideo_WhenGetVideoByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                get(urlWithInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Atualiza Video válido")
        public void givenVideo_WhenPutValidVideo_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                put(urlWithId, lastVideoId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastVideoId))
                                .andExpect(jsonPath("$.descricao").value(videoPut.getDescricao()))
                                .andExpect(jsonPath("$.titulo").value(videoPut.getTitulo()))
                                .andExpect(jsonPath("$.url").value(videoPut.getUrl()))
                                .andExpect(jsonPath("$.categoriaId").value(videoPost.getCategoriaId()));
        }

        @Test
        @DisplayName("Atualiza Video com Url e CategoriaId nulos")
        public void givenVideo_WhenPutVideoWithNullUrlAndCategoriaId_Then200() throws Exception {

                postVideo();
                String url = videoPost.getUrl();
                Integer categoriaId = videoPost.getCategoriaId();
                videoPut.url(null).categoriaId(null);
                getMockMvc().perform(
                                put(urlWithId, lastVideoId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastVideoId))
                                .andExpect(jsonPath("$.descricao").value(videoPut.getDescricao()))
                                .andExpect(jsonPath("$.titulo").value(videoPut.getTitulo()))
                                .andExpect(jsonPath("$.url").value(url))
                                .andExpect(jsonPath("$.categoriaId").value(categoriaId));
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
                                put(urlWithId, lastVideoId)
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
                                put(urlWithInvalidId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Video com Categoria Id inválido")
        public void givenVideo_WhenPutVideoWithInvalidCategoriaId_Then400() throws Exception {

                postVideo();
                videoPut.categoriaId(0);
                getMockMvc().perform(
                                put(urlWithId, lastVideoId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("4001"))
                                .andExpect(jsonPath("$.message").value(
                                                "POST/PUT method: Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Deleta Video")
        public void givenVideo_WhenDeleteVideo_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                delete(urlWithId, lastVideoId))
                                .andDo(print())
                                .andExpect(status().isOk());
                videoIdList.remove(videoIdList.size() - 1);
        }

        @Test
        @DisplayName("Erro ao tentar deletar Video por Id inválido")
        public void givenVideo_WhenDeleteVideoByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                delete(urlWithInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

}
