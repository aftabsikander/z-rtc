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
    private final RecordingActionListener listener;

    public interface RecordingActionListener {
        void onPlayRecording(Recording recording);
        void onDeleteRecording(Recording recording);
    }

    private Recording recording;

    public final TextView title;
    public final Button play;
    public final Button delete;

    public RecordingViewHolder(View itemView, RecordingActionListener listener) {
        super(itemView);

        this.listener = listener;

        title = (TextView) itemView.findViewById(R.id.title);
        play = (Button) itemView.findViewById(R.id.play_button);
        delete = (Button) itemView.findViewById(R.id.delete_button);

        play.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    public void bind(Recording recording) {
        this.recording = recording;

        title.setText(recording.name);
    }

    public void recycle() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                listener.onPlayRecording(recording);
                break;
            case R.id.delete_button:
                listener.onDeleteRecording(recording);
                break;
        }
    }
}
