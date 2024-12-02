package com.bootcamp.todoList.service;

import com.bootcamp.todoList.model.Todo;
import com.bootcamp.todoList.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    @Mock
    TodoRepository mockTodoRepository = mock(TodoRepository.class);

    private TodoService buildService() {
        return new TodoService(mockTodoRepository);
    }

    @Test
    void should_return_todo_list_when_get_all_given_todo_list() {
    
        //Given
        List<Todo> expectedTodoList = List.of(new Todo(1, "Task 123", false));
        when(mockTodoRepository.findAll()).thenReturn(expectedTodoList);
        TodoService todoService = buildService();
        
        //When
        List<Todo> todoList = todoService.findAll();
        
        //Then
        assertEquals(1, todoList.size());
        assertEquals(expectedTodoList.get(0).getId(), todoList.get(0).getId());
        assertEquals(expectedTodoList.get(0).getText(), todoList.get(0).getText());
        assertEquals(expectedTodoList.get(0).getDone(), todoList.get(0).getDone());
    }

    @Test
    void should_create_todo_when_create_given_todo() {

        //Given
        TodoService todoService = buildService();
        Todo newTodo = new Todo(1, "Task new Todo", false);

        //When
        todoService.create(newTodo);

        //Then
        verify(mockTodoRepository).save(newTodo);

    }
}
