package com.reellist.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.reellist.app.R;
import com.reellist.app.data.Movie;

/**
 * RecyclerView adapter with two looks:
 * ROWS  = portrait list rows with a watched checkbox (item_movie.xml)
 * GRID  = landscape poster grid like the landscape wireframe (item_poster.xml)
 */
public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.Holder> {

    public enum Style { ROWS, GRID }

    public interface OnMovieClick { void onClick(Movie movie); }

    private final Style style;
    private final boolean showRating;
    private final boolean showWatchedDate;
    private final OnMovieClick onClick;
    private final MovieViews.OnWatchedChange onWatchedChange;
    private long selectedId = -1;

    public MovieAdapter(Style style, boolean showRating, boolean showWatchedDate,
                        OnMovieClick onClick, MovieViews.OnWatchedChange onWatchedChange) {
        super(DIFF);
        this.style = style;
        this.showRating = showRating;
        this.showWatchedDate = showWatchedDate;
        this.onClick = onClick;
        this.onWatchedChange = onWatchedChange;
        setHasStableIds(true);
    }

    /** Highlights one poster in GRID style (used by the landscape Seen screen). */
    public void setSelectedId(long id) {
        if (id != selectedId) {
            selectedId = id;
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) { return getItem(position).id; }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = style == Style.ROWS ? R.layout.item_movie : R.layout.item_poster;
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        Movie m = getItem(position);
        if (style == Style.ROWS) {
            MovieViews.bindMovieRow(h.itemView, m, showRating, showWatchedDate, v -> onClick.onClick(m), onWatchedChange);
            return;
        }
        MovieViews.bindPoster(h.itemView.findViewById(R.id.poster), m.title);
        ((TextView) h.itemView.findViewById(R.id.title)).setText(m.title);
        LinearLayout stars = h.itemView.findViewById(R.id.stars);
        stars.setVisibility(m.watched ? View.VISIBLE : View.GONE);
        if (m.watched) MovieViews.bindStars(stars, m.rating, 12, null);
        MaterialCardView card = (MaterialCardView) h.itemView;
        card.setChecked(m.id == selectedId);
        card.setStrokeWidth(m.id == selectedId ? (int) MovieViews.dp(card.getContext(), 2) : 0);
        card.setOnClickListener(v -> onClick.onClick(m));
    }

    static class Holder extends RecyclerView.ViewHolder {
        Holder(View v) { super(v); }
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie a, @NonNull Movie b) { return a.id == b.id; }

        @Override
        public boolean areContentsTheSame(@NonNull Movie a, @NonNull Movie b) { return a.sameContentAs(b); }
    };
}
