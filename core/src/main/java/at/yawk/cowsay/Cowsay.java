package at.yawk.cowsay;

import java.util.ArrayList;
import java.util.List;
import lombok.Value;
import lombok.experimental.Builder;

/**
 * @author yawkat
 */
@Value
@Builder
public class Cowsay {
    public static final Cowsay COWSAY = builder()
            .header(new String[0])
            .preLineChar('_')
            .oneLinePrefix("< ")
            .firstLinePrefix("/ ")
            .linePrefix("| ")
            .lastLinePrefix("\\ ")
            .oneLineSuffix(" >")
            .firstLineSuffix(" \\")
            .lineSuffix(" |")
            .lastLineSuffix(" /")
            .postLineChar('-')
            .footer(new String[]{
                    "        \\   ^__^",
                    "         \\  (oo)\\_______",
                    "            (__)\\       )\\/\\",
                    "                ||----w |",
                    "                ||     ||",
            })
            .build();
    public static final Cowsay COWTHINK = builder()
            .header(new String[0])
            .preLineChar('_')
            .oneLinePrefix("( ")
            .firstLinePrefix("( ")
            .linePrefix("( ")
            .lastLinePrefix("( ")
            .oneLineSuffix(" )")
            .firstLineSuffix(" )")
            .lineSuffix(" )")
            .lastLineSuffix(" )")
            .postLineChar('-')
            .footer(new String[]{
                    "        °   ^__^",
                    "         °  (oo)\\_______",
                    "            (__)\\       )\\/\\",
                    "                ||----w |",
                    "                ||     ||",
            })
            .build();

    private String[] header;
    private char preLineChar;
    private String oneLinePrefix;
    private String firstLinePrefix;
    private String linePrefix;
    private String lastLinePrefix;
    private String oneLineSuffix;
    private String firstLineSuffix;
    private String lineSuffix;
    private String lastLineSuffix;
    private char postLineChar;
    private String[] footer;

    public List<String> cowsayBase(String s, String color, Wrapper wrapper, int maxWidth) {
        String prefix0 = color + wrapper.monospace(oneLinePrefix);
        String prefix1 = color + wrapper.monospace(firstLinePrefix);
        String prefix2 = color + wrapper.monospace(linePrefix);
        String prefix3 = color + wrapper.monospace(lastLinePrefix);
        String suffix0 = color + wrapper.monospace(oneLineSuffix);
        String suffix1 = color + wrapper.monospace(firstLineSuffix);
        String suffix2 = color + wrapper.monospace(lineSuffix);
        String suffix3 = color + wrapper.monospace(lastLineSuffix);

        int maxPrefixWidth = Math.max(
                Math.max(wrapper.getWidth(prefix0),
                         wrapper.getWidth(prefix1)),
                Math.max(wrapper.getWidth(prefix2),
                         wrapper.getWidth(prefix3))
        );
        int maxSuffixWidth = Math.max(
                Math.max(wrapper.getWidth(suffix0),
                         wrapper.getWidth(suffix1)),
                Math.max(wrapper.getWidth(suffix2),
                         wrapper.getWidth(suffix3))
        );

        int maxTextWidth = maxWidth - maxPrefixWidth - maxSuffixWidth;

        List<String> wrapped = wrapper.wrap(s, maxTextWidth);
        int maxLineWidth = 0;
        for (String line : wrapped) {
            maxLineWidth = Math.max(maxLineWidth, wrapper.getWidth(line));
        }

        List<String> strings = new ArrayList<>();

        for (String line : header) {
            strings.add(color + wrapper.monospace(line));
        }

        strings.add(color + buildBubbleLine(wrapper, maxLineWidth, maxPrefixWidth, preLineChar));
        for (int i = 0; i < wrapped.size(); i++) {
            String line = wrapped.get(i);
            int rightPad = maxLineWidth - wrapper.getWidth(line);
            String prefix, suffix;
            if (i == 0) {
                if (i == wrapped.size() - 1) {
                    prefix = prefix0;
                    suffix = suffix0;
                } else {
                    prefix = prefix1;
                    suffix = suffix1;
                }
            } else if (i == wrapped.size() - 1) {
                prefix = prefix3;
                suffix = suffix3;
            } else {
                prefix = prefix2;
                suffix = suffix2;
            }
            line = prefix + line + wrapper.getWhitespace(rightPad) + suffix;
            strings.add(line);
        }
        strings.add(color + buildBubbleLine(wrapper, maxLineWidth, maxPrefixWidth, postLineChar));

        for (String line : footer) {
            strings.add(color + wrapper.monospace(line));
        }
        return strings;
    }

    private String buildBubbleLine(Wrapper wrapper, int maxLineWidth, int prefixWidth, char c) {
        StringBuilder builder = new StringBuilder(wrapper.getWhitespace(prefixWidth));
        int bubbleLineWidth = maxLineWidth + wrapper.getWidth(' ') * 2;
        for (int i = 0; i < Math.round(bubbleLineWidth / (float) wrapper.getWidth(c)); i++) {
            builder.append(c);
        }
        return builder.toString();
    }
}
