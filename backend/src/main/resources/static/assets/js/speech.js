/**
 * Speech Capabilities: Text-to-Speech (TTS) & Web Speech API Voice Recognition
 * UN SDG 1 - NO POVERTY Welfare Assistance Platform
 */

const SpeechManager = {
  synth: window.speechSynthesis,
  currentUtterance: null,
  recognition: null,
  isRecognizing: false,

  // 1. TEXT TO SPEECH (TTS) - Read Aloud
  readAloud(text, lang = 'en-IN') {
    if (!this.synth) {
      App.showToast('Text-to-speech is not supported on this browser.', 'error');
      return;
    }

    this.stopTTS();

    const cleanText = text.replace(/<[^>]*>?/gm, ''); // strip HTML tags
    this.currentUtterance = new SpeechSynthesisUtterance(cleanText);

    // Map language
    const currentAppLang = App.currentLang || 'en';
    if (currentAppLang === 'hi') this.currentUtterance.lang = 'hi-IN';
    else if (currentAppLang === 'kn') this.currentUtterance.lang = 'kn-IN';
    else if (currentAppLang === 'mr') this.currentUtterance.lang = 'mr-IN';
    else this.currentUtterance.lang = 'en-IN';

    this.currentUtterance.rate = 0.95;
    this.currentUtterance.pitch = 1.0;

    this.currentUtterance.onend = () => {
      this.updateTTSControls('stopped');
    };

    this.currentUtterance.onerror = (e) => {
      console.warn('SpeechSynthesis error:', e);
      this.updateTTSControls('stopped');
    };

    this.synth.speak(this.currentUtterance);
    this.updateTTSControls('playing');
    App.showToast('Playing audio reading...', 'info');
  },

  pauseTTS() {
    if (this.synth && this.synth.speaking) {
      this.synth.pause();
      this.updateTTSControls('paused');
    }
  },

  resumeTTS() {
    if (this.synth && this.synth.paused) {
      this.synth.resume();
      this.updateTTSControls('playing');
    }
  },

  stopTTS() {
    if (this.synth) {
      this.synth.cancel();
      this.updateTTSControls('stopped');
    }
  },

  updateTTSControls(state) {
    const playBtn = document.getElementById('tts-play-btn');
    const pauseBtn = document.getElementById('tts-pause-btn');
    const stopBtn = document.getElementById('tts-stop-btn');

    if (!playBtn) return;

    if (state === 'playing') {
      playBtn.classList.add('d-none');
      if (pauseBtn) pauseBtn.classList.remove('d-none');
      if (stopBtn) stopBtn.classList.remove('d-none');
    } else if (state === 'paused') {
      if (pauseBtn) pauseBtn.innerHTML = '<i class="fa-solid fa-play"></i> Resume';
    } else {
      playBtn.classList.remove('d-none');
      if (pauseBtn) pauseBtn.classList.add('d-none');
      if (stopBtn) stopBtn.classList.add('d-none');
      if (pauseBtn) pauseBtn.innerHTML = '<i class="fa-solid fa-pause"></i> Pause';
    }
  },

  // 2. VOICE SEARCH (Speech-to-Text)
  initVoiceRecognition(onResultCallback, onStateChangeCallback) {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

    if (!SpeechRecognition) {
      return {
        supported: false,
        message: "Voice search is not supported in this browser. Please use keyboard text search."
      };
    }

    this.recognition = new SpeechRecognition();
    this.recognition.continuous = false;
    this.recognition.interimResults = false;

    // Detect language
    const currentAppLang = App.currentLang || 'en';
    if (currentAppLang === 'hi') this.recognition.lang = 'hi-IN';
    else if (currentAppLang === 'kn') this.recognition.lang = 'kn-IN';
    else if (currentAppLang === 'mr') this.recognition.lang = 'mr-IN';
    else this.recognition.lang = 'en-IN';

    this.recognition.onstart = () => {
      this.isRecognizing = true;
      if (onStateChangeCallback) onStateChangeCallback('listening');
    };

    this.recognition.onresult = (event) => {
      const transcript = event.results[0][0].transcript;
      if (onResultCallback) onResultCallback(transcript);
    };

    this.recognition.onerror = (event) => {
      console.warn('Speech recognition error:', event.error);
      this.isRecognizing = false;
      if (onStateChangeCallback) onStateChangeCallback('error', event.error);
    };

    this.recognition.onend = () => {
      this.isRecognizing = false;
      if (onStateChangeCallback) onStateChangeCallback('ended');
    };

    return {
      supported: true,
      start: () => {
        try {
          this.recognition.start();
        } catch (e) {
          console.warn(e);
        }
      },
      stop: () => {
        try {
          this.recognition.stop();
        } catch (e) {
          console.warn(e);
        }
      }
    };
  }
};
