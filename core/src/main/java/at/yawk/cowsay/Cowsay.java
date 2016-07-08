package at.yawk.cowsay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
            .header(Collections.<String>emptyList())
            .bubbleTopChar('_')

            .singleLineBubblePrefix("< ")
            .firstLineBubblePrefix("/ ")
            .otherLineBubblePrefix("| ")
            .lastLineBubblePrefix("\\ ")

            .singleLineBubbleSuffix(" >")
            .firstLineBubbleSuffix(" \\")
            .otherLineBubbleSuffix(" |")
            .lastLineBubbleSuffix(" /")

            .bubbleBottomChar('-')
            .footer(Arrays.asList(
                    "        \\   ^__^",
                    "         \\  (oo)\\_______",
                    "            (__)\\       )\\/\\",
                    "                ||----w |",
                    "                ||     ||"
            ))
            .build();
    public static final Cowsay COWTHINK = builder()
            .header(Collections.<String>emptyList())
            .bubbleTopChar('_')

            .singleLineBubblePrefix("( ")
            .firstLineBubblePrefix("( ")
            .otherLineBubblePrefix("( ")
            .lastLineBubblePrefix("( ")

            .singleLineBubbleSuffix(" )")
            .firstLineBubbleSuffix(" )")
            .otherLineBubbleSuffix(" )")
            .lastLineBubbleSuffix(" )")

            .bubbleBottomChar('-')
            .footer(Arrays.asList(
                    "        °   ^__^",
                    "         °  (oo)\\_______",
                    "            (__)\\       )\\/\\",
                    "                ||----w |",
                    "                ||     ||"
            ))
            .build();

    /**
     * Header of the image.
     */
    private List<String> header;
    /**
     * Top delimiter of the message bubble.
     */
    private char bubbleTopChar;

    /**
     * Left delimiter of the bubble if the bubble only contains a single line.
     */
    private String singleLineBubblePrefix;
    /**
     * Left delimiter of the first line of the bubble.
     */
    private String firstLineBubblePrefix;
    /**
     * Left delimiter of the middle lines of the bubble.
     */
    private String otherLineBubblePrefix;
    /**
     * Left delimiter of the last line of the bubble.
     */
    private String lastLineBubblePrefix;

    /**
     * Right delimiter of the bubble if the bubble only contains a single line.
     */
    private String singleLineBubbleSuffix;
    /**
     * Right delimiter of the first line of the bubble.
     */
    private String firstLineBubbleSuffix;
    /**
     * Right delimiter of the middle lines of the bubble.
     */
    private String otherLineBubbleSuffix;
    /**
     * Right delimiter of the last line of the bubble.
     */
    private String lastLineBubbleSuffix;

    /**
     * Bottom delimiter of the bubble.
     */
    private char bubbleBottomChar;
    /**
     * Footer of the image.
     */
    private List<String> footer;

    /**
     * Build a cowsay image using the given message and maximum width. Uses the default text wrapper.
     *
     * @see #buildCowsayImage(String, Wrapper, int)
     */
    public List<String> buildCowsayImage(String message, int maxWidth) {
        return buildCowsayImage(message, DefaultWrapper.getInstance(), maxWidth);
    }

    /**
     * Build a cowsay image using the given message, wrapper and maximum width.
     *
     * @param message  The message to appear in the bubble.
     * @param wrapper  The text wrapper to use for line wrapping.
     * @param maxWidth The maximum width of the whole output (including the bubble delimiters), in the units used by
     *                 the given wrapper.
     * @return A list of lines for this cowsay image.
     */
    public List<String> buildCowsayImage(String message, Wrapper wrapper, int maxWidth) {
        return buildCowsayImage(message, "", wrapper, maxWidth);
    }

    List<String> buildCowsayImage(String message, String color, Wrapper wrapper, int maxWidth) {
        String prefix0 = color + wrapper.monospace(singleLineBubblePrefix);
        String prefix1 = color + wrapper.monospace(firstLineBubblePrefix);
        String prefix2 = color + wrapper.monospace(otherLineBubblePrefix);
        String prefix3 = color + wrapper.monospace(lastLineBubblePrefix);

        String suffix0 = color + wrapper.monospace(singleLineBubbleSuffix);
        String suffix1 = color + wrapper.monospace(firstLineBubbleSuffix);
        String suffix2 = color + wrapper.monospace(otherLineBubbleSuffix);
        String suffix3 = color + wrapper.monospace(lastLineBubbleSuffix);

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

        // wrap text
        List<String> wrapped = wrapper.wrap(message, maxTextWidth);
        if (wrapped.isEmpty()) { wrapped = Collections.singletonList(""); }
        int maxLineWidth = 0;
        for (String line : wrapped) {
            maxLineWidth = Math.max(maxLineWidth, wrapper.getWidth(line));
        }

        List<String> outputLines = new ArrayList<>();

        for (String line : header) {
            outputLines.add(color + wrapper.monospace(line));
        }

        outputLines.add(color + buildBubbleLine(wrapper, maxLineWidth, maxPrefixWidth, bubbleTopChar));
        for (int i = 0; i < wrapped.size(); i++) {
            String inputLine = wrapped.get(i);
            int rightPad = maxLineWidth - wrapper.getWidth(inputLine);
            String prefix, suffix;
            if (i == 0) {
                if (wrapped.size() == 1) {
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
            outputLines.add(prefix + inputLine + wrapper.getWhitespace(rightPad) + suffix);
        }
        outputLines.add(color + buildBubbleLine(wrapper, maxLineWidth, maxPrefixWidth, bubbleBottomChar));

        for (String line : footer) {
            outputLines.add(color + wrapper.monospace(line));
        }
        return outputLines;
    }

    private String buildBubbleLine(Wrapper wrapper, int maxLineWidth, int prefixWidth, char c) {
        StringBuilder builder = new StringBuilder(wrapper.getWhitespace(prefixWidth - wrapper.getWidth(' ')));
        int bubbleLineWidth = maxLineWidth + wrapper.getWidth(' ') * 2;
        for (int i = 0; i < Math.round(bubbleLineWidth / (float) wrapper.getWidth(c)); i++) {
            builder.append(c);
        }
        return builder.toString();
    }
}
