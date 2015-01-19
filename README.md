Cowsay
======

Cowsay plugin for bukkit and bungee.

Usage
-----

`/cowsay <message>`

`/cowthink <message>`

Both commands accept arguments:

- `-p <regex>` sends the message to all players matching that regex.
- `-i` sets the regex to ignore-case.
- `-l` sets the regex to literal mode.
- `-c <color>` changes the color of the cow and the bubble. (for example `-c §5`)
- `-s <needle>` replaces the given needle with `§` (the minecraft color character) in the text and in the bubble color arg.
- `--` to stop parsing parameters and treat everything following as text

If you end the parameter list with an argumentless parameter like `-i` or `-l` you either have to add an argument that will subsequently ignored or you have to use `--`.

Screenshots
-----------

![Console](http://s.yawk.at/v4s9.png)

![In-Game](http://s.yawk.at/Pc9n.png)

Permissions
-----------

- `cowsay.cowsay`: Give access to `/cowsay`
- `cowsay.cowthink`: Give access to `/cowthink`
- `cowsay.color.cow`: Allow players to use `-c`
- `cowsay.color.text`: Allow players to use `-s`
- `cowsay.other`: Allow players to use `-p`
