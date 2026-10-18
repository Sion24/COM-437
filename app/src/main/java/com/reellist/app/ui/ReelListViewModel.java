package com.reellist.app.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.reellist.app.data.Movie;
import com.reellist.app.data.MovieRepository;
import com.reellist.app.data.ReelListDatabase;
import com.reellist.app.data.SettingsStore;
import com.reellist.app.logic.MovieForm;
import com.reellist.app.logic.SortOption;
import com.reellist.app.logic.WatchlistLogic;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Holds the app state for every screen and survives screen rotation
 * (important because ReelList has a separate landscape layout).
 * All screens share this one ViewModel, scoped to MainActivity.
 */
public class ReelListViewModel extends AndroidViewModel {

    private final MovieRepository repo;
    private final SettingsStore settings;

    private final LiveData<List<Movie>> movies;

    // ----- Watchlist filters -----
    private final MutableLiveData<SortOption> sort;
    private final MutableLiveData<String> service = new MutableLiveData<>(WatchlistLogic.ALL_SERVICES);
    private final MediatorLiveData<List<Movie>> watchlist = new MediatorLiveData<>();
    private final LiveData<List<Movie>> watched;

    // ----- "Pick for Tonight" -----
    private final int pickSeed = new Random().nextInt();   // new random pick each app launch
    private final MutableLiveData<Long> pickId = new MutableLiveData<>(null);
    private final MediatorLiveData<Movie> pick = new MediatorLiveData<>();

    // ----- Settings -----
    private final MutableLiveData<SortOption> defaultSort;
    private final MutableLiveData<Boolean> confirmDelete;

    // Landscape Seen screen: which poster is selected in the grid
    private final MutableLiveData<Long> selectedWatchedId = new MutableLiveData<>(null);

    public ReelListViewModel(@NonNull Application app) {
        super(app);
        repo = new MovieRepository(ReelListDatabase.get(app).movieDao());
        settings = new SettingsStore(app);

        movies = repo.getMovies();
        sort = new MutableLiveData<>(settings.getDefaultSort());
        defaultSort = new MutableLiveData<>(settings.getDefaultSort());
        confirmDelete = new MutableLiveData<>(settings.getConfirmBeforeDelete());

        watchlist.addSource(movies, x -> recomputeWatchlist());
        watchlist.addSource(sort, x -> recomputeWatchlist());
        watchlist.addSource(service, x -> recomputeWatchlist());

        watched = Transformations.map(movies, WatchlistLogic::watched);

        pick.addSource(movies, x -> recomputePick());
        pick.addSource(pickId, x -> recomputePick());
    }

    private List<Movie> all() {
        List<Movie> list = movies.getValue();
        return list == null ? Collections.emptyList() : list;
    }

    private void recomputeWatchlist() {
        List<Movie> filtered = WatchlistLogic.filterByService(WatchlistLogic.toWatch(all()), service.getValue());
        watchlist.setValue(WatchlistLogic.sort(filtered, sort.getValue()));
    }

    private void recomputePick() {
        Long id = pickId.getValue();
        if (id != null) {
            for (Movie m : all()) {
                if (m.id == id && !m.watched) {
                    pick.setValue(m);
                    return;
                }
            }
        }
        pick.setValue(WatchlistLogic.pickForTonight(all(), null, new Random(pickSeed)));
    }

    // ----- Read-only state for the screens -----
    public LiveData<List<Movie>> getMovies() { return movies; }
    public LiveData<List<Movie>> getWatchlist() { return watchlist; }
    public LiveData<List<Movie>> getWatched() { return watched; }
    public LiveData<SortOption> getSort() { return sort; }
    public LiveData<String> getService() { return service; }
    public LiveData<Movie> getPick() { return pick; }
    public LiveData<SortOption> getDefaultSort() { return defaultSort; }
    public LiveData<Boolean> getConfirmDelete() { return confirmDelete; }
    public LiveData<Long> getSelectedWatchedId() { return selectedWatchedId; }
    public LiveData<Movie> movie(long id) { return repo.movie(id); }

    public void setSort(SortOption option) { sort.setValue(option); }
    public void setService(String s) { service.setValue(s); }
    public void selectWatched(long id) { selectedWatchedId.setValue(id); }

    public void shufflePick() {
        Movie next = WatchlistLogic.pickForTonight(all(), pick.getValue(), new Random());
        pickId.setValue(next == null ? null : next.id);
    }

    // ----- Single movie actions (all used from Movie Details, the editing hub) -----
    public void saveForm(MovieForm form, Movie existing, MovieRepository.Callback<Long> onSaved) {
        if (existing == null) {
            repo.add(form.toMovie(null), onSaved);
        } else {
            repo.save(form.toMovie(existing));
            onSaved.onResult(existing.id);
        }
    }

    public void setWatched(Movie movie, boolean isWatched) { repo.markWatched(movie, isWatched); }
    public void rate(Movie movie, int stars) { repo.rate(movie, stars); }
    public void delete(Movie movie) { repo.delete(movie); }

    /** Undo after delete: put the same movie back (Room gives it a new id). */
    public void restore(Movie movie) {
        Movie m = movie.copy();
        m.id = 0;
        repo.add(m, null);
    }

    // ----- Settings -----
    public void setDefaultSort(SortOption option) {
        settings.setDefaultSort(option);
        defaultSort.setValue(option);
        sort.setValue(option);
    }

    public void setConfirmDelete(boolean enabled) {
        settings.setConfirmBeforeDelete(enabled);
        confirmDelete.setValue(enabled);
    }

    public void clearWatchedHistory() { repo.clearWatchedHistory(); }
}
