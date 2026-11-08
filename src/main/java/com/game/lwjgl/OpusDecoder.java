package com.game.lwjgl;

import java.nio.ShortBuffer;

public class OpusDecoder {
  private OpusDecoder() {}
  
  public static ShortBuffer createBGMPCMData(AudioData audioData) {
//    try (MemoryStack stack = MemoryStack.stackPush()) {
//      IntBuffer error = stack.mallocInt(1);
//      audioData.bgmFilePointer = OpusFile.op_open_memory(audioData.bgmFileBuffer, error);
//
//      if (audioData.bgmFilePointer == 0) {
//        LoggerUtil.error("failed to open opus file. error: " + error.get(0));
//      }
//
//      audioData.channels = OpusFile.op_head(audioData.bgmFilePointer, -1).channel_count();
//    }
//
//    try {
//      long totalSamples = OpusFile.op_pcm_total(audioData.bgmFilePointer, -1);
//
//      ShortBuffer pcmData = BufferUtils.createShortBuffer((int) totalSamples * audioData.channels);
//      ShortBuffer readBuffer = BufferUtils.createShortBuffer(4096 * audioData.channels);
//
//      int samplesRead;
//      while ((samplesRead = OpusFile.op_read(audioData.bgmFilePointer, readBuffer, null)) > 0) {
//        readBuffer.limit(samplesRead * audioData.channels);
//        pcmData.put(readBuffer);
//        readBuffer.clear();
//      }
//
//      pcmData.flip();
//      return pcmData;
//
//    } finally {
//      //prevent memory leak if something fails
//      OpusFile.op_free(audioData.bgmFilePointer);
//    }
    return null;
  }
}
