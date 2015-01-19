package at.yawk.cowsay;

/**
 * @author yawkat
 */
public interface CommandSenderWrapper {
    void sendMessage(String... s);

    Wrapper getWrapper();

    int getChatWidth();

    boolean hasPermission(String permission);
}
