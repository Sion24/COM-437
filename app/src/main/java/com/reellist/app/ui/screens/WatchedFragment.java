package com.reellist.app.ui.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.reellist.app.R;
import com.reellist.app.data.Movie;
import com.reellist.app.logic.WatchlistLogic;
import com.reellist.app.ui.MovieAdapter;
import com.reellist.app.ui.MovieViews;
import com.reellist.app.ui.ReelListViewModel;

import java.util.List;

/**
 * Watched history ("Seen"). Portrait: list with stars and watched date.
 * Landscape: poster grid on the left + rating panel on the right, as in the landscape wireframe.
 */
public class WatchedFragment extends BaseFragment {

    public WatchedFragment() {
        super(R.layout.fragment_watched);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ReelListViewModel vm = vm();
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        boolean grid = landscape();

        RecyclerView list = view.findViewById(R.id.list);
        MovieAdapter adapter = new MovieAdapter(
                grid ? MovieAdapter.Style.GRID : MovieAdapter.Style.ROWS, true, true,
                m -> {
                    if (grid) vm.selectWatched(m.id);    // landscape: show it in the rating panel
                    else openDetails(m.id);
                },
                (m, w) -> vm.setWatched(m, w));
        list.setLayoutManager(grid ? new GridLayoutManager(requireContext(), 4) : new LinearLayoutManager(requireContext()));
        list.setAdapter(adapter);

        ((TextView) view.findViewById(R.id.empty_title)).setText(R.string.watched_empty_title);
        ((TextView) view.findViewById(R.id.empty_body)).setText(R.string.watched_empty_body);

        vm.getWatched().observe(getViewLifecycleOwner(), watched -> {
            adapter.submitList(watched);
            toolbar.setTitle(getString(R.string.watched_title, watched.size()));
            view.findViewById(R.id.empty).setVisibility(watched.isEmpty() ? View.VISIBLE : View.GONE);
            View content = view.findViewById(R.id.content);
            if (content != null) content.setVisibility(watched.isEmpty() ? View.GONE : View.VISIBLE);
            if (grid) bindPanel(view, adapter, watched, vm.getSelectedWatchedId().getValue());
        });
        if (grid) {
            vm.getSelectedWatchedId().observe(getViewLifecycleOwner(),
                    id -> bindPanel(view, adapter, vm.getWatched().getValue(), id));
        }
    }

    /** Landscape rating panel for the selected poster (first one if nothing is selected yet). */
    private void bindPanel(View view, MovieAdapter adapter, @Nullable List<Movie> watched, @Nullable Long selectedId) {
        if (watched == null || watched.isEmpty()) return;
        Movie selected = watched.get(0);
        for (Movie m : watched) if (selectedId != null && m.id == selectedId) selected = m;
        final Movie movie = selected;
        adapter.setSelectedId(movie.id);

        MovieViews.bindPoster(view.findViewById(R.id.panel_poster), movie.title);
        ((TextView) view.findViewById(R.id.panel_title)).setText(movie.title);
        ((TextView) view.findViewById(R.id.panel_detail)).setText(WatchlistLogic.detailLine(movie));
        ((TextView) view.findViewById(R.id.panel_date)).setText(getString(R.string.watched_on, MovieViews.formatDate(movie.dateWatched)));
        MovieViews.bindStars(view.findViewById(R.id.panel_stars), movie.rating, 28, stars -> vm().rate(movie, stars));
        TextView notes = view.findViewById(R.id.panel_notes);
        notes.setText(movie.notes);
        notes.setVisibility(movie.notes.isEmpty() ? View.GONE : View.VISIBLE);
        view.findViewById(R.id.panel_open).setOnClickListener(v -> openDetails(movie.id));
    }
}
