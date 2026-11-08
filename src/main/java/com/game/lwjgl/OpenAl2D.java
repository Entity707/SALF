package com.game.lwjgl;

public class OpenAl2D {
  public static void updateStream(AudioData audioData) {
    
//    int processed = AL11.alGetSourcei(audioData.source, AL11.AL_BUFFERS_PROCESSED);
//
//    while (processed > 0) {
//      int unqueuedBuffer = AL11.alSourceUnqueueBuffers(audioData.source);
//
//      audioData.bgmFileBuffer.clear();
//      int samplesRead = OpusFile.op_read(audioData.bgmFilePointer, audioData.bgmFileBuffer, null);
//
//      if (samplesRead > 0) {
//        audioData.bgmFileBuffer.limit(samplesRead * audioData.channels);
//        AL11.alBufferData(unqueuedBuffer, audioData.format, audioData.bgmFileBuffer, audioData.sampleRate);
//        AL11.alSourceQueueBuffers(audioData.source, unqueuedBuffer);
//      } else {
//
//      }
//
//      if (AL11.alGetSourcei(audioData.source, AL11.AL_SOURCE_STATE) != AL11.AL_PLAYING) {
//        AL11.alSourcePlay(audioData.source);
//      }
//
//      processed--;
//    }
  }
}
