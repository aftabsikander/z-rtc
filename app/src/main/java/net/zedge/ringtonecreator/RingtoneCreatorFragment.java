package net.zedge.ringtonecreator;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.LinearLayout;
import android.widget.Button;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;

import net.zedge.ringtonecreator.list.Recording;

import java.io.File;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class RingtoneCreatorFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG = "RingtoneCreatorFragment";

    private static String mFileName = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    Handler handler;
    private VisualizerView mVisualizerView;
    private boolean mStartRecording = true;
    private long mCounterTime;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mFileName = getNewFile().getAbsolutePath();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();

            mRecorder.getMaxAmplitude();
            mRecorder.start();
            mCounterTime = System.currentTimeMillis();
        } catch (IOException e) {
            Log.e(LOG, "prepare() failed");
        }

         handler.post(updateVisualizer);
    }

    private TextView mTextCounter;
    Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (mRecorder != null) // if we are already recording
            {
                // get the current amplitude
                int x = mRecorder.getMaxAmplitude();
                mVisualizerView.addAmplitude(x); // update the VisualizeView
                mVisualizerView.invalidate(); // refresh the VisualizerView

                mTextCounter.setText(getTimeAsText());

                // update in 40 milliseconds
                handler.postDelayed(this, 40);
            }
        }
    };

     String getTimeAsText() {
        int min = 0;
        int sec = 0;
        int msec = 0;

        if (mCounterTime !=0) {
            long diff = System.currentTimeMillis()-mCounterTime;
            msec = (int) diff % 100;
            sec = (int) ((diff / 1000) % 60);
            min = (int) ((diff / 60000) % 60);

        }

        return getDigit(min)+":"+getDigit(sec)+":"+getDigit(msec);
     }

    String getDigit(int i) {
        if (i > 99) i = i / 100;
        String ret = Integer.toString(i);
        if (ret.length() == 1) ret = "0" + ret;
        return ret;
    }

    private void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RingtoneCreatorFragment newInstance(int sectionNumber) {

        RingtoneCreatorFragment fragment = new RingtoneCreatorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public RingtoneCreatorFragment() {
        handler = new Handler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =   inflater.inflate(R.layout.recorder, container, false);

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.ll);
        mVisualizerView = (VisualizerView) rootView.findViewById(R.id.visualizer);

        mTextCounter = (TextView) rootView.findViewById(R.id.text_counter);
        final TextView textView = (TextView) rootView.findViewById(R.id.text_recording);

        ImageView saveButton = (ImageView) rootView.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCounterTime = 0;
                mVisualizerView.clear();
                mVisualizerView.invalidate(); // refresh the VisualizerView

                mTextCounter.setText(getTimeAsText());

            }
        });
        ImageView playButton = (ImageView) rootView.findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ImageView deleteButton = (ImageView) rootView.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.record_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    textView.setText("Stop recording");
                    imageView.setImageResource(R.drawable.stop_btn);

                    mCounterTime = 0;
                    mVisualizerView.clear();
                    mVisualizerView.invalidate(); // refresh the VisualizerView

                    mTextCounter.setText(getTimeAsText());

                } else {
                    textView.setText("Start recording");
                    imageView.setImageResource(R.drawable.rec_btn);


                }

                mStartRecording = !mStartRecording;

            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public File getNewFile() { return new File(Recording.getBaseDownloadDir(), "record."+findNextIndex()); }



    public int findNextIndex() {
        int ret = 1;
        try {
            File file[] = Recording.getBaseDownloadDir().listFiles();
        for (int i=0; i < file.length; i++) {
            String name = file[i].getName();
            int pos = name.indexOf(".");
            if (pos != -1 && pos < name.length()) {
                    int ii = Integer.parseInt(name.substring(pos+1));
                    if (ii>=ret) {
                        ret = ii+1;
                    }
            }
        }
        } catch (Exception e) {

        }
        return ret;
    }
}


