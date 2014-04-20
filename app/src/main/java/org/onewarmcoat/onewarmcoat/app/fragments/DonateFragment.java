package org.onewarmcoat.onewarmcoat.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseObject;

import org.onewarmcoat.onewarmcoat.app.fragments.donate.PickUpFragment;


public class DonateFragment extends PageSlidingTabStripFragment {

    private Fragment pickupFragment;
    private Fragment dropoffFragment;
    private Fragment cashFragment;

    @Override
    protected String[] getTitles() {
        return new String[]{"PickUp", "DropOff", "Donate Cash"};
    }
    
//    private OnFragmentInteractionListener mListener;

    public DonateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickupFragment = PickUpFragment.newInstance();
//        dropoffFragment = DropOffFragment.newInstance(); //TODO
//        cashFragment = CashFragment.newInstance(); //TODO
    }

    @Override
    protected Fragment getFragmentForPosition(int position) {
        Fragment frag;
        switch (position) {
            case 0: //Pickup
                frag = pickupFragment;
                break;
//            case 1: //Dropoff
//                frag = dropoffFragment;
//                break;
//            case 2: //Donate Cash
//                frag = cashFragment;
//                break;
            default:
                Log.d("DonateFragment", "default case hit in getFragmentForPosition, weird tab/position number!");
                frag = SuperAwesomeCardFragment.newInstance(position);
                break;
        }
        return frag;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View root = inflater.inflate(R.layout.fragment_donate, container, false);
//
//        return root;
//    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        ParseObject testObject = new ParseObject("WhichView");
        testObject.put("userIs", "Donating");
        testObject.saveInBackground();

//        ActionBar actionBar = getActivity().getActionBar();
//
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //this is ghetto, but just placeholder
//        ((MainActivity) activity).onSectionAttached(2);

//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

//        ActionBar actionBar = getActivity().getActionBar();
//
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        mListener = null;
    }
//
//    @Override
//    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//    }
//
//    @Override
//    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//        Log.d("donatefrag", "tabunselected");
//
//    }
//
//    @Override
//    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//    }

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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}