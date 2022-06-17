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
import br.com.juliocauan.openapi.model.CategoryGet;
import br.com.juliocauan.openapi.model.CategoryPost;
import br.com.juliocauan.openapi.model.CategoryPut;
import br.com.juliocauan.openapi.model.Color;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
@TestInstance(Lifecycle.PER_CLASS)
public class CategoryControllerTest extends TestContext {

        private final String url = "/categories";
        private final String urlWithId = "/categories/{categoryId}";
        private final String urlWithInvalidId = "/categories/0";
        private final String urlGetVideoListByCategoryId = "/categories/{categoryId}/videos";
        private final String urlGetVideoListByCategoryInvalidId = "/categories/0/videos";
        private final Integer categoryDefaultId = 1;

        private CategoryPost categoryPost = new CategoryPost();
        private CategoryPut categoryPut = new CategoryPut();
        private List<Integer> categoryIdList = new ArrayList<>();
        private Integer lastCategoryId = 0;
        private VideoGet videoGet = new VideoGet();

        private ResultActions postCategory() throws Exception {
                ResultActions result = getMockMvc().perform(
                                post(url)
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
                VideoPost videoPost = new VideoPost()
                                .description("Descricao teste POST in Category")
                                .title("Titulo teste POST in Category")
                                .url("https://www.testePOSTInCategory.com/")
                                .categoryId(categoryDefaultId);
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
                categoryIdList.forEach(categoriaId -> {
                        try {
                                getMockMvc().perform(delete(urlWithId, categoriaId));
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
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoryPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("2001"))
                                .andExpect(jsonPath("$.fieldList", hasSize(2)));
        }

        @Test
        @DisplayName("Busca lista de Categorys")
        public void givenCategory_WhenGetAllCategorys_Then200() throws Exception {
                postCategory();
                String content = String.format("$.content[%d].", categoryIdList.size());
                getMockMvc().perform(
                                get(url))
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
                                get(urlWithId, lastCategoryId))
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
                                get(urlWithInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Busca lista de Videos por Category")
        public void givenCategory_WhenGetVideoListByCategory_Then200() throws Exception {
                getMockMvc().perform(
                                get(urlGetVideoListByCategoryId, categoryDefaultId))
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
                                get(urlGetVideoListByCategoryInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Atualiza Category válido")
        public void givenCategory_WhenPutValidCategory_Then200() throws Exception {
                postCategory();
                getMockMvc().perform(
                                put(urlWithId, lastCategoryId)
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
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoryPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("2001"))
                                .andExpect(jsonPath("$.fieldList", hasSize(1)));
        }

        @Test
        @DisplayName("Erro ao tentar atualizar Category válido por Id inválido")
        public void givenCategory_WhenPutValidCategoryByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                put(urlWithInvalidId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(categoryPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        @DisplayName("Deleta Category")
        public void givenCategory_WhenDeleteCategory_Then200() throws Exception {
                postCategory();
                getMockMvc().perform(
                                delete(urlWithId, lastCategoryId))
                                .andDo(print())
                                .andExpect(status().isOk());
                categoryIdList.remove(categoryIdList.size()-1);
        }

        @Test
        @DisplayName("Erro ao tentar deletar Category por Id inválido")
        public void givenCategory_WhenDeleteCategoryByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                delete(urlWithInvalidId))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("1001"))
                                .andExpect(jsonPath("$.message").value(
                                                "GET/DELETE method: Unable to find br.com.juliocauan.aluraflix.infrastructure.model.CategoryEntity with id 0"))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

}
