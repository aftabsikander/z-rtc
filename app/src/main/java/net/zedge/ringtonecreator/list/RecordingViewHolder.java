package net.zedge.ringtonecreator.list;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.zedge.ringtonecreator.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Stein Eldar Johnsen <steineldar@zedge.net>
 * @since 15.12.15
 */
public class RecordingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Recording recording;
    private MediaPlayer player;

    private final TextView title;
    private final Button play;

    public RecordingViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.title);
        play = (Button) itemView.findViewById(R.id.play_button);
        play.setOnClickListener(this);
    }

    public void bind(Recording recording) {
        this.recording = recording;

        title.setText(recording.name);
    }

    public void recycle() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (player == null) {
            player = new MediaPlayer();
        }
        if (player.isPlaying()) {
            return;
        }
        playFileInMediaPlayer(player, recording.getFile());
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
}
