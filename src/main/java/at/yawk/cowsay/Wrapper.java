package at.yawk.cowsay;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yawkat
 */
public abstract class Wrapper {
    public abstract byte getWidth(char c);

    public int getWidth(CharSequence seq) {
        int w = 0;
        for (int i = 0; i < seq.length(); i++) {
            w += getWidth(seq.charAt(i));
        }
        return w;
    }

    protected abstract boolean isWrappableAfter(String s, int i);

    protected String collectNextLinePrefix(String currentLine) {
        return "";
    }

    public List<String> wrap(String s, int maxWidth) {
        List<String> strings = new ArrayList<>();
        String prefix = "";
        while (!s.isEmpty()) {
            s = prefix + s;
            String line = wrapOne(s, maxWidth);
            s = s.substring(line.length());
            if (line.charAt(line.length() - 1) == ' ') {
                line = line.substring(0, line.length() - 1);
            }
            strings.add(line);
            prefix = collectNextLinePrefix(line);
        }
        return strings;
    }

    private String wrapOne(String s, int maxWidth) {
        int lastWhitespaceEnd = 0;
        while (true) {
            int next = s.indexOf(' ', lastWhitespaceEnd);
            if (next == -1) {
                next = s.length();
            }
            int width = getWidth(s.subSequence(0, next));
            if (width < maxWidth) {
                if (next >= s.length()) {
                    return s;
                }
                lastWhitespaceEnd = next + 1;
            } else {
                if (lastWhitespaceEnd != 0) {
                    return s.substring(0, lastWhitespaceEnd);
                }
                break;
            }
        }
        int lastWrappable = 1;
        for (int i = 2; i < s.length(); i++) {
            if (isWrappableAfter(s, i)) {
                if (getWidth(s.subSequence(0, i)) > maxWidth) {
                    return s.substring(0, lastWrappable);
                } else {
                    lastWrappable = i;
                }
            }
        }
        return s.substring(0, lastWrappable);
    }

    public abstract String monospace(CharSequence seq);

    public abstract String getWhitespace(int width);
}
