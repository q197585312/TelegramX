package tw.nekogram.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.ApplicationLoader
import org.telegram.messenger.FileLog
import tw.nekogram.NekoConfig
import java.io.File
import java.util.*

object EnvUtil {

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    val rootDirectories: List<File> by lazy {

        try {
            val mStorageManager = ApplicationLoader.applicationContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            (mStorageManager.javaClass.getMethod("getVolumePaths").invoke(mStorageManager) as Array<String>).map { File(it) }
        } catch (e:  Throwable) {
            AndroidUtilities.getRootDirs()
        }

    }

    @JvmStatic
    val availableDirectories
        get() = LinkedList<File>().apply {

            add(File(ApplicationLoader.getDataDirFixed(), "files/media"))
            add(File(ApplicationLoader.getDataDirFixed(), "cache/media"))

            rootDirectories.forEach {

                add(File(it, "Android/data/" + ApplicationLoader.applicationContext.packageName + "/files"))
                add(File(it, "Android/data/" + ApplicationLoader.applicationContext.packageName + "/cache"))

            }

            add(Environment.getExternalStoragePublicDirectory("NekoX"))

        }.map { it.path }.toTypedArray()

    @JvmStatic
    fun getTelegramPath(): File {

        if (NekoConfig.cachePath == null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // https://github.com/NekoX-Dev/NekoX/issues/284
                NekoConfig.setCachePath(File(ApplicationLoader.getDataDirFixed(), "cache/media").path)
            } else {
                NekoConfig.setCachePath(ApplicationLoader.applicationContext.getExternalFilesDir("files")?.parent ?: File(ApplicationLoader.getDataDirFixed(), "cache/media").path)
            }

        }

        var telegramPath = File(NekoConfig.cachePath)

        if (telegramPath.isDirectory || telegramPath.mkdirs()) {

            return telegramPath

        }

        telegramPath = ApplicationLoader.applicationContext.getExternalFilesDir(null) ?: File(ApplicationLoader.getDataDirFixed(), "cache/files")

        if (telegramPath.isDirectory || telegramPath.mkdirs()) {

            return telegramPath

        }

        telegramPath = File(ApplicationLoader.getDataDirFixed(), "cache/files")

        if (!telegramPath.isDirectory) telegramPath.mkdirs();

        return telegramPath;

    }

    @JvmStatic
    fun doTest() {

        FileLog.d("rootDirectories: ${rootDirectories.size}")

        rootDirectories.forEach { FileLog.d(it.path) }

    }

}