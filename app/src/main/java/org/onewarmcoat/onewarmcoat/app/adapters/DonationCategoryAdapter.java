package org.onewarmcoat.onewarmcoat.app.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.collect.Collections2;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.onewarmcoat.onewarmcoat.app.R;
import org.onewarmcoat.onewarmcoat.app.models.DonationCategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DonationCategoryAdapter extends RecyclerView.Adapter<DonationCategoryAdapter.ViewHolder> {

    private final DonationCategory mDummyCategory;
    ArrayList<DonationCategory> mItems;
    private Context mContext;

    public DonationCategoryAdapter() {
        super();
        mDummyCategory = new DonationCategory();
        mItems = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            mItems.add(mDummyCategory);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_category, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int i) {
        DonationCategory donationCategory = mItems.get(i);
        if (donationCategory.equals(mDummyCategory)) {
            return;
        }

        vh.cvRoot.setClickable(donationCategory.isClickable());

        if (donationCategory.isSelected()) {
            vh.cvRoot.setSelected(true);
            vh.cvRoot.setElevation(1);
            vh.tvName.setTextColor(vh.white);
            vh.tvName.setTypeface(null, Typeface.BOLD);
//                vPalette.setBackgroundResource(R.color.colorPrimary);
//            cv.setStateListAnimator(p);
        } else {
            //TODO: this is gross, use statelists for all the things that change when selected/unselected
            vh.cvRoot.setSelected(false);
            vh.cvRoot.setElevation(vh.card_elevation);
            vh.tvName.setTextColor(vh.colorPrimary);
            vh.tvName.setTypeface(null, Typeface.NORMAL);
        }

        if (vh.tvName != null && vh.tvDescription != null) {
            if (Locale.getDefault() == Locale.GERMAN) { //TODO locale doesnt work correctly
                vh.tvName.setText(donationCategory.getNameDE());
                vh.tvDescription.setText(donationCategory.getDescriptionDE());
            } else {
                vh.tvName.setText(donationCategory.getNameEN());
                vh.tvDescription.setText(donationCategory.getDescriptionEN());
            }
        }

        Picasso.with(mContext).load(donationCategory.getImage().getUrl()).into(vh.imageView, new Callback() {
            @Override
            public void onSuccess() {
                vh.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {
                // set a default or 'error' image
                // imageView.setImageDrawable(R.id.);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(DonationCategory item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void setItems(Collection<DonationCategory> items) {
        if (items.equals(mItems)) {
            Log.i("DonationCategoryAdapter", "New DonationCategory list is the same as the current list.");
            return;
        }
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void setItemSelected(int position, boolean selected) {
        mItems.get(position).setSelected(selected);
    }

    public Collection<DonationCategory> getSelectedItems() {
        return Collections2.filter(mItems, DonationCategory::isSelected);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image)
        public ImageView imageView;

        @Bind(R.id.progressBar)
        public ProgressBar progressBar;

        @Bind(R.id.tvName)
        public TextView tvName;

        @Bind(R.id.tvDescription)
        public TextView tvDescription;

        @Bind(R.id.cvRoot)
        CardView cvRoot;

        @Bind(R.id.vPalette)
        View vPalette;

        @BindColor(R.color.white)
        int white;

        @BindColor(R.color.colorPrimary)
        int colorPrimary;
        @BindDimen(R.dimen.card_elevation)
        int card_elevation;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cvRoot)
        public void onCardClick(CardView cv) {
            int pos = getAdapterPosition();
            DonationCategory donationCategory = mItems.get(pos);

            if (donationCategory.isSelected()) {
                donationCategory.setSelected(false);
                //TODO: this is gross, use statelists for all the things that change when selected/unselected
                tvName.setTextColor(colorPrimary);
                tvName.setTypeface(null, Typeface.NORMAL);
                cv.setElevation(card_elevation);
                cv.setSelected(false);
            } else {
                donationCategory.setSelected(true);
                tvName.setTextColor(white);
                tvName.setTypeface(null, Typeface.BOLD);
                cv.setElevation(1);
                cv.setSelected(true);
//                vPalette.setBackgroundResource(R.color.colorPrimary);
//            cv.setStateListAnimator(p);
            }
        }
    }
}