package ru.pskda.vkfilemanager.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.pskda.vkfilemanager.data.adapter.FileListAdapter
import ru.pskda.vkfilemanager.databinding.ActivityFileListBinding
import ru.pskda.vkfilemanager.utils.extensions.ContextExtensions.visible
import java.io.File

class FileListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFileListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initStoragePath()
        initNavigation()
    }

    private fun initStoragePath(){
        val storagePath = intent.getStringExtra("storagePath")
        if (storagePath == null) {
            showErrorMsg()
        } else {
            getStorageFiles(storagePath)
        }
    }

    private fun getStorageFiles(storagePath: String) {
        val storage = File(storagePath)
        val files = storage.listFiles()

        if (files == null || files.isEmpty()) {
            showErrorMsg()
            return
        }

        initList(files)
    }

    private fun initNavigation() {
        with(binding) {
            backBtn.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            curPathTV.text = intent.getStringExtra("storagePath")
        }
    }

    private fun initList(files: Array<File>) {
        with(binding) {
            fileListRV.layoutManager = LinearLayoutManager(this@FileListActivity)
            fileListRV.adapter = FileListAdapter(applicationContext, files)
        }
    }

    private fun showErrorMsg() {
        binding.noFileErrorTV.visible(true)
    }
}