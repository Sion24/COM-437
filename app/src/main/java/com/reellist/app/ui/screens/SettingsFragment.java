package com.reellist.app.ui.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.reellist.app.R;
import com.reellist.app.logic.SortOption;
import com.reellist.app.ui.ReelListViewModel;

/** Settings / Preferences: default sort, delete confirmation, clear history, about. */
public class SettingsFragment extends BaseFragment {

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ReelListViewModel vm = vm();
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> nav().popBackStack());

        RadioGroup group = view.findViewById(R.id.sort_group);
        for (SortOption option : SortOption.values()) {
            if (option == SortOption.WATCHED_STATUS) continue;
            RadioButton rb = new MaterialRadioButton(requireContext());
            rb.setId(option.ordinal() + 1);
            rb.setText(option.label);
            group.addView(rb);
        }
        SortOption current = vm.getDefaultSort().getValue();
        if (current != null) group.check(current.ordinal() + 1);
        group.setOnCheckedChangeListener((g, checkedId) -> {
            if (checkedId > 0) vm.setDefaultSort(SortOption.values()[checkedId - 1]);
        });

        MaterialSwitch confirm = view.findViewById(R.id.switch_confirm);
        confirm.setChecked(Boolean.TRUE.equals(vm.getConfirmDelete().getValue()));
        confirm.setOnCheckedChangeListener((b, checked) -> vm.setConfirmDelete(checked));

        view.findViewById(R.id.btn_clear).setOnClickListener(v ->
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.clear_history_title)
                        .setMessage(R.string.clear_history_body)
                        .setPositiveButton(R.string.clear, (d, w) -> vm.clearWatchedHistory())
                        .setNegativeButton(R.string.cancel, null)
                        .show());
    }
}
