package ru.pskda.vkfilemanager.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.text.format.Formatter.formatFileSize
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ru.pskda.vkfilemanager.BuildConfig
import ru.pskda.vkfilemanager.R
import ru.pskda.vkfilemanager.databinding.ActivityMainBinding
import ru.pskda.vkfilemanager.utils.extensions.ContextExtensions.visible
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val currAndroidVersion = android.os.Build.VERSION.SDK_INT
    private val QAndroidVersion = android.os.Build.VERSION_CODES.Q

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()
        initClickListeners()
        initStorageInfo(getExternalStorage())
    }

    private fun initStorageInfo(dirPath: File) {
        val dirStat = StatFs(dirPath.path)
        val dirBlockSize = dirStat.blockSizeLong

        val availableSpace = dirStat.availableBlocksLong * dirBlockSize
        val totalSpace = dirStat.blockCountLong * dirBlockSize

        val formattedAvailableSpace = formatFileSize(this, availableSpace)
        val formattedTotalSpace = formatFileSize(this, totalSpace)

        with(binding) {
            storageSpaceTV.text = String.format(
                getString(R.string.storage_space_text),
                formattedAvailableSpace,
                formattedTotalSpace
            )
            spacePB.progress =
                100 - ((availableSpace.toFloat() / totalSpace.toFloat()) * 100).toInt()
        }
    }

    private fun initClickListeners() {
        with(binding) {
            reqPermBtn.setOnClickListener {
                if (currAndroidVersion > QAndroidVersion)
                    requestNewPermissions()
                else
                    requestPermissions()
            }

            fileListBtn.setOnClickListener {
                val intent = Intent(this@MainActivity, FileListActivity::class.java)
                intent.putExtra("storagePath", getExternalStorage().path)
                startActivity(intent)
            }
        }
    }


    /* Вызов isNewPermissionsGranted не должен происходить на старых версиях андройда, соответственно,
    Environment.isExternalStorageManager() также не должен вызываться и приводить к крашу */
    private fun checkPermissions() {
        with(binding) {
            if (!isOldPermissionsGranted() &&
                currAndroidVersion <= QAndroidVersion
            ) {
                reqPermLayout.visible(true)
                fileListBtn.visible(false)
            } else if (currAndroidVersion > QAndroidVersion && !isNewPermissionsGranted()) {
                reqPermLayout.visible(true)
                fileListBtn.visible(false)
            }
            reqPermLayout.visible(false)
        }
    }

    private fun isOldPermissionsGranted() =
        PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)


    private fun isNewPermissionsGranted() =
        Environment.isExternalStorageManager()

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            100
        )
    }

    private fun requestNewPermissions() {
        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        startActivity(
            Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                uri
            )
        )
    }

    private fun getExternalStorage(): File = Environment.getExternalStorageDirectory()
}