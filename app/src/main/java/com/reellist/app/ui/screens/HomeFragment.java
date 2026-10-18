package com.reellist.app.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;
import com.reellist.app.R;
import com.reellist.app.data.Movie;
import com.reellist.app.logic.WatchStats;
import com.reellist.app.logic.WatchlistLogic;
import com.reellist.app.ui.MovieViews;
import com.reellist.app.ui.ReelListViewModel;

import java.util.ArrayList;
import java.util.List;

/** Home / Dashboard: stats, "Pick for Tonight", recently added and recently watched. */
public class HomeFragment extends BaseFragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ReelListViewModel vm = vm();

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_settings) {
                nav().navigate(R.id.settingsFragment);
                return true;
            }
            return false;
        });
        view.findViewById(R.id.fab_add).setOnClickListener(v -> openAddMovie());
        view.findViewById(R.id.btn_shuffle).setOnClickListener(v -> vm.shufflePick());

        vm.getMovies().observe(getViewLifecycleOwner(), movies -> bindLists(view, movies == null ? new ArrayList<>() : movies));
        vm.getPick().observe(getViewLifecycleOwner(), pick -> bindPick(view, pick));
    }

    private void bindLists(View view, List<Movie> movies) {
        WatchStats stats = WatchlistLogic.stats(movies);
        bindStat(view.findViewById(R.id.stat_to_watch), stats.toWatch, R.string.to_watch);
        bindStat(view.findViewById(R.id.stat_watched), stats.watched, R.string.watched);
        bindStat(view.findViewById(R.id.stat_hours), stats.hoursWatched, R.string.hours);

        fillRows(view, R.id.added_header, R.id.added_list, WatchlistLogic.recentlyAdded(movies, 5), false);
        fillRows(view, R.id.seen_header, R.id.seen_list, WatchlistLogic.recentlyWatched(movies, 3), true);
        view.findViewById(R.id.home_empty).setVisibility(movies.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void bindStat(View card, int value, int label) {
        ((TextView) card.findViewById(R.id.value)).setText(String.valueOf(value));
        ((TextView) card.findViewById(R.id.label)).setText(label);
    }

    /** Home shows at most 5 + 3 rows, so simple inflated rows are enough (no RecyclerView needed). */
    private void fillRows(View view, int headerId, int containerId, List<Movie> list, boolean showRating) {
        LinearLayout container = view.findViewById(containerId);
        container.removeAllViews();
        view.findViewById(headerId).setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (Movie m : list) {
            View row = inflater.inflate(R.layout.item_movie, container, false);
            MovieViews.bindMovieRow(row, m, showRating, false, v -> openDetails(m.id), (movie, w) -> vm().setWatched(movie, w));
            container.addView(row);
        }
    }

    private void bindPick(View view, @Nullable Movie pick) {
        TextView poster = view.findViewById(R.id.pick_poster);
        TextView title = view.findViewById(R.id.pick_title);
        TextView detail = view.findViewById(R.id.pick_detail);
        View buttons = view.findViewById(R.id.pick_buttons);
        if (pick == null) {
            poster.setVisibility(View.GONE);
            buttons.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            detail.setText(R.string.pick_empty);
            return;
        }
        poster.setVisibility(View.VISIBLE);
        buttons.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        MovieViews.bindPoster(poster, pick.title);
        title.setText(pick.title);
        List<String> parts = new ArrayList<>();
        String line = WatchlistLogic.detailLine(pick);
        if (!line.isEmpty()) parts.add(line);
        if (!pick.whereToWatch.isEmpty()) parts.add(pick.whereToWatch);
        detail.setText(String.join(" \u2022 ", parts));
        view.findViewById(R.id.btn_pick_details).setOnClickListener(v -> openDetails(pick.id));
    }
}
