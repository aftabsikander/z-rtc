package net.zedge.ringtonecreator.list;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.zedge.ringtonecreator.R;

import java.io.File;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordingListFragment extends Fragment implements RecordingListAdapter.SetRingtoneListener {
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
            adapter = new RecordingListAdapter(this);
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

    @Override
    public void setRingtone(Recording recording) {
        RingtoneManager.setActualDefaultRingtoneUri(getActivity(), RingtoneManager.TYPE_RINGTONE,
                                                    getSoundFileURI(recording));
    }

    /**
     * Find URI from content provider based on file path. If it is not
     * found, a new scan is triggered.
     *
     * @param recording The absolute path of the file
     * @return Uri from media content provider or null if not found
     */
    public Uri getSoundFileURI(Recording recording) {
        Uri media = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {BaseColumns._ID, MediaStore.MediaColumns.DATA};
        String selection = MediaStore.MediaColumns.DATA + " == '" + recording.path + "'";
        Cursor cursor = getContext().getContentResolver()
                                    .query(media, projection, selection, null, null);
        int id;
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(0);
            cursor.close();
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return insertSoundFile(recording);
        }
        if (id > 0) {
            return ContentUris.withAppendedId(media, id);
        }
        return null;
    }

    /**
     * Insert a single mp3-file into the MediaStore content provider
     *
     * @param item The item to insert
     * @return The content resolver URI of the inserted file
     */
    public Uri insertSoundFile(Recording item) {
        File file = item.getFile();

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, item.name);
        values.put(MediaStore.MediaColumns.SIZE, file.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
        values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE, true);
        values.put(MediaStore.Audio.AudioColumns.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.AudioColumns.IS_MUSIC, false);

        // Insert it into the database

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        return getContext().getContentResolver().insert(uri, values);
    }

}
