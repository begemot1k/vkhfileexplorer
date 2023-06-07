package com.example.vkhfileexplorer

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkhfileexplorer.Utils.Companion.isDirectory
import com.example.vkhfileexplorer.Utils.Companion.isImage
import com.example.vkhfileexplorer.Utils.Companion.listFiles
import com.example.vkhfileexplorer.Utils.Companion.parent
import com.example.vkhfileexplorer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), FileItemAdapter.ClickListener {
    private lateinit var binding: ActivityMainBinding
    private val adapter = FileItemAdapter(this)
    private lateinit var layout: View
    private var currentPath: String = ""

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
        navigateTo(Environment.getExternalStorageDirectory().absolutePath)
    }

    private fun navigateTo(path: String) {
        if (isDirectory(path)) {
            adapter.resetFiles()
            val dir = listFiles(path)
            for (file in dir) adapter.addFile(FileItem(file))
            adapter.update()

            supportActionBar?.apply {
                subtitle = path.substringAfter(Environment.getExternalStorageDirectory().absolutePath)
                setHomeButtonEnabled(path != Environment.getExternalStorageDirectory().absolutePath)
                setDisplayHomeAsUpEnabled(path != Environment.getExternalStorageDirectory().absolutePath)
            }
            currentPath = path
        }
    }

    override fun onClick(fileItem: FileItem) {
        if (isDirectory(fileItem.fileName)) {
            navigateTo(fileItem.fileName)
        } else {
            if (isImage(fileItem.fileName)) {
                startActivity(Intent(this, ContentActivity::class.java).apply {
                    putExtra("fileItem", fileItem.fileName)
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == resources.getIdentifier("home", "id", "android")) {
            navigateTo(parent(currentPath))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (currentPath != Environment.getExternalStorageDirectory().absolutePath) {
            navigateTo(parent(currentPath))
            return
        }
        super.onBackPressed()
    }

}