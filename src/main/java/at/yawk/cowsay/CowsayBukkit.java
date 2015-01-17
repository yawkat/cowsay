package at.yawk.cowsay;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author yawkat
 */
public class CowsayBukkit extends JavaPlugin {
    private final CowsayCommandHandler commandHandler = new CowsayCommandHandler();

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        CommandSenderWrapper senderWrapper = new CommandSenderWrapper() {
            @Override
            public void sendMessage(String... s) {
                sender.sendMessage(s);
            }

            @Override
            public Wrapper getWrapper() {
                return sender instanceof ConsoleCommandSender ?
                        ConsoleWrapper.getInstance() :
                        MinecraftWrapper.getInstance();
            }

            @Override
            public int getChatWidth() {
                return sender instanceof ConsoleCommandSender ?
                        40 : MinecraftWrapper.DEFAULT_CHAT_WIDTH;
            }
        };
        if (command.getName().equals("cowsay")) {
            commandHandler.cowsay(senderWrapper, args);
            return true;
        }
        if (command.getName().equals("cowthink")) {
            commandHandler.cowthink(senderWrapper, args);
            return true;
        }
        return false;
    }
}
