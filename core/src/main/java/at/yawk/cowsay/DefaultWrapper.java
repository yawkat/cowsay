package at.yawk.cowsay;

import java.util.Arrays;
import lombok.Getter;

/**
 * @author yawkat
 */
public class DefaultWrapper extends Wrapper {
    @Getter private static final Wrapper instance = new DefaultWrapper();

    protected DefaultWrapper() {}

    @Override
    public byte getWidth(char c) {
        return 1;
    }

    @Override
    protected boolean isWrappableAfter(String s, int i) {
        return i == s.length() - 1;
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
