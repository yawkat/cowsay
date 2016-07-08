package at.yawk.cowsay;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author yawkat
 */
public interface CowsayPlatform {
    List<CommandSenderWrapper> getSenders(Pattern namePattern);
}
