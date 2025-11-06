package com.epikason.miaapp2501exam

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var title: String,
    var description: String? = null,
    var dueDate: Long? = null,
    var isCompleted: Boolean = false

)
