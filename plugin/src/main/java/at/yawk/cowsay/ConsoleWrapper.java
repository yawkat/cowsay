/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package at.yawk.cowsay;

import java.util.Arrays;
import lombok.Getter;

/**
 * Wrapper for a colored, already monospaced output. Char width is 1.
 *
 * @author yawkat
 */
public final class ConsoleWrapper extends DefaultWrapper {
    @Getter private static final Wrapper instance = new ConsoleWrapper();

    @Override
    protected String collectNextLinePrefix(String currentLine) {
        return MinecraftWrapper.getInstance().collectNextLinePrefix(currentLine);
    }

    @Override
    public int getWidth(CharSequence seq) {
        int width = 0;
        for (int i = 0; i < seq.length() - 1; i++) {
            if (seq.charAt(i) != '§' || MinecraftWrapper.COLORS.indexOf(seq.charAt(i + 1)) == -1) {
                width++;
            } else {
                i++;
            }
        }
        return width;
    }

    @Override
    protected boolean isWrappableAfter(String s, int i) {
        return MinecraftWrapper.getInstance().isWrappableAfter(s, i);
    }
}
