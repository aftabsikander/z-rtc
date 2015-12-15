package net.zedge.ringtonecreator.list;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.zedge.ringtonecreator.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordingListFragment extends Fragment {
    private RecordingListAdapter adapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private static RecordingListFragment sInstance;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RecordingListFragment getInstance() {
        if (sInstance == null) {
            sInstance = new RecordingListFragment();
        }
        return sInstance;
    }

    public RecordingListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recording_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.item_list);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (adapter == null) {
            adapter = new RecordingListAdapter();
            adapter.reload();
        }

        mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    public void reload() {
        if (adapter != null) {
            adapter.reload();
        }
    }
}
