package com.taskmanagement.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taskmanagement.R
import com.taskmanagement.database.TaskEntity
import com.taskmanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        setupRecyclerView()
        observeTasks()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            onEdit = { showTaskDialog(it) },
            onDelete = { viewModel.delete(it) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeTasks() {
        viewModel.tasks.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun setupListeners() {

        binding.btnAdd.setOnClickListener {
            showTaskDialog(null)
        }

        binding.etSearch.addTextChangedListener {
            viewModel.search(it.toString()).observe(this) { list ->
                adapter.submitList(list)
            }
        }
    }

    /**
     * Add/Edit Task Dialog
     */
    private fun showTaskDialog(task: TaskEntity?) {

        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_add_edit_task, null)

        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDesc)

        val isEdit = task != null

        if (isEdit) {
            etTitle.setText(task!!.title)
            etDesc.setText(task.description)
        }

        AlertDialog.Builder(this)
            .setTitle(if (isEdit) "Edit Task" else "Add Task")
            .setView(dialogView)
            .setPositiveButton(if (isEdit) "Update" else "Add", null)
            .setNegativeButton("Cancel", null)
            .create()
            .also { dialog ->
                dialog.setOnShowListener {

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                        val title = etTitle.text.toString().trim()
                        val desc = etDesc.text.toString().trim()

                        if (title.isEmpty()) {
                            etTitle.error = "Title required"
                            return@setOnClickListener
                        }

                        if (desc.isEmpty()) {
                            etDesc.error = "Description required"
                            return@setOnClickListener
                        }

                        if (isEdit) {
                            viewModel.update(
                                task!!.copy(title = title, description = desc)
                            )
                        } else {
                            viewModel.insert(title, desc)
                        }

                        dialog.dismiss()
                    }
                }
                dialog.show()
            }
    }
}
