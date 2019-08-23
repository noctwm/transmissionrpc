package com.github.noctwm.transmissionrpc.ui.fragment.server;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.github.noctwm.R;

public class RemoveServerFragment extends DialogFragment {

    private static final String EXTRA_SERVERS_COUNT = "com.github.noctwm.transmissionrpc.SERVERS_COUNT";

    private RemoveServerListener listener;
    private int selectedCount;

    public static RemoveServerFragment getInstance(int selectedCount) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_SERVERS_COUNT, selectedCount);

        RemoveServerFragment fragment = new RemoveServerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedCount = getArguments().getInt(EXTRA_SERVERS_COUNT, 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RemoveServerListener) {
            listener = (RemoveServerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RemoveServerListener");
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new AlertDialog.Builder(requireContext())
                .setMessage(getResources().getQuantityString(R.plurals.remove_servers_message, selectedCount, selectedCount))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) ->
                        listener.onDialogPositiveClick(this)).create();
    }

    public interface RemoveServerListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }



}
