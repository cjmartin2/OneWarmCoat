package org.onewarmcoat.onewarmcoat.app.fragments.main.volunteer;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import org.onewarmcoat.onewarmcoat.app.R;
import org.onewarmcoat.onewarmcoat.app.adapters.DashboardItemAdapter;
import org.onewarmcoat.onewarmcoat.app.interfaces.ViewPagerChangeListener;
import org.onewarmcoat.onewarmcoat.app.models.Donation;
import org.onewarmcoat.onewarmcoat.app.models.PickupRequest;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class DashboardFragment extends Fragment implements
        ViewPagerChangeListener, OnDismissCallback, ContextualUndoAdapter.DeleteItemCallback {

    @InjectView(R.id.emptyView)
    LinearLayout emptyView;
    @InjectView(R.id.lvItems)
    ListView lvItems;
    PullToRefreshLayout mPullToRefreshLayout;
    private DashboardItemAdapter mAdapter;

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance() {
        // strange. I can't use a constructor, I have to define this newInstance method and
        // call this in order to get a usable instance of this fragment.
        DashboardFragment f = new DashboardFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new DashboardItemAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.inject(this, v);
        setHasOptionsMenu(true);

        lvItems.setEmptyView(emptyView);

        // Convert view to ViewGroup
        ViewGroup viewGroup = (ViewGroup) v;
        // Create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());
        // Setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                .insertLayoutInto(viewGroup)
                        // We need to mark the ListView and it's Empty View as pullable
                        // This is because they are not dirent children of the ViewGroup
                .theseChildrenArePullable(lvItems, lvItems.getEmptyView())
                        // Set a OnRefreshListener
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        loadDashboardItems();
                    }
                })
                        // Commit the setup to our PullToRefreshLayout
                .setup(mPullToRefreshLayout);

        mAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener() {
            @Override
            public void onLoading() {
                mPullToRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onLoaded(List list, Exception e) {
                mPullToRefreshLayout.setRefreshComplete();
            }
        });

        // Appearance Animation
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
        swingBottomInAnimationAdapter.setAbsListView(lvItems);

        // Swipe-to-dismiss
        SwipeDismissAdapter swipeDismissAdapter = new SwipeDismissAdapter(swingBottomInAnimationAdapter, this);
        swipeDismissAdapter.setAbsListView(lvItems);

        // Undo support (used to confirm whether pickup was dropped off)
        ContextualUndoAdapter contextualUndoAdapter = new ContextualUndoAdapter(swipeDismissAdapter, R.layout.dashboard_item_contextual_undo, R.id.btnCompleteDropoff, this);
        contextualUndoAdapter.setAbsListView(lvItems);

        lvItems.setAdapter(contextualUndoAdapter);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // DONATION CREATION
                final PickupRequest pickupRequest = (PickupRequest) parent.getItemAtPosition(position);
                final Donation donation = new Donation(pickupRequest.getDonor(), pickupRequest.getDonationType(), pickupRequest.getDonationValue(), pickupRequest.getNumberOfCoats());

                donation.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            //send push to donor
                            pickupRequest.generatePickupCompleteNotif();

                            //create donation, and set it in the PickupRequest
                            pickupRequest.setDonation(donation);

                            pickupRequest.saveInBackground();
                            loadDashboardItems();
                        }
                    }
                });
                return false;
            }
        });

        return v;
    }

    private void loadDashboardItems() {
        mAdapter.loadObjects();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadDashboardItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewPagerShow() {
        loadDashboardItems();
    }

    @Override
    public void onViewPagerHide() {
    }

    @Override
    public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            //create the contextual view asking if complete
            Log.w("df", "dismiss");
        }
    }

    @Override
    public void deleteItem(int position) {
        //pickup dropped off. complete
        Log.w("df", "delete");

    }
}
