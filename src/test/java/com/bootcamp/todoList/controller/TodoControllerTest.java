package com.bootcamp.todoList.controller;

import com.bootcamp.todoList.model.Todo;
import com.bootcamp.todoList.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc client;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JacksonTester<List<Todo>> todoListJacksonTester;

    private JacksonTester<Todo> todoJacksonTester;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
        todoRepository.flush();

        todoRepository.save(new Todo(null, "Test 1", false));
        todoRepository.save(new Todo(null, "Test 2", false));
        todoRepository.save(new Todo(null, "Test 3", false));
    }

    @Test
    void should_return_all_todos_when_get_all_given_todo_list() throws Exception {
    
        //Given
        final List<Todo> expectedTodoList = todoRepository.findAll();
        
        //When
        final MvcResult result = client.perform(MockMvcRequestBuilders.get("/todos")).andReturn();

        //Then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        final List<Todo> fetchedTodos = todoListJacksonTester.parseObject(result.getResponse().getContentAsString());
        assertThat(fetchedTodos).hasSameSizeAs(expectedTodoList);
        assertThat(fetchedTodos)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedTodoList);
    
    }
    
    @Test
    void should_return_new_todo_when_create_given_new_todo() throws Exception {
    
        //Given
        String text = "Integration Test 1";
        Boolean done = false;

        String givenNewTodo = String.format(
                "{\"text\": \"%s\", \"done\": \"%s\"}",
                text,
                done
        );
        
        //When
        //Then
        client.perform(MockMvcRequestBuilders.post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(givenNewTodo)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(text))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(done));

        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(4);
        assertThat(todos.get(3).getText()).isEqualTo(text);
        assertThat(todos.get(3).getDone()).isEqualTo(done);
    
    }

    @Test
    void should_update_todo_when_update_given_update_todo() throws Exception {

        //Given
        Integer updatedId = 1;
        String updatedText = "New Updated text";
        Boolean updatedDone = true;

        String updateTodoRequestBody = String.format("{\"text\": \"%s\",  \"done\": \"%s\"}", updatedText, updatedDone);

        //When
        //Then
        client.perform(MockMvcRequestBuilders.put("/todos/" + updatedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTodoRequestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(updatedText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(updatedDone));

    }


}
