package at.yawk.cowsay;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import lombok.Getter;

/**
 * @author yawkat
 */
public class MinecraftWrapper extends Wrapper {
    private static final int MONOSPACED_WIDTH = 6;
    public static final int DEFAULT_CHAT_WIDTH = 320;

    // don't think there are others that aren't 0-width :(
    private static final String WHITESPACE = " ";
    private static final String COLORS = "0123456789abcdefklmnor";

    @Getter private static final MinecraftWrapper instance = new MinecraftWrapper();

    private byte[] widths = new byte[Character.MAX_VALUE + 1];

    static {
        try {
            instance.load();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void load() throws IOException {
        try (InputStream stream = MinecraftWrapper.class.getResourceAsStream("/char_sizes")) {
            int i = 0;
            while (i < widths.length) {
                i += stream.read(widths, i, widths.length - i);
            }
        }
    }

    @Override
    public byte getWidth(char c) {
        return widths[c];
    }

    @Override
    public int getWidth(CharSequence seq) {
        int width = 0;
        for (int i = 0; i < seq.length(); i++) {
            char c = seq.charAt(i);
            if (c == '§' && i < seq.length() - 1) {
                char peek = seq.charAt(i + 1);
                if (COLORS.indexOf(peek) != -1) {
                    i++; // skip
                    continue;
                }
            }
            width += getWidth(c);
        }
        return width;
    }

    @Override
    protected boolean isWrappableAfter(String s, int i) {
        if (i == s.length() - 1) { return true; }
        if (s.charAt(i) != '§') { return true; }
        if (COLORS.indexOf(s.charAt(i + 1)) != -1) { return true; }
        return false;
    }

    @Override
    public String monospace(CharSequence seq) {
        StringBuilder builder = new StringBuilder();
        int width = 0;
        for (int i = 0; i < seq.length(); i++) {
            char c = seq.charAt(i);
            boolean whiteSpace = WHITESPACE.indexOf(c) != -1;
            int charWidth = whiteSpace ? 0 : getWidth(c);
            int halfWidth = charWidth / 2;
            int expectedCenterPosition = i * MONOSPACED_WIDTH + MONOSPACED_WIDTH / 2;
            int toFillUp = expectedCenterPosition - width - halfWidth;
            width += fillUpWithWhitespace(builder, toFillUp);
            if (!whiteSpace) {
                builder.append(c);
                width += charWidth;
            }
        }
        int targetWidth = width - seq.length() * MONOSPACED_WIDTH;
        fillUpWithWhitespace(builder, targetWidth);
        return builder.toString();
    }

    @Override
    public String getWhitespace(int width) {
        StringBuilder builder = new StringBuilder();
        fillUpWithWhitespace(builder, width);
        return builder.toString();
    }

    private int fillUpWithWhitespace(StringBuilder builder, int toFillUp) {
        int added = 0;
        while (toFillUp > 0) {
            char bestC = 0;
            int bestWidth = Integer.MAX_VALUE;
            for (int j = 0; j < WHITESPACE.length(); j++) {
                char w = WHITESPACE.charAt(j);
                int ww = getWidth(w);
                if (ww / 2 >= toFillUp) {
                    // don't even try this char if adding it would actually move the center further away
                    continue;
                }
                if (Math.abs(ww - toFillUp) < Math.abs(bestWidth - toFillUp)) {
                    bestC = w;
                    bestWidth = ww;
                }
            }
            if (bestWidth != Integer.MAX_VALUE) {
                builder.append(bestC);
                added += bestWidth;
                toFillUp -= bestWidth;
            } else {
                break;
            }
        }
        return added;
    }
}
