package net.zedge.ringtonecreator.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.zedge.ringtonecreator.R;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Stein Eldar Johnsen <steineldar@zedge.net>
 * @since 15.12.15
 */
public class RecordingListAdapter extends RecyclerView.Adapter<RecordingViewHolder> {
    private ArrayList<Recording> recordings;

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

        if (recordings.size() < oldSize) {
            notifyItemRangeRemoved(recordings.size(), oldSize);
        }
        notifyItemRangeChanged(0, recordings.size());
    }

    @Override
    public RecordingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recording_item, null);
        return new RecordingViewHolder(view);
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
}
