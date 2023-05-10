package ru.pskda.vkfilemanager.ui.activity

import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.pskda.vkfilemanager.R
import ru.pskda.vkfilemanager.data.adapter.FileListAdapter
import ru.pskda.vkfilemanager.databinding.ActivityFileListBinding
import ru.pskda.vkfilemanager.utils.extensions.ContextExtensions.visible
import ru.pskda.vkfilemanager.utils.sort.FileSorting.sortFilesByDate
import ru.pskda.vkfilemanager.utils.sort.FileSorting.sortFilesByDateInv
import ru.pskda.vkfilemanager.utils.sort.FileSorting.sortFilesByExt
import ru.pskda.vkfilemanager.utils.sort.FileSorting.sortFilesByExtInv
import ru.pskda.vkfilemanager.utils.sort.FileSorting.sortFilesByName
import ru.pskda.vkfilemanager.utils.sort.FileSorting.sortFilesByNameInv
import ru.pskda.vkfilemanager.utils.sort.FileSorting.sortFilesBySize
import ru.pskda.vkfilemanager.utils.sort.FileSorting.sortFilesBySizeInv
import java.io.File

class FileListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFileListBinding
    private var invMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initStoragePath()
        initNavigation()
    }

    private fun initStoragePath() {
        val storagePath = intent.getStringExtra("storagePath")
        if (storagePath == null) {
            showErrorMsg()
        } else {
            getStorageFiles(storagePath)
        }
    }

    private fun getStorageFiles(storagePath: String) {
        val storage = File(storagePath)
        val files = storage.listFiles()?.toList()

        if (files.isNullOrEmpty()) {
            showErrorMsg()
            return
        }

        initList(files)
        initFilters(files)
    }

    private fun initNavigation() {
        with(binding) {
            backBtn.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            curPathTV.text = intent.getStringExtra("storagePath")
        }
    }

    private fun initList(files: List<File>) {
        with(binding) {
            fileListRV.layoutManager = LinearLayoutManager(this@FileListActivity)

            fileListRV.adapter = FileListAdapter(applicationContext, sortFilesByName(files))
        }
    }


    private fun resetList(files: List<File>) {
        binding.fileListRV.adapter = FileListAdapter(this, files)
    }

    private fun initFilters(files: List<File>) {
        var sortedList: List<File>
        val popup = PopupMenu(this, binding.filterBtn)

        popup.menu.add(getString(R.string.name_filter_popup_text))
        popup.menu.add(getString(R.string.date_filter_popup_text))
        popup.menu.add(getString(R.string.size_filter_popup_text))

        popup.setOnMenuItemClickListener {
            when (it.title) {
                getString(R.string.name_filter_popup_text) -> {
                    sortedList = if (!invMode) sortFilesByName(files)
                    else sortFilesByNameInv(files)
                    resetList(sortedList)
                }

                getString(R.string.date_filter_popup_text) -> {
                    sortedList = if (!invMode) sortFilesByDate(files)
                    else sortFilesByDateInv(files)
                    resetList(sortedList)
                }

                getString(R.string.size_filter_popup_text) -> {
                    sortedList = if (!invMode) sortFilesBySize(files)
                    else sortFilesBySizeInv(files)
                    resetList(sortedList)
                }

                getString(R.string.size_filter_popup_text) -> {
                    sortedList = if (!invMode) sortFilesByExt(files)
                    else sortFilesByExtInv(files)
                    resetList(sortedList)
                }
            }
            true
        }

        with(binding) {
            filterBtn.setOnClickListener {
                popup.show()
            }

            filterModeBtn.setOnClickListener {
                invMode = !invMode
                it.animate()
                    .rotationBy(it.rotationX + 180f)
                    .setDuration(250)
                    .start()
            }
        }
    }

    private fun showErrorMsg() {
        binding.noFileErrorTV.visible(true)
    }
}