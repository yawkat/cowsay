package at.yawk.cowsay;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
public class CowsayCommandHandler {
    private final CowsayPlatform platform;

    public void cowsay(CommandSenderWrapper sender, String... args) {
        cowsayCommand(Cowsay.COWSAY, sender, args);
    }

    public void cowthink(CommandSenderWrapper sender, String... args) {
        cowsayCommand(Cowsay.COWTHINK, sender, args);
    }

    private void cowsayCommand(Cowsay cowsay, CommandSenderWrapper sender, String[] args) {
        CommandArguments ca = CommandArguments.parse(args);

        String color = null;
        if (ca.getOptions().containsKey("-c")) {
            if (!sender.hasPermission("cowsay.color.cow")) {
                sender.sendMessage("§e§oYou are not permitted to color the cow.");
                return;
            }
            color = ca.getOptions().get("-c");
        }

        String text = ca.getFreeArguments();
        if (ca.getOptions().containsKey("-s")) {
            if (!sender.hasPermission("cowsay.color.message")) {
                sender.sendMessage("§e§oYou are not permitted to color messages.");
                return;
            }
            String needle = ca.getOptions().get("-s");
            if (color != null) {
                color = color.replace(needle, "§");
            } else {
                color = "§r"; // use reset so we stay white after wrapped colors
            }
            text = text.replace(needle, "§");
        } else {
            if (color == null) {
                color = "";
            }
        }

        List<String> parts = cowsay.cowsayBase(text, color, sender.getWrapper(), sender.getChatWidth());

        Collection<CommandSenderWrapper> recipients;
        if (ca.getOptions().containsKey("-p")) {
            if (!sender.hasPermission("cowsay.other")) {
                sender.sendMessage("§e§oYou are not permitted to send cowsay to others.");
                return;
            }
            int flags = 0;
            if (ca.getOptions().containsKey("-i")) { flags |= Pattern.CASE_INSENSITIVE; }
            if (ca.getOptions().containsKey("-l")) { flags |= Pattern.LITERAL; }
            Pattern pattern = Pattern.compile(ca.getOptions().get("-p"), flags);

            recipients = platform.getSenders(pattern);
        } else {
            recipients = Collections.singleton(sender);
        }

        String[] arr = parts.toArray(new String[parts.size()]);
        for (CommandSenderWrapper recipient : recipients) {
            recipient.sendMessage(arr);
        }
    }
}
