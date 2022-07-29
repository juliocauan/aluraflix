package br.com.juliocauan.flix.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.MediaType;

import br.com.juliocauan.flix.config.TestContext;
import br.com.juliocauan.openapi.model.LoginForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
@TestInstance(Lifecycle.PER_CLASS)
public class AuthenticationControllerTest extends TestContext {

    private final String url = "/auth";
    private final String categoryUrl = "/categories/{categoryId}";
    private final Integer categoryDefaultId = 1;
    private LoginForm loginForm = new LoginForm();

    @BeforeEach
    public void setup() {
        loginForm.email("julio@test.com").pswd("123456");
    }

    @Test
    @DisplayName("Retorna o token")
    public void givenAuth_WhenPostValidLoginForm_Then200() throws Exception {

        getMockMvc().perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(loginForm)))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    @DisplayName("Erro ao tentar retornar token com User inválido")
    public void givenAuth_WhenPostInvalidUser_Then400() throws Exception {
        loginForm.email("juli@tes.com");
        getMockMvc().perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(loginForm)))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("Invalid User or Password!"));
    }

    @Test
    @DisplayName("Erro ao tentar retornar token com Password inválido")
    public void givenAuth_WhenPostInvalidPassword_Then400() throws Exception {
        loginForm.pswd("1234");
        getMockMvc().perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(loginForm)))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("Invalid User or Password!"));
    }

    @Test
    @DisplayName("Erro ao tentar acessar url com token inválid")
    public void givenAuth_WhenGetWithInvalidToken_Then400() throws Exception {
        getMockMvc().perform(
                get(categoryUrl, categoryDefaultId)
                        .header("Authorization", "saofkasodkasokdaogmvadksodma"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("501"))
                .andExpect(jsonPath("$.message").value("Bad Credentials!"));
    }

}
