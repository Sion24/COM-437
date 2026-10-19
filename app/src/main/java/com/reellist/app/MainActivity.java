package com.reellist.app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.reellist.app.data.Movie;
import com.reellist.app.ui.ReelListViewModel;

import androidx.lifecycle.ViewModelProvider;

/**
 * The single Activity. It hosts every screen (Fragment) and the Home / List / Seen navigation:
 * a bottom bar in portrait and a side rail in landscape (layout-land/activity_main.xml).
 */
public class MainActivity extends AppCompatActivity {

    private NavController nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        nav = host.getNavController();

        // BottomNavigationView (portrait) and NavigationRailView (landscape) share this base class.
        // NavigationUI switches tabs without stacking them, so Back never walks through old tabs.
        NavigationBarView tabs = findViewById(R.id.nav_tabs);
        NavigationUI.setupWithNavController(tabs, nav);

        // Only show the tabs on the three top-level screens
        nav.addOnDestinationChangedListener((controller, destination, args) -> {
            int id = destination.getId();
            boolean topLevel = id == R.id.homeFragment || id == R.id.watchlistFragment || id == R.id.watchedFragment;
            tabs.setVisibility(topLevel ? View.VISIBLE : View.GONE);
        });
    }

    /** Called by Movie Details after a delete. The snackbar lives here so it survives the screen closing. */
    public void showDeletedSnackbar(Movie deleted) {
        ReelListViewModel vm = new ViewModelProvider(this).get(ReelListViewModel.class);
        Snackbar.make(findViewById(R.id.root), getString(R.string.deleted_fmt, deleted.title), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> vm.restore(deleted))
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return nav.navigateUp() || super.onSupportNavigateUp();
    }
}
