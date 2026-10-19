package com.reellist.app.ui.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavOptions;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.reellist.app.R;
import com.reellist.app.data.Movie;
import com.reellist.app.data.MovieOptions;
import com.reellist.app.logic.MovieForm;

import java.util.Calendar;
import java.util.Map;

/**
 * Add Movie (movieId == -1) and Edit Movie (any other id) share one form:
 * title, year, genre, where to watch, runtime, notes. Only Title is required,
 * so adding a recommendation takes seconds.
 */
public class AddEditMovieFragment extends BaseFragment {

    private Movie existing;
    private boolean loaded;

    public AddEditMovieFragment() {
        super(R.layout.fragment_add_edit);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        long movieId = getArguments() == null ? -1L : getArguments().getLong("movieId", -1L);
        boolean editing = movieId != -1L;
        // Text typed before a rotation is restored automatically by Android, so don't overwrite it
        loaded = savedInstanceState != null;

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(editing ? R.string.edit_movie : R.string.add_movie);
        toolbar.setNavigationOnClickListener(v -> nav().popBackStack());

        // Dropdowns that still let the user type their own value (e.g. a niche streaming service)
        ((MaterialAutoCompleteTextView) view.findViewById(R.id.input_genre)).setAdapter(
                new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, MovieOptions.GENRES));
        ((MaterialAutoCompleteTextView) view.findViewById(R.id.input_service)).setAdapter(
                new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, MovieOptions.SERVICES));

        if (editing) {
            vm().movie(movieId).observe(getViewLifecycleOwner(), m -> {
                existing = m;
                if (m != null && !loaded) {      // fill the form once with the saved values
                    MovieForm f = MovieForm.from(m);
                    set(view, R.id.input_title, f.title);
                    set(view, R.id.input_year, f.year);
                    set(view, R.id.input_runtime, f.runtime);
                    ((MaterialAutoCompleteTextView) view.findViewById(R.id.input_genre)).setText(f.genre, false);
                    ((MaterialAutoCompleteTextView) view.findViewById(R.id.input_service)).setText(f.whereToWatch, false);
                    set(view, R.id.input_notes, f.notes);
                    loaded = true;
                }
            });
        }

        view.findViewById(R.id.btn_save).setOnClickListener(v -> save(view, editing));
    }

    private void save(View view, boolean editing) {
        MovieForm form = new MovieForm(
                text(view, R.id.input_title), text(view, R.id.input_year), text(view, R.id.input_genre),
                text(view, R.id.input_service), text(view, R.id.input_runtime), text(view, R.id.input_notes));
        Map<String, String> errors = form.validate(Calendar.getInstance().get(Calendar.YEAR));
        ((TextInputLayout) view.findViewById(R.id.input_title_layout)).setError(errors.get("title"));
        ((TextInputLayout) view.findViewById(R.id.input_year_layout)).setError(errors.get("year"));
        ((TextInputLayout) view.findViewById(R.id.input_runtime_layout)).setError(errors.get("runtime"));
        if (!errors.isEmpty()) return;
        if (editing && existing == null) return;   // still loading

        vm().saveForm(form, editing ? existing : null, id -> {
            if (!isAdded()) return;
            if (editing) {
                nav().popBackStack();
            } else {
                // After adding, show the new title's details and drop the form from the back stack
                Bundle args = new Bundle();
                args.putLong("movieId", id);
                NavOptions opts = new NavOptions.Builder().setPopUpTo(R.id.addEditFragment, true).build();
                nav().navigate(R.id.detailsFragment, args, opts);
            }
        });
    }

    private static String text(View root, int id) {
        CharSequence s = ((EditText) root.findViewById(id)).getText();
        return s == null ? "" : s.toString();
    }

    private static void set(View root, int id, String value) {
        ((EditText) root.findViewById(id)).setText(value);
    }
}
