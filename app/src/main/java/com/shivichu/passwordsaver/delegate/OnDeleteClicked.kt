package com.shivichu.passwordsaver.delegate

import com.shivichu.passwordsaver.database.entity.Todo


interface OnDeleteClicked {
    fun onDelete(position : Int, todo : Todo)
    fun onItemClicked(position : Int, todo : Todo)
}