package net.zedge.ringtonecreator.list;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.zedge.ringtonecreator.R;

/**
 * @author Stein Eldar Johnsen <steineldar@zedge.net>
 * @since 15.12.15
 */
public class RecordingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
    private static final int SET_RINGTONE = 0;
    private static final int SHARE = 2;
    private static final int DELETE = 3;

    private final RecordingActionListener listener;
    private final TextView length;

    public interface RecordingActionListener {
        void onPlayRecording(RecordingViewHolder holder);
        void onDeleteRecording(RecordingViewHolder holder);
        void onSetRingtone(RecordingViewHolder holder);
    }

    public Recording recording;

    public final TextView title;
    public final ImageView play;
    public final FrameLayout context;

    public RecordingViewHolder(View itemView, RecordingActionListener listener) {
        super(itemView);

        this.listener = listener;

        title = (TextView) itemView.findViewById(R.id.title);
        length = (TextView) itemView.findViewById(R.id.length);

        play = (ImageView) itemView.findViewById(R.id.play_button);
        context = (FrameLayout) itemView.findViewById(R.id.context);

        play.setOnClickListener(this);
        context.setOnClickListener(this);

        itemView.setOnCreateContextMenuListener(this);
    }

    public void bind(Recording recording) {
        this.recording = recording;

        title.setText(recording.name);
        length.setText(recording.getLength());
    }

    public void recycle() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                listener.onPlayRecording(this);
                break;
            case R.id.context:
                itemView.showContextMenu();
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(R.string.recording_action);
        menu.add(Menu.NONE, SET_RINGTONE, Menu.NONE, R.string.set)
            .setOnMenuItemClickListener(this);
        menu.add(Menu.NONE, DELETE, Menu.NONE, R.string.delete)
            .setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case SET_RINGTONE:
                listener.onSetRingtone(this);
                return true;
            case DELETE:
                listener.onDeleteRecording(this);
                return true;
        }
        return false;
    }
}
