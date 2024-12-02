package com.bootcamp.todoList.exception;

public class TodoNotFoundException extends RuntimeException{

    private static final String TODO_NOT_FOUND = "Todo item not found";

    public TodoNotFoundException() {
        super(TODO_NOT_FOUND);
    }
}
