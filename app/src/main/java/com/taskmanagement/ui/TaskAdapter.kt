package com.taskmanagement.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taskmanagement.R
import com.taskmanagement.database.TaskEntity

class TaskAdapter(
    private val onEdit: (TaskEntity) -> Unit,
    private val onDelete: (TaskEntity) -> Unit
) : ListAdapter<TaskEntity, TaskAdapter.TaskViewHolder>(Diff) {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitle)
        val desc: TextView = view.findViewById(R.id.tvDesc)
        val edit: ImageView = view.findViewById(R.id.ivEdit)
        val delete: ImageView = view.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TaskViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false))

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.title.text = task.title
        holder.desc.text = task.description
        holder.edit.setOnClickListener { onEdit(task) }
        holder.delete.setOnClickListener { onDelete(task) }
    }

    companion object {
        val Diff = object : DiffUtil.ItemCallback<TaskEntity>() {
            override fun areItemsTheSame(old: TaskEntity, new: TaskEntity) =
                old.id == new.id

            override fun areContentsTheSame(old: TaskEntity, new: TaskEntity) =
                old == new
        }
    }
}