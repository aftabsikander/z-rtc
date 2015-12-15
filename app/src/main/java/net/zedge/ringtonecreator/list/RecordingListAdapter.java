package net.zedge.ringtonecreator.list;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.zedge.ringtonecreator.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Stein Eldar Johnsen <steineldar@zedge.net>
 * @since 15.12.15
 */
public class RecordingListAdapter extends RecyclerView.Adapter<RecordingViewHolder> implements RecordingViewHolder.RecordingActionListener {
    private ArrayList<Recording> recordings;
    private MediaPlayer player;

    public RecordingListAdapter() {
        recordings = new ArrayList<>();
    }

    public void reload() {
        int oldSize = recordings.size();
        recordings.clear();
        File dir = Recording.getBaseDownloadDir();

        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new IllegalStateException(dir.getAbsolutePath() + " is not a cirectory!!!");
            }
            File[] content = dir.listFiles();

            for (File file : content) {
                recordings.add(new Recording(file));
            }
        }

        notifyItemRangeRemoved(0, oldSize);
        notifyItemRangeInserted(0, recordings.size());
    }

    @Override
    public RecordingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recording_item, null);
        RecordingViewHolder holder = new RecordingViewHolder(view, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecordingViewHolder holder, int position) {
        holder.bind(recordings.get(position));
    }

    @Override
    public void onViewRecycled(RecordingViewHolder holder) {
        holder.recycle();
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    /**
     * Prepare the MediaPlayer for playing a ringtone represented by <code>File file</code>
     * @param mp The MediaPlayer instance which will play the ringtone
     * @param file The audio file to play
     * @return Whether or not the file was set successfully as the MediaPlayer data source
     */
    public boolean playFileInMediaPlayer(MediaPlayer mp, File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return playFileInputStreamInMediaPlayer(mp, fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Prepare the MediaPlayer for playing a ringtone from a FileInputStream
     *
     * @param mp The MediaPlayer instance which will play the ringtone
     * @param fileInputStream The FileInputStream for the audio
     * @return Whether or not the FileInputStream was set successfully as the MediaPlayer data source
     */
    public boolean playFileInputStreamInMediaPlayer(MediaPlayer mp, FileInputStream fileInputStream) {
        try {
            mp.setDataSource(fileInputStream.getFD());
            mp.prepareAsync();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            player.reset();
        }
        return false;
    }

    @Override
    public void onPlayRecording(Recording recording) {
        if (player == null) {
            player = new MediaPlayer();
        }
        if (player.isPlaying()) {
            return;
        }
        playFileInMediaPlayer(player, recording.getFile());
    }

    @Override
    public void onDeleteRecording(Recording recording) {
        int pos = recordings.indexOf(recording);
        recording.getFile().delete();
        recordings.remove(pos);
        notifyItemRemoved(pos);
    }
}
