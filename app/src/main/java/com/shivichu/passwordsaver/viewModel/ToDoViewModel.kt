package com.shivichu.passwordsaver.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shivichu.passwordsaver.database.entity.Todo
import com.shivichu.passwordsaver.model.authentication.TodoLoginResponseModel
import com.shivichu.passwordsaver.repository.TodoRepository

class ToDoViewModel(val todoRepository: TodoRepository) : ViewModel() {

    fun getAllTodo(): LiveData<List<Todo>> {
        return todoRepository.getAllTodo()
    }

    fun insertTodo(todoItem: Todo) {
        todoRepository.insertTodo(todoItem)
    }

    fun deleteTodo(todoItem: Todo) : LiveData<Boolean>{
        return todoRepository.deleteTodo(todoItem)
    }

    fun updateTodo(updatedTodoItem: Todo) : LiveData<Boolean>{
        return todoRepository.updateTodo(updatedTodoItem)
    }

    fun authenticateUser(email: String, password: String) : LiveData<TodoLoginResponseModel> {
        return todoRepository.authenticateUser(email, password)
    }
}