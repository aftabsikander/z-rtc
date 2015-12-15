package net.zedge.ringtonecreator.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.zedge.ringtonecreator.R;

/**
 * @author Stein Eldar Johnsen <steineldar@zedge.net>
 * @since 15.12.15
 */
public class RecordingViewHolder extends RecyclerView.ViewHolder {
    public Recording recording;

    private final TextView title;

    public RecordingViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.title);
    }

    public void bind(Recording recording) {
        this.recording = recording;

        title.setText(recording.path);
    }
}
