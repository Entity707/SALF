package com.game.lwjgl;

import java.nio.ByteBuffer;

class BakedAtlas {
  ByteBuffer pixelData;
  int atlasWidth = 512;
  int atlasHeight = 512;
  float ascent;
  float descent;
  GlyphInfo[] glyphs = new GlyphInfo[96];
}
