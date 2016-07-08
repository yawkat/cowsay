/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package at.yawk.cowsay;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author yawkat
 */
public class CowsayBukkit extends JavaPlugin implements CowsayPlatform {
    private final CowsayCommandHandler commandHandler = new CowsayCommandHandler(this);

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        CommandSenderWrapper senderWrapper = wrap(sender);
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

    private CommandSenderWrapper wrap(final CommandSender sender) {
        return new CommandSenderWrapper() {
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

            @Override
            public boolean hasPermission(String permission) {
                return sender.hasPermission(permission);
            }
        };
    }

    @Override
    public List<CommandSenderWrapper> getSenders(Pattern namePattern) {
        List<CommandSenderWrapper> wrappers = new ArrayList<>();
        for (Player player : getServer().getOnlinePlayers()) {
            if (namePattern.matcher(player.getName()).matches()) {
                wrappers.add(wrap(player));
            }
        }
        return wrappers;
    }
}
