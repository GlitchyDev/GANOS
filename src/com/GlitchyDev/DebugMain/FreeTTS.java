package com.GlitchyDev.DebugMain;

import com.GlitchyDev.Utility.TTSVoiceManager;

public class FreeTTS {
    public static void main(String[] args) {
        TTSVoiceManager voiceManager = new TTSVoiceManager();

        String message1 = "I see some of you saying that this has been proven to be false. For one, please understand that I did the research for it this morning. Second, I'm not seeing the proof, so if someone could provide me with a link from an Intel rep or reputable source, saying this isn't right, I'll either change the name or just take the video down. I definitely never intend to spread things that are knowingly false. Reply to this comment or message me on the Gamer Meld Discord (sometimes YouTube will flag comments with links). ";
        voiceManager.voiceSpeakAsynchronous("mbrola_us1",message1);
        voiceManager.voiceSpeakAsynchronous("mbrola_us2",message1);
        voiceManager.voiceSpeakAsynchronous("mbrola_us3",message1);



    }
}
