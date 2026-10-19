package com.reellist.app.ui.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.reellist.app.R;
import com.reellist.app.data.Movie;
import com.reellist.app.logic.SortOption;
import com.reellist.app.logic.WatchlistLogic;
import com.reellist.app.ui.MovieAdapter;
import com.reellist.app.ui.ReelListViewModel;

import java.util.ArrayList;
import java.util.List;

/** Watchlist: unwatched titles, service filter chips, sort menu, checkbox to mark watched. */
public class WatchlistFragment extends BaseFragment {

    private List<String> shownChips = new ArrayList<>();

    public WatchlistFragment() {
        super(R.layout.fragment_watchlist);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ReelListViewModel vm = vm();
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_sort) {
                showSortMenu(toolbar.findViewById(R.id.action_sort));
                return true;
            }
            return false;
        });
        view.findViewById(R.id.fab_add).setOnClickListener(v -> openAddMovie());

        // Landscape: poster grid like the landscape wireframe. Portrait: list rows with checkboxes.
        RecyclerView list = view.findViewById(R.id.list);
        boolean grid = landscape();
        MovieAdapter adapter = new MovieAdapter(grid ? MovieAdapter.Style.GRID : MovieAdapter.Style.ROWS,
                false, false, m -> openDetails(m.id), (m, w) -> vm.setWatched(m, w));
        list.setLayoutManager(grid ? new GridLayoutManager(requireContext(), 5) : new LinearLayoutManager(requireContext()));
        list.setAdapter(adapter);

        ChipGroup chips = view.findViewById(R.id.service_chips);
        chips.setOnCheckedStateChangeListener((group, ids) -> {
            if (ids.isEmpty()) return;
            Chip c = group.findViewById(ids.get(0));
            if (c != null) vm.setService(c.getText().toString());
        });

        vm.getMovies().observe(getViewLifecycleOwner(), all -> bindChips(chips, all, vm.getService().getValue()));
        vm.getService().observe(getViewLifecycleOwner(), s -> bindChips(chips, vm.getMovies().getValue(), s));
        vm.getWatchlist().observe(getViewLifecycleOwner(), movies -> {
            adapter.submitList(movies);
            toolbar.setTitle(getString(R.string.watchlist_title, movies.size()));
            View empty = view.findViewById(R.id.empty);
            empty.setVisibility(movies.isEmpty() ? View.VISIBLE : View.GONE);
            ((TextView) view.findViewById(R.id.empty_title)).setText(R.string.nothing_here);
            String service = vm.getService().getValue();
            ((TextView) view.findViewById(R.id.empty_body)).setText(WatchlistLogic.ALL_SERVICES.equals(service)
                    ? getString(R.string.watchlist_empty_all)
                    : getString(R.string.watchlist_empty_service, service));
        });
    }

    /** "All" + the services actually used by unwatched titles. Rebuilt only when that list changes. */
    private void bindChips(ChipGroup group, @Nullable List<Movie> all, String selected) {
        List<String> services = WatchlistLogic.serviceFilters(WatchlistLogic.toWatch(all == null ? new ArrayList<>() : all));
        if (!services.equals(shownChips)) {
            shownChips = services;
            group.removeAllViews();
            for (String s : services) {
                Chip chip = new Chip(requireContext(), null, com.google.android.material.R.attr.chipStyle);
                chip.setId(View.generateViewId());
                chip.setText(s);
                chip.setCheckable(true);
                chip.setCheckedIconVisible(false);
                group.addView(chip);
            }
        }
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip chip = (Chip) group.getChildAt(i);
            if (chip.getText().toString().equals(selected) && !chip.isChecked()) chip.setChecked(true);
        }
    }

    private void showSortMenu(View anchor) {
        PopupMenu menu = new PopupMenu(requireContext(), anchor == null ? requireView().findViewById(R.id.toolbar) : anchor);
        SortOption current = vm().getSort().getValue();
        SortOption[] options = SortOption.values();
        for (int i = 0; i < options.length; i++) {
            if (options[i] == SortOption.WATCHED_STATUS) continue;
            menu.getMenu().add(0, i, i, (options[i] == current ? "\u2713 " : "") + "Sort by " + options[i].label);
        }
        menu.setOnMenuItemClickListener(item -> {
            vm().setSort(options[item.getItemId()]);
            return true;
        });
        menu.show();
    }
}
