package com.bootcamp.todoList.service;


import com.bootcamp.todoList.model.Todo;
import com.bootcamp.todoList.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Integer id, Todo todo) {
        final Todo updateTodo = todoRepository.findById(id).orElse(null);
        return updateTodo == null ? null : todoRepository.save(todo);
    }

    public Todo getTodoById(Integer id) {
        return todoRepository.findById(id).orElse(null);
    }

    public Todo deleteTodo(Integer id) {
        final Todo deletedTodo = todoRepository.findById(id).orElse(null);
        todoRepository.deleteById(id);
        return deletedTodo;
    }
}
