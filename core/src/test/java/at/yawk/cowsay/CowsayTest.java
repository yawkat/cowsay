package at.yawk.cowsay;

import java.util.Arrays;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author yawkat
 */
public class CowsayTest {
    @Test
    public void basic() {
        assertEquals(
                Cowsay.COWSAY.buildCowsayImage("abc", 80),
                Arrays.asList(
                        " _____",
                        "< abc >",
                        " -----",
                        "        \\   ^__^",
                        "         \\  (oo)\\_______",
                        "            (__)\\       )\\/\\",
                        "                ||----w |",
                        "                ||     ||"
                )
        );
        assertEquals(
                Cowsay.COWTHINK.buildCowsayImage("abc", 80),
                Arrays.asList(
                        " _____",
                        "( abc )",
                        " -----",
                        "        °   ^__^",
                        "         °  (oo)\\_______",
                        "            (__)\\       )\\/\\",
                        "                ||----w |",
                        "                ||     ||"
                )
        );
    }

    @Test
    public void empty() {
        assertEquals(
                Cowsay.COWSAY.buildCowsayImage("", 80),
                Arrays.asList(
                        " __",
                        "<  >",
                        " --",
                        "        \\   ^__^",
                        "         \\  (oo)\\_______",
                        "            (__)\\       )\\/\\",
                        "                ||----w |",
                        "                ||     ||"
                )
        );
    }

    @Test
    public void multiLine() {
        assertEquals(
                Cowsay.COWSAY.buildCowsayImage("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do " +
                                               "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim " +
                                               "ad minim veniam,", 44),
                Arrays.asList(
                        " _________________________________________",
                        "/ Lorem ipsum dolor sit amet, consectetur \\",
                        "| adipisicing elit, sed do eiusmod tempor |",
                        "| incididunt ut labore et dolore magna    |",
                        "\\ aliqua. Ut enim ad minim veniam,        /",
                        " -----------------------------------------",
                        "        \\   ^__^",
                        "         \\  (oo)\\_______",
                        "            (__)\\       )\\/\\",
                        "                ||----w |",
                        "                ||     ||"
                )
        );
    }
}