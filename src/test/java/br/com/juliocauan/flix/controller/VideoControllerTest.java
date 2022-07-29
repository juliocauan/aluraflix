package br.com.juliocauan.flix.controller;

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
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import br.com.juliocauan.flix.config.TestContext;
import br.com.juliocauan.openapi.model.LoginForm;
import br.com.juliocauan.openapi.model.Token;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(3)
@TestInstance(Lifecycle.PER_CLASS)
public class VideoControllerTest extends TestContext {

        private final String url = "/videos";
        private final String urlWithId = "/videos/{videoId}";
        private final String urlWithInvalidId = "/videos/0";
        private final String urlFree = "/videos/free";
        private final String tokenUrl = "/auth";
        private final Integer categoryDefaultId = 1;
        private final Integer freePageSize = 5;
        
        @Value("${spring.data.web.pageable.default-page-size}")
        private Integer pageSize;
        private VideoPost videoPost = new VideoPost();
        private VideoPut videoPut = new VideoPut();
        private List<Integer> videoIdList = new ArrayList<>();
        private Integer lastVideoId = 0;
        private String token;

        private ResultActions postVideo() throws Exception {
                ResultActions result = getMockMvc().perform(
                                post(url)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)));
                String videoGetString = result.andReturn().getResponse().getContentAsString();
                lastVideoId = getObjectMapper()
                                .readValue(videoGetString, VideoGet.class)
                                .getId();
                videoIdList.add(lastVideoId);
                return result;
        }

        @BeforeAll
        public void start() throws Exception {
                LoginForm login = new LoginForm().email("julio@test.com").pswd("123456");
                String content = getMockMvc().perform(
                                post(tokenUrl)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(login)))
                                .andReturn().getResponse().getContentAsString();
                Token aux = getObjectMapper().readValue(content, Token.class);
                token = String.format("%s %s", aux.getType().getValue(), aux.getToken());
        }

        @BeforeEach
        public void setup() throws Exception {
                videoPost
                                .description("Descrição teste POST")
                                .title("Título teste POST")
                                .url("https://www.testePOST.com/" + lastVideoId)
                                .categoryId(categoryDefaultId);
                videoPut
                                .description("Descrição teste PUT")
                                .title("Título teste PUT")
                                .url("https://www.testePUT.com/" + lastVideoId)
                                .categoryId(categoryDefaultId);
        }

        @AfterAll
        private void deleteAllVideos() {
                videoIdList.forEach(videoId -> {
                        try {
                                getMockMvc().perform(delete(urlWithId, videoId).header("Authorization", token));
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
                                .andExpect(jsonPath("$.description").value(videoPost.getDescription()))
                                .andExpect(jsonPath("$.title").value(videoPost.getTitle()))
                                .andExpect(jsonPath("$.url").value(videoPost.getUrl()))
                                .andExpect(jsonPath("$.categoryId").value(videoPost.getCategoryId()));
        }

        @Test
        @DisplayName("Cadastra um Video com Categoria padrão quando insere Categoria nula")
        public void givenVideo_WhenPostVideoWithNullCategoryId_ThenCategoryDefaultId201() throws Exception {

                videoPost.categoryId(null);
                postVideo()
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(lastVideoId))
                                .andExpect(jsonPath("$.description").value(videoPost.getDescription()))
                                .andExpect(jsonPath("$.title").value(videoPost.getTitle()))
                                .andExpect(jsonPath("$.url").value(videoPost.getUrl()))
                                .andExpect(jsonPath("$.categoryId").value(categoryDefaultId));
        }

        @Test
        @DisplayName("Erro ao tentar cadastrar um Video inválido")
        public void givenVideo_WhenPostInvalidVideo_Then400() throws Exception {

                videoPost.title(
                                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                                .description(null)
                                .url("url");
                getMockMvc().perform(
                                post(url)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("201"))
                                .andExpect(jsonPath("$.fieldList", hasSize(3)));
        }

        @Test
        @DisplayName("Erro ao tentar cadastrar um Video com CategoriaId inválido")
        public void givenVideo_WhenPostVideoWithInvalidCategoriaId_Then400() throws Exception {

                videoPost.categoryId(0);
                getMockMvc().perform(
                                post(url)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("202"))
                                .andExpect(jsonPath("$.message").value(
                                                "POST/PUT method: Unable to find br.com.juliocauan.flix.infrastructure.model.application.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Busca lista de todos os Videos")
        public void givenVideo_WhenGetAllVideos_Then200() throws Exception {
                postVideo();
                String page = String.valueOf(videoIdList.size() / pageSize);
                String content = String.format("$.content[%d].",
                                videoIdList.size() - 1 - Integer.valueOf(page) * pageSize);
                getMockMvc().perform(
                                get(url)
                                                .header("Authorization", token)
                                                .queryParam("page", page))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath(content + "id").value(lastVideoId))
                                .andExpect(jsonPath(content + "title").value(videoPost.getTitle()))
                                .andExpect(jsonPath(content + "description").value(videoPost.getDescription()))
                                .andExpect(jsonPath(content + "url").value(videoPost.getUrl()))
                                .andExpect(jsonPath(content + "categoryId").value(videoPost.getCategoryId()));
        }

        @Test
        @DisplayName("Busca lista de Videos por Titulo")
        public void givenVideo_WhenGetVideosByTitle_Then200() throws Exception {
                postVideo();
                String page = String.valueOf(videoIdList.size() / pageSize);
                String content = String.format("$.content[%d].",
                                videoIdList.size() - 1 - Integer.valueOf(page) * pageSize);
                getMockMvc().perform(
                                get(url)
                                                .header("Authorization", token)
                                                .queryParam("search", "tes")
                                                .queryParam("page", page))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath(content + "id").value(lastVideoId))
                                .andExpect(jsonPath(content + "title").value(videoPost.getTitle()))
                                .andExpect(jsonPath(content + "description").value(videoPost.getDescription()))
                                .andExpect(jsonPath(content + "url").value(videoPost.getUrl()))
                                .andExpect(jsonPath(content + "categoryId").value(videoPost.getCategoryId()));
        }

        @Test
        @DisplayName("Busca lista de Videos por Titulo não presente")
        public void givenVideo_WhenGetVideosByNotPresentTitle_Then200() throws Exception {
                postVideo();
                getMockMvc().perform(
                                get(url)
                                                .header("Authorization", token)
                                                .param("search", "jogos"))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content").isEmpty());
        }

        @Test
        @DisplayName("Busca lista de todos os Videos gratuitos")
        public void givenVideo_WhenGetAllFreeVideos_Then200() throws Exception {
                postVideo();
                getMockMvc().perform(
                                get(urlFree))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.pageable.pageSize").value(freePageSize))
                                .andExpect(jsonPath("$.content[0].id").value("1"));
        }

        @Test
        @DisplayName("Busca Video por Id")
        public void givenVideo_WhenGetVideoById_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                get(urlWithId, lastVideoId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastVideoId))
                                .andExpect(jsonPath("$.title").value(videoPost.getTitle()))
                                .andExpect(jsonPath("$.description").value(videoPost.getDescription()))
                                .andExpect(jsonPath("$.url").value(videoPost.getUrl()))
                                .andExpect(jsonPath("$.categoryId").value(videoPost.getCategoryId()));
        }

        @Test
        @DisplayName("Erro ao tentar buscar Video por Id inválido")
        public void givenVideo_WhenGetVideoByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                get(urlWithInvalidId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.flix.infrastructure.model.application.VideoEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Atualiza Video válido")
        public void givenVideo_WhenPutValidVideo_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                put(urlWithId, lastVideoId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastVideoId))
                                .andExpect(jsonPath("$.description").value(videoPut.getDescription()))
                                .andExpect(jsonPath("$.title").value(videoPut.getTitle()))
                                .andExpect(jsonPath("$.url").value(videoPut.getUrl()))
                                .andExpect(jsonPath("$.categoryId").value(videoPost.getCategoryId()));
        }

        @Test
        @DisplayName("Atualiza Video com Url e CategoriaId nulos")
        public void givenVideo_WhenPutVideoWithNullUrlAndCategoriaId_Then200() throws Exception {

                postVideo();
                String url = videoPost.getUrl();
                Integer categoryId = videoPost.getCategoryId();
                videoPut.url(null).categoryId(null);
                getMockMvc().perform(
                                put(urlWithId, lastVideoId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastVideoId))
                                .andExpect(jsonPath("$.description").value(videoPut.getDescription()))
                                .andExpect(jsonPath("$.title").value(videoPut.getTitle()))
                                .andExpect(jsonPath("$.url").value(url))
                                .andExpect(jsonPath("$.categoryId").value(categoryId));
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Video inválido")
        public void givenVideo_WhenPutInvalidVideo_Then400() throws Exception {
                postVideo();
                videoPut.title(
                                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                                .description(null)
                                .url("url");
                getMockMvc().perform(
                                put(urlWithId, lastVideoId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("201"))
                                .andExpect(jsonPath("$.fieldList", hasSize(2)));
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Video válido por Id inválido")
        public void givenVideo_WhenPutValidVideoByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                put(urlWithInvalidId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.flix.infrastructure.model.application.VideoEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Video com Categoria Id inválido")
        public void givenVideo_WhenPutVideoWithInvalidCategoriaId_Then400() throws Exception {

                postVideo();
                videoPut.categoryId(0);
                getMockMvc().perform(
                                put(urlWithId, lastVideoId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("202"))
                                .andExpect(jsonPath("$.message").value(
                                                "POST/PUT method: Unable to find br.com.juliocauan.flix.infrastructure.model.application.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Deleta Video")
        public void givenVideo_WhenDeleteVideo_Then200() throws Exception {

                postVideo();
                getMockMvc().perform(
                                delete(urlWithId, lastVideoId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(status().isOk());
                videoIdList.remove(videoIdList.size() - 1);
        }

        @Test
        @DisplayName("Erro ao tentar deletar Video por Id inválido")
        public void givenVideo_WhenDeleteVideoByInvalidId_Then404() throws Exception {

                getMockMvc().perform(
                                delete(urlWithInvalidId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.flix.infrastructure.model.application.VideoEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

}
