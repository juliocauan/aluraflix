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
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import br.com.juliocauan.flix.config.TestContext;
import br.com.juliocauan.openapi.model.CategoryGet;
import br.com.juliocauan.openapi.model.CategoryPost;
import br.com.juliocauan.openapi.model.CategoryPut;
import br.com.juliocauan.openapi.model.Color;
import br.com.juliocauan.openapi.model.LoginForm;
import br.com.juliocauan.openapi.model.Token;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(4)
@TestInstance(Lifecycle.PER_CLASS)
public class CategoryControllerTest extends TestContext {

        private final String url = "/categories";
        private final String urlWithId = "/categories/{categoryId}";
        private final String urlWithInvalidId = "/categories/0";
        private final String urlGetVideoListByCategoryId = "/categories/{categoryId}/videos";
        private final String urlGetVideoListByCategoryInvalidId = "/categories/0/videos";
        private final String tokenUrl = "/auth";
        private final Integer categoryDefaultId = 1;

        private CategoryPost categoryPost = new CategoryPost();
        private CategoryPut categoryPut = new CategoryPut();
        private List<Integer> categoryIdList = new ArrayList<>();
        private Integer lastCategoryId = 0;
        private VideoGet videoGet = new VideoGet();
        private String token;

        private ResultActions postCategory() throws Exception {
                ResultActions result = getMockMvc().perform(
                                post(url)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoryPost)));
                String videoGetString = result.andReturn().getResponse().getContentAsString();
                lastCategoryId = getObjectMapper()
                                .readValue(videoGetString, CategoryGet.class)
                                .getId();
                categoryIdList.add(lastCategoryId);
                return result;
        }

        @BeforeAll
        private void init() throws Exception {

                LoginForm login = new LoginForm().email("julio@test.com").pswd("123456");
                String content = getMockMvc().perform(
                                post(tokenUrl)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(login)))
                                .andReturn().getResponse().getContentAsString();
                Token aux = getObjectMapper().readValue(content, Token.class);
                token = String.format("%s %s", aux.getType().getValue(), aux.getToken());

                VideoPost videoPost = new VideoPost()
                                .description("Descricao teste POST in Category")
                                .title("Titulo teste POST in Category")
                                .url("https://www.testePOSTInCategory.com/")
                                .categoryId(categoryDefaultId);
                content = getMockMvc().perform(
                                post("/videos")
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(videoPost)))
                                .andReturn().getResponse().getContentAsString();

                videoGet = getObjectMapper().readValue(content, VideoGet.class);
        }

        @AfterAll
        private void clean() throws Exception {
                getMockMvc().perform(delete("/videos/{videoId}", videoGet.getId()));
                categoryIdList.forEach(categoriaId -> {
                        try {
                                getMockMvc().perform(delete(urlWithId, categoriaId).header("Authorization", token));
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });
        }

        @BeforeEach
        public void setup() {
                categoryPost
                                .title("Título teste POST")
                                .color(Color.TEAL);
                categoryPut
                                .title("Título teste PUT")
                                .color(Color.WHITE);
        }

        @Test
        @DisplayName("Cadastra um Category válido")
        public void givenCategory_WhenPostValidCategory_Then201() throws Exception {
                postCategory()
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(lastCategoryId))
                                .andExpect(jsonPath("$.title").value(categoryPost.getTitle()))
                                .andExpect(jsonPath("$.color").value(categoryPost.getColor().getValue()));
        }

        @Test
        @DisplayName("Erro ao tentar cadastrar um Category inválido")
        public void givenCategory_WhenPostInvalidCategory_Then400() throws Exception {

                categoryPost.title(
                                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                                .color(null);
                getMockMvc().perform(
                                post(url)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoryPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("201"))
                                .andExpect(jsonPath("$.fieldList", hasSize(2)));
        }

        @Test
        @DisplayName("Busca lista de Categorys")
        public void givenCategory_WhenGetAllCategorys_Then200() throws Exception {
                postCategory();
                String content = String.format("$.content[%d].", categoryIdList.size());
                getMockMvc().perform(
                                get(url)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath(content + "id").value(lastCategoryId))
                                .andExpect(jsonPath(content + "title").value(categoryPost.getTitle()))
                                .andExpect(jsonPath(content + "color").value(categoryPost.getColor().getValue()))
                                .andExpect(jsonPath("$.numberOfElements").value(categoryIdList.size() + 1));
        }

        @Test
        @DisplayName("Busca Category por Id")
        public void givenCategory_WhenGetCategoryById_Then200() throws Exception {
                postCategory();
                getMockMvc().perform(
                                get(urlWithId, lastCategoryId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastCategoryId))
                                .andExpect(jsonPath("$.title").value(categoryPost.getTitle()))
                                .andExpect(jsonPath("$.color").value(categoryPost.getColor().getValue()));
        }

        @Test
        @DisplayName("Erro ao tentar buscar Category por Id inválido")
        public void givenCategory_WhenGetCategoryByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                get(urlWithInvalidId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.flix.infrastructure.model.application.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Busca lista de Videos por Category")
        public void givenCategory_WhenGetVideoListByCategory_Then200() throws Exception {
                getMockMvc().perform(
                                get(urlGetVideoListByCategoryId, categoryDefaultId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value(videoGet.getId()))
                                .andExpect(jsonPath("$.content[0].title").value(videoGet.getTitle()))
                                .andExpect(jsonPath("$.content[0].description").value(videoGet.getDescription()))
                                .andExpect(jsonPath("$.content[0].url").value(videoGet.getUrl()))
                                .andExpect(jsonPath("$.content[0].categoryId").value(videoGet.getCategoryId()))
                                .andExpect(jsonPath("$.numberOfElements").value(1));
        }

        @Test
        @DisplayName("Erro ao tentar buscar lista de Videos por Category com Id inválido")
        public void givenCategory_WhenGetVideoListByInvalidCategoryId_Then404() throws Exception {
                getMockMvc().perform(
                                get(urlGetVideoListByCategoryInvalidId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.flix.infrastructure.model.application.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Atualiza Category válido")
        public void givenCategory_WhenPutValidCategory_Then200() throws Exception {
                postCategory();
                getMockMvc().perform(
                                put(urlWithId, lastCategoryId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoryPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastCategoryId))
                                .andExpect(jsonPath("$.title").value(categoryPut.getTitle()))
                                .andExpect(jsonPath("$.color").value(categoryPut.getColor().getValue()));
        }

        @Test
        @DisplayName("Atualiza Category com Titulo nulo")
        public void givenCategory_WhenPutCategoryWithTituloNull_Then200() throws Exception {
                postCategory();
                String title = categoryPost.getTitle();
                categoryPut.title(null);
                getMockMvc().perform(
                                put(urlWithId, lastCategoryId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoryPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastCategoryId))
                                .andExpect(jsonPath("$.title").value(title))
                                .andExpect(jsonPath("$.color").value(categoryPut.getColor().getValue()));
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Category inválido")
        public void givenCategory_WhenPutInvalidCategory_Then400() throws Exception {
                postCategory();
                categoryPut.title("a").color(null);
                getMockMvc().perform(
                                put(urlWithId, lastCategoryId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoryPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("201"))
                                .andExpect(jsonPath("$.fieldList", hasSize(1)));
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Category válido por Id inválido")
        public void givenCategory_WhenPutValidCategoryByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                put(urlWithInvalidId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoryPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.flix.infrastructure.model.application.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Deleta Category")
        public void givenCategory_WhenDeleteCategory_Then200() throws Exception {
                postCategory();
                getMockMvc().perform(
                                delete(urlWithId, lastCategoryId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(status().isOk());
                categoryIdList.remove(categoryIdList.size() - 1);
        }

        @Test
        @DisplayName("Erro ao tentar deletar Category por Id inválido")
        public void givenCategory_WhenDeleteCategoryByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                delete(urlWithInvalidId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.flix.infrastructure.model.application.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

}
