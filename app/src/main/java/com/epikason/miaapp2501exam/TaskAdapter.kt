package com.epikason.miaapp2501exam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epikason.miaapp2501exam.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val onItemClick: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit,
    private val onCompletedChanged: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList = listOf<Task>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun submitList(list: List<Task>) {
        taskList = list
        notifyDataSetChanged()
    }

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {


        val task = taskList[position]
        holder.binding.apply {
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvDueDate.text = task.dueDate?.let {
                "Due: ${dateFormat.format(Date(it))}"
            } ?: "No due date"
            cbCompleted.isChecked = task.isCompleted

            btnEdit.setOnClickListener {
                onItemClick(task)
            }
            btnDelete.setOnClickListener {
                onDeleteClick(task)
                true
            }
            cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked != task.isCompleted) {
                    onCompletedChanged(task, isChecked)
                }
            }
        }
    }

    override fun getItemCount(): Int = taskList.size
}
