package com.epikason.miaapp2501exam

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.epikason.miaapp2501exam.databinding.ActivityAddEditTaskBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTaskBinding
    private var selectedDate: Long? = null
    private var taskId: Int? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskDao = TaskDatabase.getDatabase(this).taskDao()

        taskId = intent.getIntExtra("task_id", -1).takeIf { it != -1 }

        if (taskId != null) {
            lifecycleScope.launch {
                val task = taskDao.getTaskById(taskId!!)
                task?.let {
                    binding.etTitle.setText(it.title)
                    binding.etDescription.setText(it.description)
                    selectedDate = it.dueDate
                    binding.tvPickedDate.text = it.dueDate?.let { d -> dateFormat.format(Date(d)) } ?: "No date selected"
                    binding.cbCompleted.isChecked = it.isCompleted
                }
            }
        }

        binding.btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val picker = DatePickerDialog(
                this,
                { _, y, m, d ->
                    calendar.set(y, m, d)
                    selectedDate = calendar.timeInMillis
                    binding.tvPickedDate.text = dateFormat.format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            picker.show()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val desc = binding.etDescription.text.toString().trim()
            val completed = binding.cbCompleted.isChecked

            if (title.isEmpty()) {
                Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if (taskId != null) {
                    taskDao.update(Task(taskId!!, title, desc, selectedDate, completed))
                    Toast.makeText(this@AddEditTaskActivity, "Task Updated", Toast.LENGTH_SHORT).show()
                } else {
                    taskDao.insertTask(Task(
                        id = null,
                        title = title,
                        description = desc,
                        dueDate = selectedDate,
                        isCompleted = completed
                    ))
                    Toast.makeText(this@AddEditTaskActivity, "Task Added", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }
    }
}
