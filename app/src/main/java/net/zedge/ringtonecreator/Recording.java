package net.zedge.ringtonecreator;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * @author Stein Eldar Johnsen <steineldar@zedge.net>
 * @since 15.12.15
 */
public class Recording {
    public static final String BASE_DOWNLOAD_DIR = "z-rtc";
    public static File sBaseDownloadDir;

    public String name;
    public String path;

    public Recording(File file) throws IOException {
        path = file.getCanonicalPath();
        name = file.getName();
    }

    public static File getBaseDownloadDir() {
        if (sBaseDownloadDir == null) {
            sBaseDownloadDir =
                    new File(Environment.getExternalStorageDirectory(), BASE_DOWNLOAD_DIR);
            if (!sBaseDownloadDir.exists()) {
                sBaseDownloadDir.mkdir();
            }
        }
        return sBaseDownloadDir;
    }

    public File getFile() {
        return new File(getBaseDownloadDir(), name);
    }


}
