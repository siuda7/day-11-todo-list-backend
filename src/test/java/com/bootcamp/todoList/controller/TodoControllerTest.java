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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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


}
