package com.GlitchyDev.Utility;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.util.HashMap;

public class TTSVoiceManager {
    // Remember, each voice can only be speaking in once place at one time!
    private HashMap<String,Voice> loadedVoices = new HashMap<>();



    public TTSVoiceManager() {
        System.setProperty("mbrola.base", "GameAssets/MBROLA_Voices");
        loadAllVoices();
    }


    private void loadAllVoices() {
        System.out.println();
        System.out.println("TTSVoiceManager: Loading all TTS voices");
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice[] voices = voiceManager.getVoices();
        for(int i = 0; i < voices.length; ++i) {
            System.out.println("- " + voices[i].getName() + " (" + voices[i].getDomain() + " domain)");
            loadedVoices.put(voices[i].getName(),voices[i]);
            voices[i].allocate();
        }
    }

    public void voiceSpeakAsynchronous(String voice, String message) {
        new Thread(new VoiceTalkRunnable(loadedVoices.get(voice),message)).start();
    }

    public void voiceSpeakSynchronous(String voice, String message) {
        loadedVoices.get(voice).speak(message);
    }

    public void cleanup() {
        for(String voice: loadedVoices.keySet()) {
            loadedVoices.get(voice).deallocate();
        }
    }

    private class VoiceTalkRunnable implements Runnable {
        private final Voice voice;
        private final String message;

        public VoiceTalkRunnable(Voice voice, String message) {
            this.voice = voice;
            this.message = message;
        }

        @Override
        public void run() {
            voice.speak(message);
        }
    }
}
