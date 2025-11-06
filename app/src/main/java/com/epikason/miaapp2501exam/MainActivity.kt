package com.epikason.miaapp2501exam

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.epikason.miaapp2501exam.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var taskDao: TaskDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskDao = TaskDatabase.getDatabase(this).taskDao()

        adapter = TaskAdapter(
            onItemClick = { task ->
                val intent = Intent(this, AddEditTaskActivity::class.java)
                intent.putExtra("task_id", task.id)
                startActivity(intent)
            },
            onDeleteClick = { task ->
                lifecycleScope.launch {
                    taskDao.delete(task)
                }
            },
            onCompletedChanged = { task, isCompleted ->
                lifecycleScope.launch {
                    taskDao.update(task.copy(isCompleted = isCompleted))
                }
            }
        )
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditTaskActivity::class.java))
        }

        taskDao.getAllTasksSortedByDueDate().observe(this) { tasks ->
            adapter.submitList(tasks)
        }
    }
}
