package at.yawk.cowsay;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.command.ConsoleCommandSender;
import net.md_5.bungee.event.EventHandler;

/**
 * @author yawkat
 */
public class CowsayBungee extends Plugin implements Listener, CowsayPlatform {
    private final CowsayCommandHandler commandHandler = new CowsayCommandHandler(this);

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);

        Command cowsay = new Command("cowsay", "cowsay.cowsay") {
            @Override
            public void execute(CommandSender sender, String[] args) {
                commandHandler.cowsay(wrap(sender), args);
            }
        };
        getProxy().getPluginManager().registerCommand(this, cowsay);

        Command cowthink = new Command("cowthink", "cowsay.cowthink") {
            @Override
            public void execute(CommandSender sender, String[] args) {
                commandHandler.cowthink(wrap(sender), args);
            }
        };
        getProxy().getPluginManager().registerCommand(this, cowthink);
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        // default permissions
        event.getPlayer().setPermission("cowsay.cowsay", true);
        event.getPlayer().setPermission("cowsay.cowthink", true);
    }

    private CommandSenderWrapper wrap(final CommandSender commandSender) {
        return new CommandSenderWrapper() {
            @SuppressWarnings("deprecation")
            @Override
            public void sendMessage(String... s) {
                commandSender.sendMessages(s);
            }

            @Override
            public Wrapper getWrapper() {
                return commandSender instanceof ConsoleCommandSender ?
                        ConsoleWrapper.getInstance() :
                        MinecraftWrapper.getInstance();
            }

            @Override
            public int getChatWidth() {
                return commandSender instanceof ConsoleCommandSender ?
                        40 : MinecraftWrapper.DEFAULT_CHAT_WIDTH;
            }

            @Override
            public boolean hasPermission(String permission) {
                return commandSender.hasPermission(permission);
            }
        };
    }

    @Override
    public List<CommandSenderWrapper> getSenders(Pattern namePattern) {
        List<CommandSenderWrapper> wrappers = new ArrayList<>();
        for (ProxiedPlayer player : getProxy().getPlayers()) {
            if (namePattern.matcher(player.getName()).matches()) {
                wrappers.add(wrap(player));
            }
        }
        return wrappers;
    }
}
