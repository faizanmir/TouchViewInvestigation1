package com.example.touchviewinvestigation

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File

/**
 * Created on 06/09/20
 * @author Anggrayudi H
 */
enum class MediaType(val readUri: Uri?, val writeUri: Uri?) {
    IMAGE(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.getContentUri(MediaStoreCompat.volumeName)),
    AUDIO(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media.getContentUri(MediaStoreCompat.volumeName)),
    VIDEO(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.getContentUri(MediaStoreCompat.volumeName)),
    DOWNLOADS(
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) null else MediaStore.Downloads.EXTERNAL_CONTENT_URI,
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) null else MediaStore.Downloads.getContentUri(MediaStoreCompat.volumeName)
    );

    /**
     * Get all directories associated with this media type.
     */
    @Suppress("DEPRECATION")
    val directories: List<File>
        get() = when (this) {
            IMAGE -> ImageMediaDirectory.values().map { Environment.getExternalStoragePublicDirectory(it.folderName) }
            AUDIO -> AudioMediaDirectory.values().map { Environment.getExternalStoragePublicDirectory(it.folderName) }
            VIDEO -> VideoMediaDirectory.values().map { Environment.getExternalStoragePublicDirectory(it.folderName) }
            DOWNLOADS -> listOf(PublicDirectory.DOWNLOADS.file)
        }

    val mimeType: String
        get() = when (this) {
            IMAGE -> MimeType.IMAGE
            AUDIO -> MimeType.AUDIO
            VIDEO -> MimeType.VIDEO
            else -> MimeType.UNKNOWN
        }
}