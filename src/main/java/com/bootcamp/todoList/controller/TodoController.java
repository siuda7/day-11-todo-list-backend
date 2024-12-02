package com.bootcamp.todoList.controller;


import com.bootcamp.todoList.model.Todo;
import com.bootcamp.todoList.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getTodos() {
        return todoService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.create(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Integer id, @RequestBody Todo company) {
        return todoService.update(id, company);
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Integer id) {
        return todoService.getTodoById(id);
    }
}
