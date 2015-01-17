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
