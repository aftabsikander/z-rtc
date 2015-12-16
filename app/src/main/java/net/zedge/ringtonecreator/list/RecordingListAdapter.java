package net.zedge.ringtonecreator.list;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.zedge.ringtonecreator.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Stein Eldar Johnsen <steineldar@zedge.net>
 * @since 15.12.15
 */
public class RecordingListAdapter extends RecyclerView.Adapter<RecordingViewHolder> implements RecordingViewHolder.RecordingActionListener, MediaPlayer.OnCompletionListener {
    private final SetRingtoneListener listener;
    private ArrayList<Recording> recordings;
    private MediaPlayer player = null;
    private ImageView mPlayButton = null;
    private boolean mIsPlaying = false;

    public interface SetRingtoneListener {
        void setRingtone(Recording recording);
    }

    public RecordingListAdapter(SetRingtoneListener listener) {
        this.listener = listener;
        this.recordings = new ArrayList<>();
    }

    public void reload() {
        ArrayList<Recording> found = new ArrayList<>();

        HashMap<String, Recording> old = new HashMap<>();
        for (Recording r : recordings) {
            old.put(r.path, r);
        }

        File dir = Recording.getBaseDownloadDir();

        boolean hasNew = false;
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new IllegalStateException(dir.getAbsolutePath() + " is not a cirectory!!!");
            }
            File[] content = dir.listFiles();

            for (File file : content) {
                String path = file.getAbsolutePath();
                if (old.containsKey(path)) {
                    found.add(old.get(path));
                    old.remove(path);
                } else {
                    found.add(new Recording(file));
                    hasNew = true;
                }
            }
        }

        Collections.sort(found, new Comparator<Recording>() {
            @Override
            public int compare(Recording lhs, Recording rhs) {
                return -Long.compare(lhs.timestamp, rhs.timestamp);
            }
        });

        if (old.size() > 0 || hasNew) {
            recordings.clear();
            recordings.addAll(found);

            notifyDataSetChanged();
        }
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


    private MediaPlayer getPlayer() {
        if (player == null) {
            player = new MediaPlayer();
            player.setOnCompletionListener(this);
        }
        return player;
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onPlayRecording(RecordingViewHolder holder) {
        if (mPlayButton != null && mPlayButton != holder.play) {
            mPlayButton.setImageResource(R.drawable.list_play);
            releasePlayer();
            mIsPlaying = false;
        }

        mPlayButton = holder.play;

        if (mIsPlaying) {
            mPlayButton.setImageResource(R.drawable.list_play);
            releasePlayer();
        } else {
            mPlayButton.setImageResource(R.drawable.stop);

            if (getPlayer().isPlaying()) {
                return;
            }
            playFileInMediaPlayer(getPlayer(), holder.recording.getFile());
        }
        mIsPlaying = !mIsPlaying;
    }

    @Override
    public void onSetRingtone(RecordingViewHolder recording) {
        listener.setRingtone(recording.recording);
    }

    @Override
    public void onDeleteRecording(RecordingViewHolder holder) {
        Recording recording = holder.recording;
        int pos = recordings.indexOf(recording);
        recording.getFile().delete();
        recordings.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mIsPlaying = false;
        if (mPlayButton != null) {
            mPlayButton.setImageResource(R.drawable.play);
            mPlayButton = null;
        }
        releasePlayer();
    }
}
