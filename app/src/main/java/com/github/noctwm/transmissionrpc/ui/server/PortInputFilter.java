package com.github.noctwm.transmissionrpc.ui.server;

import android.text.InputFilter;
import android.text.Spanned;

public class PortInputFilter implements InputFilter {

    private final static int MAX_PORT_NUMBER = 65535;

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.length() == 0)
            return null;
        String result = resultingText(source, start, end, dest, dstart, dend);
        if (result.startsWith("0"))
            return "";
        try {
            int port = Integer.parseInt(result);
            if (port > MAX_PORT_NUMBER)
                return dest.subSequence(dstart, dend);
        } catch (NumberFormatException e) {
            return "";
        }
        return null;
    }

    private String resultingText(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return String.valueOf(dest.subSequence(0, dstart)) +
                source.subSequence(start, end) +
                dest.subSequence(dend, dest.length());
    }
}
