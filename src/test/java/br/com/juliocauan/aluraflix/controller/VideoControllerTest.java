package br.com.juliocauan.aluraflix.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.juliocauan.openapi.model.VideoPost;

@SpringBootTest
@AutoConfigureMockMvc
public class VideoControllerTest{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final String url = "/videos";
    
    @Test
    @DisplayName("Cadastra um Video válido")
    public void givenVideo_WhenPostValidVideo_Then201() throws Exception{

        VideoPost videoPost = new VideoPost();
        videoPost.setDescricao("Descrição teste");
        videoPost.setTitulo("Título teste");
        videoPost.setUrl("https://www.teste.com/");
        mockMvc.perform(
            post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(videoPost))
        ).andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
        .andExpect(jsonPath("$.descricao").value(videoPost.getDescricao()))
        .andExpect(jsonPath("$.titulo").value(videoPost.getTitulo()))
        .andExpect(jsonPath("$.url").value(videoPost.getUrl()));
    }

}
