package net.zedge.ringtonecreator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class RingtoneCreatorFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ringtone_creator, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.recordings);
        textView.setText(getString(R.string.section_format,
                                   getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
