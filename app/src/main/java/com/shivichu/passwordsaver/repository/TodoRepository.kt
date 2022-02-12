package com.shivichu.passwordsaver.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shivichu.passwordsaver.database.AppDatabase
import com.shivichu.passwordsaver.database.entity.Todo
import com.shivichu.passwordsaver.model.authentication.TodoLoginRequestModel
import com.shivichu.passwordsaver.model.authentication.TodoLoginResponseModel
import com.shivichu.passwordsaver.retrofit.BaseRepository
import com.shivichu.passwordsaver.retrofit.api.ApiInterface
import com.shivichu.passwordsaver.utils.Coroutines

class TodoRepository(val appDatabase: AppDatabase, val apiInterface: ApiInterface) :  BaseRepository() {

    fun getAllTodo() : MutableLiveData<List<Todo>>{
        val data: MutableLiveData<List<Todo>> = MutableLiveData()
        Coroutines.io {
            val result = appDatabase.getTodoDAO().getAllTodo()
            if (result != null) {
                data.postValue(result)
            } else {
                data.postValue(emptyList())
            }
        }
        return data
    }

    fun insertTodo(todoItem: Todo) {
        Coroutines.io {
            appDatabase.getTodoDAO().insertTodo(todoItem)
        }
    }

    fun deleteTodo(todoItem: Todo) : MutableLiveData<Boolean> {
        var result : MutableLiveData<Boolean> = MutableLiveData()
        Coroutines.io {
            appDatabase.getTodoDAO().deleteTodo(todoItem)
        }.invokeOnCompletion {
            result.postValue(true)
        }
        return result
    }

    fun updateTodo(updatedTodoItem: Todo) : MutableLiveData<Boolean> {
        var result : MutableLiveData<Boolean> = MutableLiveData()
        Coroutines.io {
            appDatabase.getTodoDAO().updateTodo(updatedTodoItem)
        }.invokeOnCompletion {
            result.postValue(true)
        }
        return result
    }

    fun authenticateUser(email: String, password: String) : LiveData<TodoLoginResponseModel> {
        val returnData: MutableLiveData<TodoLoginResponseModel> = MutableLiveData()
        Coroutines.io {
            val data = TodoLoginRequestModel(email, password)
            val result = loginUserAPI(data)
            if (result != null) {
                returnData.postValue(result)
            } else {
                returnData.postValue(null)
            }
        }
        return returnData
    }

    suspend fun loginUserAPI(request: TodoLoginRequestModel): TodoLoginResponseModel? {
        return getSafeApiCall(
            call = { apiInterface.loginUser2(request) },
            errorMessage = "Error"
        )
    }
}