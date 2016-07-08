/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package at.yawk.cowsay;

import java.util.HashMap;
import java.util.Map;
import lombok.Value;

/**
 * @author yawkat
 */
@Value
public class CommandArguments {
    private String freeArguments;
    private Map<String, String> options;

    public static CommandArguments parse(String... args) {
        // 0: before option or text
        // 1: in option
        // 2: in text
        int mode = 0;
        String option = null;
        StringBuilder optionValue = new StringBuilder();
        StringBuilder text = new StringBuilder();
        Map<String, String> options = new HashMap<>();

        for (String arg : args) {
            switch (mode) {
            case 0:
                if (arg.startsWith("-")) {
                    if (arg.equals("--")) {
                        mode = 2;
                    } else {
                        option = arg;
                        mode = 1;
                    }
                } else {
                    text.append(arg);
                    mode = 2;
                }
                break;
            case 1:
                if (!arg.startsWith("-")) {
                    if (arg.startsWith("\\-")) {
                        optionValue.append(arg, 1, arg.length());
                    } else {
                        optionValue.append(arg);
                    }
                    if (arg.endsWith("\\")) {
                        optionValue.setLength(optionValue.length() - 1);
                        break;
                    }
                }
                mode = 0;
                options.put(option, optionValue.toString());
                optionValue.setLength(0);
                break;
            case 2:
                if (text.length() > 0) { text.append(' '); }
                text.append(arg);
                break;
            }
        }

        return new CommandArguments(text.toString(), options);
    }
}
