package net.zedge.ringtonecreator.list;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    Button mPlayButton;
    boolean mIsPlaying = false;

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
        mPlayButton = (Button) view.findViewById(R.id.play_button);
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
     *
     * @param mp   The MediaPlayer instance which will play the ringtone
     * @param file The audio file to play
     * @return Whether or not the file was set successfully as the MediaPlayer data source
     */
    public boolean playFileInMediaPlayer(MediaPlayer mp, File file) {
        try {
            mp.setDataSource(file.getAbsolutePath());
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onPlayRecording(Recording recording) {
        if (mIsPlaying) {
            mPlayButton.setText("Play");
            if (player != null) {
                player.release();
                player = null;
            }
        } else {
            mPlayButton.setText("Stop");

            if (player == null) {
                player = new MediaPlayer();
            }
            if (player.isPlaying()) {
                return;
            }
            playFileInMediaPlayer(player, recording.getFile());
        }
        mIsPlaying = !mIsPlaying;

    }

    @Override
    public void onDeleteRecording(Recording recording) {
        int pos = recordings.indexOf(recording);
        recording.getFile().delete();
        recordings.remove(pos);
        notifyItemRemoved(pos);
    }
}
