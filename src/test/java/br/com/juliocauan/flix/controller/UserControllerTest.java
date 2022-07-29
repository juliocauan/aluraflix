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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import br.com.juliocauan.flix.config.TestContext;
import br.com.juliocauan.openapi.model.LoginForm;
import br.com.juliocauan.openapi.model.RoleType;
import br.com.juliocauan.openapi.model.Token;
import br.com.juliocauan.openapi.model.UserGet;
import br.com.juliocauan.openapi.model.UserPost;
import br.com.juliocauan.openapi.model.UserPut;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(5)
@TestInstance(Lifecycle.PER_CLASS)
public class UserControllerTest extends TestContext {

        private final String url = "/users";
        private final String urlId = "/users/{userId}";
        private final String urlInvalidId = "/users/0";
        private final String getAndDeleteError = "GET/DELETE method: Unable to find br.com.juliocauan.flix.infrastructure.model.auth.UserEntity with id 0";
        private final String tokenUrl = "/auth";
        private final UserPost userPost = new UserPost();
        private final UserPut userPut = new UserPut();
        private final Short adminId = 1;
        private final Short clientId = 2;

        private Long lastUserId;
        private List<Long> userIdList = new ArrayList<>();
        private String token;

        private ResultActions postUser() throws Exception {
                ResultActions result = getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(userPost)));
                String userGetString = result.andReturn().getResponse().getContentAsString();
                lastUserId = getObjectMapper()
                                .readValue(userGetString, UserGet.class)
                                .getId();
                userIdList.add(lastUserId);
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

        }

        @BeforeEach
        public void setup() {
                List<RoleType> roles = new ArrayList<>();
                roles.add(RoleType.ADMIN);
                userPost.name("teste").email("teste2@teste.com").secret("secret");
                userPut.email("teste3@teste.com").secret("secret2").rolesAdd(roles).rolesRemove(null);
        }

        @AfterAll
        private void clean() throws Exception {
                userIdList.forEach(userId -> {
                        try {
                                getMockMvc().perform(delete(urlId, userId).header("Authorization", token));
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });
        }

        @Test
        public void givenUser_WhenPostValidUser_Then201() throws Exception {
                postUser()
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(lastUserId))
                                .andExpect(jsonPath("$.name").value(userPost.getName()))
                                .andExpect(jsonPath("$.email").value(userPost.getEmail()))
                                .andExpect(jsonPath("$.roles[0].id").value(clientId.toString()))
                                .andExpect(jsonPath("$.roles[0].value").value(RoleType.CLIENT.getValue()));
        }

        @Test
        public void givenUser_WhenPostInvalidUser_Then400() throws Exception {

                userPost.name("none").email("failetest").secret(null);

                getMockMvc().perform(
                                post(url)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(userPost)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("201"))
                                .andExpect(jsonPath("$.fieldList", hasSize(3)));
        }

        @Test
        public void givenUser_WhenGetAllUsers_Then200() throws Exception {
                postUser();
                String content = String.format("$.content[%d].", userIdList.size() + 1);
                getMockMvc().perform(
                                get(url)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath(content + "id").value(lastUserId))
                                .andExpect(jsonPath(content + "name").value(userPost.getName()))
                                .andExpect(jsonPath(content + "email").value(userPost.getEmail()))
                                .andExpect(jsonPath(content + "roles[0].id").value(clientId.toString()))
                                .andExpect(jsonPath(content + "roles[0].value").value(RoleType.CLIENT.getValue()))
                                .andExpect(jsonPath("$.numberOfElements").value(userIdList.size() + 2));
        }

        @Test
        public void givenUser_WhenGetUserById_Then200() throws Exception {
                postUser();
                getMockMvc().perform(
                                get(urlId, lastUserId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastUserId))
                                .andExpect(jsonPath("$.name").value(userPost.getName()))
                                .andExpect(jsonPath("$.email").value(userPost.getEmail()))
                                .andExpect(jsonPath("$.roles[0].id").value(clientId.toString()))
                                .andExpect(jsonPath("$.roles[0].value").value(RoleType.CLIENT.getValue()));
        }

        @Test
        public void givenUser_WhenGetUserByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                get(urlInvalidId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(getAndDeleteError))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        public void givenUser_WhenPutValidUser_Then200() throws Exception {
                postUser();
                getMockMvc().perform(
                                put(urlId, lastUserId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(userPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastUserId))
                                .andExpect(jsonPath("$.email").value(userPut.getEmail()))
                                .andExpect(jsonPath("$.roles", hasSize(2)));
        }

        @Test
        public void givenUser_WhenPutRepeatedRole_Then200() throws Exception {
                postUser();
                userPut.addRolesAddItem(RoleType.ADMIN).addRolesAddItem(RoleType.CLIENT);
                getMockMvc().perform(
                                put(urlId, lastUserId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(userPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastUserId))
                                .andExpect(jsonPath("$.email").value(userPut.getEmail()))
                                .andExpect(jsonPath("$.roles", hasSize(2)));
        }

        @Test
        public void givenUser_WhenPutRemoveARole_Then200() throws Exception {
                postUser();
                userPut.addRolesAddItem(RoleType.ADMIN).addRolesRemoveItem(RoleType.CLIENT);
                getMockMvc().perform(
                                put(urlId, lastUserId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(userPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastUserId))
                                .andExpect(jsonPath("$.email").value(userPut.getEmail()))
                                .andExpect(jsonPath("$.roles[0].id").value(adminId.toString()))
                                .andExpect(jsonPath("$.roles[0].value").value(RoleType.ADMIN.getValue()));
        }

        @Test
        public void givenUser_WhenPutUserWithEmailNull_Then200() throws Exception {
                postUser();
                String email = userPost.getEmail();
                userPut.email(null);
                getMockMvc().perform(
                                put(urlId, lastUserId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(userPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(lastUserId))
                                .andExpect(jsonPath("$.email").value(email))
                                .andExpect(jsonPath("$.roles", hasSize(2)));
        }

        @Test
        public void givenUser_WhenPutInvalidUser_Then400() throws Exception {
                postUser();
                userPut.email("a");
                getMockMvc().perform(
                                put(urlId, lastUserId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(userPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value("201"))
                                .andExpect(jsonPath("$.fieldList", hasSize(1)));
        }

        @Test
        public void givenUser_WhenPutValidUserByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                put(urlInvalidId)
                                                .header("Authorization", token)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(getObjectMapper().writeValueAsString(userPut)))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(getAndDeleteError))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

        @Test
        public void givenUser_WhenDeleteUser_Then200() throws Exception {
                postUser();
                getMockMvc().perform(
                                delete(urlId, lastUserId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(status().isOk());
                userIdList.remove(userIdList.size() - 1);
        }

        @Test
        public void givenUser_WhenDeleteUserByInvalidId_Then404() throws Exception {
                getMockMvc().perform(
                                delete(urlInvalidId)
                                                .header("Authorization", token))
                                .andDo(print())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value("101"))
                                .andExpect(jsonPath("$.message").value(getAndDeleteError))
                                .andExpect(jsonPath("$.fieldList").doesNotExist());
        }

}
