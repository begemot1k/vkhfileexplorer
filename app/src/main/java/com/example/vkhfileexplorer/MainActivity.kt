package com.example.vkhfileexplorer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var pLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        layout = binding.mainLayout
        setContentView(binding.root)
        registerPermissionListener()
        checkPermission()
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

    private fun checkPermission() {
        if (Environment.isExternalStorageManager()) {
            Toast.makeText(this, "Разрешение на чтение файлов имеется", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val pName = applicationContext.packageName
                var intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.setData(Uri.parse("package:${pName}"))
                pLauncher.launch(intent)
            } catch (ex: Exception) {
                Log.w("PERMISSION", "exception ${ex.localizedMessage}")
                var intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                pLauncher.launch(intent)
            }
        }

    }

    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(this, "Разрешение на чтение файлов имеется", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Нет разрешения на доступ к файлам", Toast.LENGTH_SHORT).show()
            }
        }
    }

}