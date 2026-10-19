package com.reellist.app.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.reellist.app.R;
import com.reellist.app.data.Movie;
import com.reellist.app.logic.WatchlistLogic;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/** Small reusable pieces of UI shared by several screens (poster tile, star rating, movie row). */
public final class MovieViews {
    private MovieViews() { }

    public interface OnRate { void onRate(int stars); }
    public interface OnWatchedChange { void onChange(Movie movie, boolean watched); }

    private static final int[] PALETTE = {0xFF3D5A80, 0xFF8E3B46, 0xFF2A7F62, 0xFF6B4E9B, 0xFFB5651D, 0xFF35626F};

    /** Poster placeholder: a colored tile with the title's initials (no internet / images needed yet). */
    public static void bindPoster(TextView tile, String title) {
        StringBuilder initials = new StringBuilder();
        for (String word : title.trim().split("\\s+")) {
            if (!word.isEmpty() && initials.length() < 2) initials.append(word.substring(0, 1).toUpperCase(Locale.ROOT));
        }
        tile.setText(initials.length() == 0 ? "?" : initials.toString());
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(PALETTE[(title.hashCode() & 0x7FFFFFFF) % PALETTE.length]);
        bg.setCornerRadius(dp(tile.getContext(), 10));
        tile.setBackground(bg);
    }

    /**
     * Fills {@code row} with 5 stars. Tap a star to rate; tap the same star again to clear.
     * Pass {@code onRate = null} for a read-only rating.
     */
    public static void bindStars(LinearLayout row, int rating, int sizeDp, @Nullable OnRate onRate) {
        Context c = row.getContext();
        row.removeAllViews();
        row.setContentDescription(c.getString(R.string.star_cd, rating));
        int gold = ContextCompat.getColor(c, R.color.gold);
        int off = ContextCompat.getColor(c, R.color.muted) & 0x5AFFFFFF;
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(c);
            int pad = onRate == null ? 0 : (int) dp(c, 6);
            int size = (int) dp(c, sizeDp) + pad * 2;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
            lp.setMarginEnd((int) dp(c, 2));
            star.setLayoutParams(lp);
            star.setPadding(pad, pad, pad, pad);
            star.setImageResource(i <= rating ? R.drawable.ic_star : R.drawable.ic_star_border);
            star.setImageTintList(ColorStateList.valueOf(i <= rating ? gold : off));
            if (onRate != null) {
                final int stars = i;
                star.setContentDescription(c.getString(R.string.star_cd, i));
                star.setOnClickListener(v -> onRate.onRate(rating == stars ? 0 : stars));
            } else {
                star.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            }
            row.addView(star);
        }
    }

    /**
     * One movie in a list (layout item_movie.xml). The checkbox means the same thing on every
     * screen: empty = not watched yet, filled = watched.
     */
    public static void bindMovieRow(View row, Movie movie, boolean showRating, boolean showWatchedDate,
                                    View.OnClickListener onClick, OnWatchedChange onWatchedChange) {
        Context c = row.getContext();
        bindPoster(row.findViewById(R.id.poster), movie.title);
        ((TextView) row.findViewById(R.id.title)).setText(movie.title);

        TextView detail = row.findViewById(R.id.detail);
        String line = WatchlistLogic.detailLine(movie);
        detail.setText(line);
        detail.setVisibility(line.isEmpty() ? View.GONE : View.VISIBLE);

        TextView service = row.findViewById(R.id.service);
        service.setText(movie.whereToWatch);
        service.setVisibility(movie.whereToWatch.isEmpty() ? View.GONE : View.VISIBLE);

        LinearLayout stars = row.findViewById(R.id.stars);
        stars.setVisibility(showRating ? View.VISIBLE : View.GONE);
        if (showRating) bindStars(stars, movie.rating, 16, null);

        TextView date = row.findViewById(R.id.watched_date);
        date.setVisibility(showWatchedDate ? View.VISIBLE : View.GONE);
        if (showWatchedDate) date.setText(c.getString(R.string.watched_on, formatDate(movie.dateWatched)));

        CheckBox box = row.findViewById(R.id.watched_box);
        box.setOnCheckedChangeListener(null);
        box.setChecked(movie.watched);
        box.setContentDescription(c.getString(R.string.mark_watched_cd, movie.title));
        box.setOnCheckedChangeListener((b, checked) -> onWatchedChange.onChange(movie, checked));

        row.setOnClickListener(onClick);
    }

    public static String formatDate(@Nullable Long millis) {
        return millis == null ? "\u2014" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(millis));
    }

    public static float dp(Context c, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, c.getResources().getDisplayMetrics());
    }

    public static boolean isLandscape(Context c) {
        return c.getResources().getBoolean(R.bool.is_landscape);
    }
}
