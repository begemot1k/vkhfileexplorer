package online.begemot1k.vkhfileexplorer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import online.begemot1k.vkhfileexplorer.Utils.Companion.externalRoot
import online.begemot1k.vkhfileexplorer.Utils.Companion.isDirectory
import online.begemot1k.vkhfileexplorer.Utils.Companion.isImage
import online.begemot1k.vkhfileexplorer.Utils.Companion.listFiles
import online.begemot1k.vkhfileexplorer.Utils.Companion.parent
import online.begemot1k.vkhfileexplorer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), FileItemAdapter.ClickListener {
    private val preferenceTable = "TABLE"
    private val preferenceName = "path"
    private lateinit var binding: ActivityMainBinding
    private val adapter = FileItemAdapter(this)
    private lateinit var layout: View
    private var currentPath: String = ""
    private lateinit var pLauncher: ActivityResultLauncher<Intent>
    private lateinit var preferences: SharedPreferences

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
        preferences = getSharedPreferences(preferenceTable, Context.MODE_PRIVATE)
        currentPath = preferences.getString(preferenceName, externalRoot()).toString()
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentPath != externalRoot()) {
                    navigateTo(parent(currentPath))
                    return
                }
                finish()
            }
        })

        navigateTo(currentPath)
    }

    private fun navigateTo(path: String) {
        if (isDirectory(path)) {
            adapter.resetFiles()
            val dir = listFiles(path)
            for (file in dir) adapter.addFile(FileItem(file))
            adapter.update()

            supportActionBar?.apply {
                subtitle = path.substringAfter(externalRoot())
                setHomeButtonEnabled(path != externalRoot())
                setDisplayHomeAsUpEnabled(path != externalRoot())
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

    override fun onPause() {
        super.onPause()
        val editor = preferences.edit()
        editor?.putString(preferenceName, currentPath)
        editor?.apply()
    }
}