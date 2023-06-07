package com.example.vkhfileexplorer

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkhfileexplorer.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity(), FileItemAdapter.ClickListener {
    private lateinit var binding: ActivityMainBinding
    private val adapter = FileItemAdapter(this)
    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        layout = binding.mainLayout
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter
        supportActionBar?.apply {
            subtitle = ""
        }

        navigateTo(Environment.getExternalStorageDirectory().absolutePath)
    }

    private fun navigateTo(path: String) {
        adapter.resetFiles()
        val dir = File(path).listFiles()
        for (file in dir!!) {
            adapter.addFile(FileItem(file.absolutePath))
        }
        adapter.update()
        Toast.makeText(this, "Привет!", Toast.LENGTH_LONG).show()
    }

    override fun onClick(fileItem: FileItem) {
        if (File(fileItem.fileName).isDirectory) {
            navigateTo(fileItem.fileName)
        } else {
            startActivity(Intent(this, ContentActivity::class.java).apply {
                putExtra("fileItem", fileItem.fileName)
            })
        }
    }

}