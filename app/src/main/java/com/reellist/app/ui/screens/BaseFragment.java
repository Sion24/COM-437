package com.reellist.app.ui.screens;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.reellist.app.R;
import com.reellist.app.ui.MovieViews;
import com.reellist.app.ui.ReelListViewModel;

/** Shared helpers for every screen: the shared ViewModel, navigation and orientation. */
public abstract class BaseFragment extends Fragment {

    protected BaseFragment(@LayoutRes int layout) {
        super(layout);
    }

    /** One ViewModel shared by all screens, owned by MainActivity. */
    protected ReelListViewModel vm() {
        return new ViewModelProvider(requireActivity()).get(ReelListViewModel.class);
    }

    protected NavController nav() {
        return NavHostFragment.findNavController(this);
    }

    protected boolean landscape() {
        return MovieViews.isLandscape(requireContext());
    }

    protected void openDetails(long movieId) {
        Bundle args = new Bundle();
        args.putLong("movieId", movieId);
        nav().navigate(R.id.detailsFragment, args);
    }

    protected void openAddMovie() {
        nav().navigate(R.id.addEditFragment);
    }
}
