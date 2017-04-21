package pam.astroweather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MoonFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private TextView riseTime, setTime, newMoonDate, fullMoonDate, moonIllumination, moonAge;

    public MoonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_moon, container, false);
        riseTime = (TextView)v.findViewById(R.id.rise_time);
        setTime = (TextView)v.findViewById(R.id.set_time);
        newMoonDate = (TextView)v.findViewById(R.id.new_moon_date);
        fullMoonDate = (TextView)v.findViewById(R.id.full_moon_date);
        moonIllumination = (TextView)v.findViewById(R.id.moon_illumination_value);
        moonAge = (TextView)v.findViewById(R.id.moon_age_value);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
