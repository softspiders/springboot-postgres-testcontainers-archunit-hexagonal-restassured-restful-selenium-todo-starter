package org.softspiders.todos.adapter.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.softspiders.todos.adapter.rest.dto.CreateTodoDto;
import org.softspiders.todos.adapter.rest.dto.TodoDetailsDto;
import org.softspiders.todos.adapter.rest.dto.TodoDto;
import org.softspiders.todos.domain.model.todo.TodoDomainModel;
import org.softspiders.todos.domain.port.api.todo.TodoServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.softspiders.todos.adapter.rest.mapper.TodoRestMapper.TODO_REST_MAPPER;

@Tag(name = "todo", description = "Todo tasks")
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {
  private final TodoServicePort todoServicePort;

  @Operation(summary = "Get all available todos")
  @GetMapping
  public Iterable<TodoDto> getAll() {
    return TODO_REST_MAPPER.toDtoList(todoServicePort.getAllTodos());
  }

  @Operation(summary = "Find the todo by id")
  @GetMapping("{id}")
  public TodoDetailsDto getById(@Parameter(description = "Id of the todo") @PathVariable String id) {
    return TODO_REST_MAPPER.toTodoDetailsDto(todoServicePort.getTodoById(id));
  }

  @Operation(summary = "Save a new todo of the given todoId")
  @PostMapping
  public ResponseEntity<TodoDto> create(@Valid @RequestBody CreateTodoDto todo) {
    TodoDomainModel todoDomainModel = todoServicePort.create(TODO_REST_MAPPER.toDomainModel(todo));
    TodoDto todoDto = TODO_REST_MAPPER.toDto(todoDomainModel);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(todoDto);
  }

  @Operation(summary = "Update the todo")
  @PatchMapping("/{id}")
  public TodoDto update(@Parameter(description = "Id of the todo") @PathVariable String id, @Valid @RequestBody TodoDto todo) {
    return TODO_REST_MAPPER.toDto(todoServicePort.update(id, TODO_REST_MAPPER.toDomainModel(todo)));
  }

  @Operation(summary = "Delete the todo")
  @DeleteMapping("/{id}")
  public void deleteById(@Parameter(description = "Id of the todo") @PathVariable String id) {
    todoServicePort.deleteById(id);
  }

  @Operation(summary = "Delete all todos")
  @DeleteMapping
  public void deleteAll() {
    todoServicePort.deleteAll();
  }
}
