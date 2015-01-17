package at.yawk.cowsay;

import java.util.List;

/**
 * @author yawkat
 */
public class CowsayCommandHandler {
    public void cowsay(CommandSenderWrapper sender, String... args) {
        cowsayCommand(Cowsay.COWSAY, sender, args);
    }

    public void cowthink(CommandSenderWrapper sender, String... args) {
        cowsayCommand(Cowsay.COWTHINK, sender, args);
    }

    private void cowsayCommand(Cowsay cowsay, CommandSenderWrapper sender, String[] args) {
        List<String> parts = cowsay.cowsayBase(join(args, 0), sender.getWrapper(), sender.getChatWidth());
        sender.sendMessage(parts.toArray(new String[parts.size()]));
    }

    private static String join(String[] args, int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i > start) { builder.append(' '); }
            builder.append(args[i]);
        }
        return builder.toString();
    }
}
