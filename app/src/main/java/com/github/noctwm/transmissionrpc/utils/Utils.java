package com.github.noctwm.transmissionrpc.utils;

import android.content.Context;
import androidx.core.util.Pair;

import com.github.noctwm.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utils {

    private static DecimalFormat decimalFormat = new DecimalFormat( "0.#", new DecimalFormatSymbols(Locale.US));

    public static String formatByteSize(Context context, long bytes) {
        Pair p = calcSize(bytes);
        String numString = decimalFormat.format(p.first);
        return String.format(Locale.US, context.getResources().getStringArray(R.array.size_units)[(int) p.second], numString);
    }

    public static String formatByteSpeed(Context context, long bytes) {
        Pair p = calcSize(bytes);
        String numString = decimalFormat.format(p.first);
        return String.format(Locale.US, context.getResources().getStringArray(R.array.speed_units)[(int) p.second], numString);
    }

    public static String formatDuration(Context context, int seconds) {
        if (seconds < 0) {
            return "\u221E";
        }
        int days = seconds / 86400;
        seconds %= 86400;
        int hours = seconds / 3600;
        seconds %= 3600;
        int minutes = seconds / 60;
        seconds %= 60;

        if (days > 0) {
            return context.getString(R.string.duration_days, days, hours);
        }
        if (hours > 0) {
            return context.getString(R.string.duration_hours, hours, minutes);
        }
        if (minutes > 0) {
            return context.getString(R.string.duration_minutes, minutes, seconds);
        }
        return context.getString(R.string.duration_seconds, seconds);
    }

    private static Pair calcSize(long bytes) {
        int unit = 0;
        double size = (double) bytes;
        while (size >= 1024) {
            size /= 1024;
            unit++;
        }

        return new Pair<>(size, unit);
    }




}
