#!/usr/bin/env python3

import zipfile
import os
import sys
from PIL import Image
import io

scan_whitespace = False

source_file = sys.argv[1]

with zipfile.ZipFile(source_file) as f:
    # todo: maybe support resource packs?
    with f.open("assets/minecraft/font/glyph_sizes.bin") as g:
        sizes = bytearray(g.read())
    with f.open("assets/minecraft/textures/font/ascii.png") as g:
        img = Image.open(io.BytesIO(g.read()))
    if scan_whitespace:
        unicode_images = []
        for page in range(0x100):
            file_name = "assets/minecraft/textures/font/unicode_page_%02x.png" % page
            try:
                with f.open(file_name) as g:
                    unicode_images.append(Image.open(io.BytesIO(g.read())))
            except KeyError:
                unicode_images.append(None)

def get_char_sizes(img):
    width, height = img.size
    s_width, s_height = width // 16, height // 16
    scale = 8 / s_width

    sizes = [1] * 256
    for i in range(256):
        cx, cy = i % 16, i // 16
        for char_width in range(s_width - 1, -1, -1):
            x = cx * s_width + char_width
            found_pixel = False
            for y in range(s_height):
                pixel = img.getpixel((x, cy * s_height + y))
                if pixel != (0, 0, 0, 0) and pixel != 0:
                    found_pixel = True
                    break
            if found_pixel:
                sizes[i] = round(scale * char_width) + 2
                break
    return sizes

sizes[0:256] = get_char_sizes(img)
sizes[ord(u'§')] = 0
sizes[ord(u' ')] = 4

if scan_whitespace:
    found = [False] * 16
    i = 0
    for page_image in unicode_images:
        if page_image is None:
            i += 0x100
            continue
        real_sizes = get_char_sizes(page_image)
        for size in real_sizes:
            i += 1
            if size == 1:
                actual_size = sizes[i]
                if actual_size < len(found) and not found[actual_size]:
                    print("%04x %2d %s" % (i, actual_size, chr(i)))
                    found[actual_size] = True

print(len(sizes))

with open("plugin/src/main/resources/char_sizes", "w") as f:
    f.buffer.write(sizes)
