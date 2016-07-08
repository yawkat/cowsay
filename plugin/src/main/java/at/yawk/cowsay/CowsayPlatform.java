/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package at.yawk.cowsay;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author yawkat
 */
public interface CowsayPlatform {
    List<CommandSenderWrapper> getSenders(Pattern namePattern);
}
