package net.zedge.ringtonecreator.list;

import android.media.MediaPlayer;
import android.os.Environment;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Stein Eldar Johnsen <steineldar@zedge.net>
 * @since 15.12.15
 */
public class Recording {
    public static final String BASE_DOWNLOAD_DIR = "z-rtc";
    public static File sBaseDownloadDir;

    public final String name;
    public final String path;
    public final int length;

    public Recording(File file) {
        path = file.getAbsolutePath();
        String tmp = file.getName();
        if (tmp.endsWith(".aac") || tmp.endsWith(".mp3")) {
            name = tmp.substring(0, tmp.length() - 4);
        } else {
            name = tmp;
        }

        int tmpLength = 0;
        try {
            MediaPlayer mp = new MediaPlayer();
            FileInputStream fs;
            FileDescriptor fd;
            fs = new FileInputStream(getFile());
            fd = fs.getFD();
            mp.setDataSource(fd);
            mp.prepare();
            tmpLength = mp.getDuration();
            mp.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        length = tmpLength;
    }

    public synchronized static File getBaseDownloadDir() {
        if (sBaseDownloadDir == null) {
            sBaseDownloadDir =
                    new File(Environment.getExternalStorageDirectory(), BASE_DOWNLOAD_DIR);
        }
        return sBaseDownloadDir;
    }

    public File getFile() {
        return new File(path);
    }

    public int hashCode() {
        return path.hashCode();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Recording)) {
            return false;
        }
        Recording other = (Recording) o;
        return path.equals(other.path);
    }

    public String getLength() {
        if (length > 0) {
            float l = (float) length / 1000;
            return String.format("%.2fs", l);
        } else {
            return "0.00s";
        }
    }
}
