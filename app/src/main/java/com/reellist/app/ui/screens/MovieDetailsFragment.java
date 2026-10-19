package com.reellist.app.ui.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.reellist.app.MainActivity;
import com.reellist.app.R;
import com.reellist.app.data.Movie;
import com.reellist.app.logic.WatchlistLogic;
import com.reellist.app.ui.MovieViews;
import com.reellist.app.ui.ReelListViewModel;

/**
 * Movie Details = the editing hub. Mark as Watched, Rate, Edit and Delete all live here,
 * so the user learns one place to manage a title.
 */
public class MovieDetailsFragment extends BaseFragment {

    private Movie current;

    public MovieDetailsFragment() {
        super(R.layout.fragment_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ReelListViewModel vm = vm();
        long movieId = requireArguments().getLong("movieId");

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> nav().popBackStack());
        toolbar.setOnMenuItemClickListener(item -> {
            if (current == null) return false;
            if (item.getItemId() == R.id.action_edit) {
                Bundle args = new Bundle();
                args.putLong("movieId", current.id);
                nav().navigate(R.id.addEditFragment, args);
                return true;
            }
            if (item.getItemId() == R.id.action_delete) {
                if (Boolean.TRUE.equals(vm.getConfirmDelete().getValue())) confirmDelete(current);
                else delete(current);
                return true;
            }
            return false;
        });

        view.findViewById(R.id.btn_watch).setOnClickListener(v -> { if (current != null) vm.setWatched(current, true); });
        view.findViewById(R.id.btn_unwatch).setOnClickListener(v -> { if (current != null) vm.setWatched(current, false); });

        vm.movie(movieId).observe(getViewLifecycleOwner(), movie -> {
            if (movie == null) return;     // deleted, or still loading
            current = movie;
            bind(view, movie);
        });
    }

    private void bind(View view, Movie m) {
        view.findViewById(R.id.content).setVisibility(View.VISIBLE);
        MovieViews.bindPoster(view.findViewById(R.id.poster), m.title);
        ((TextView) view.findViewById(R.id.title)).setText(m.title);

        StringBuilder info = new StringBuilder();
        appendInfo(info, "Year", m.releaseYear == null ? null : String.valueOf(m.releaseYear));
        appendInfo(info, "Genre", m.genre);
        appendInfo(info, "Runtime", m.runtimeMinutes == null ? null : WatchlistLogic.formatRuntime(m.runtimeMinutes));
        appendInfo(info, "Where to watch", m.whereToWatch);
        appendInfo(info, "Added", MovieViews.formatDate(m.dateAdded));
        ((TextView) view.findViewById(R.id.info)).setText(info.toString().trim());

        boolean hasNotes = !m.notes.isEmpty();
        view.findViewById(R.id.notes_label).setVisibility(hasNotes ? View.VISIBLE : View.GONE);
        TextView notes = view.findViewById(R.id.notes);
        notes.setVisibility(hasNotes ? View.VISIBLE : View.GONE);
        notes.setText(m.notes);

        view.findViewById(R.id.watched_section).setVisibility(m.watched ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.unwatched_section).setVisibility(m.watched ? View.GONE : View.VISIBLE);
        if (m.watched) {
            ((TextView) view.findViewById(R.id.watched_date)).setText(getString(R.string.watched_on, MovieViews.formatDate(m.dateWatched)));
            MovieViews.bindStars(view.findViewById(R.id.stars), m.rating, 28, stars -> vm().rate(m, stars));
        }
    }

    private static void appendInfo(StringBuilder sb, String label, @Nullable String value) {
        if (value != null && !value.trim().isEmpty()) sb.append(label).append(": ").append(value).append('\n');
    }

    private void confirmDelete(Movie m) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.remove_title, m.title))
                .setMessage(R.string.remove_body)
                .setPositiveButton(R.string.delete, (d, w) -> delete(m))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void delete(Movie m) {
        vm().delete(m);
        // The Activity shows "Deleted … Undo" so the snackbar stays visible after this screen closes
        ((MainActivity) requireActivity()).showDeletedSnackbar(m);
        nav().popBackStack();
    }
}
