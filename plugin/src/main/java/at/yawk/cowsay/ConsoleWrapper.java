package at.yawk.cowsay;

import java.util.Arrays;
import lombok.Getter;

/**
 * Wrapper for a colored, already monospaced output. Char width is 1.
 *
 * @author yawkat
 */
public final class ConsoleWrapper extends Wrapper {
    @Getter private static final Wrapper instance = new ConsoleWrapper();

    @Override
    public byte getWidth(char c) {
        return 1;
    }

    @Override
    protected String collectNextLinePrefix(String currentLine) {
        return MinecraftWrapper.getInstance().collectNextLinePrefix(currentLine);
    }

    @Override
    public int getWidth(CharSequence seq) {
        int width = 0;
        for (int i = 0; i < seq.length() - 1; i++) {
            if (seq.charAt(i) != '§' || MinecraftWrapper.COLORS.indexOf(seq.charAt(i + 1)) == -1) {
                width++;
            } else {
                i++;
            }
        }
        return width;
    }

    @Override
    protected boolean isWrappableAfter(String s, int i) {
        return MinecraftWrapper.getInstance().isWrappableAfter(s, i);
    }

    @Override
    public String monospace(CharSequence seq) {
        return seq.toString();
    }

    @Override
    public String getWhitespace(int width) {
        char[] chars = new char[width];
        Arrays.fill(chars, ' ');
        return new String(chars);
    }
}
