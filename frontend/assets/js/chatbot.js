/**
 * AI Welfare Assistant Chatbot Interface
 * UN SDG 1: NO POVERTY Informational Assistant
 */

const Chatbot = {
  messages: [],

  init() {
    this.addBotMessage(
      "Namaste! 🙏 I am your **AI Welfare Assistant**.\n\n" +
      "I can assist you in finding suitable government welfare schemes, checking eligibility rules, document requirements, and navigating to official government portals.\n\n" +
      "How can I assist you today?"
    );
  },

  addUserMessage(text) {
    const chatContainer = document.getElementById('chat-messages');
    if (!chatContainer) return;

    const bubble = document.createElement('div');
    bubble.className = 'chat-bubble user';
    bubble.innerText = text;
    chatContainer.appendChild(bubble);
    chatContainer.scrollTop = chatContainer.scrollHeight;
  },

  addBotMessage(markdownText) {
    const chatContainer = document.getElementById('chat-messages');
    if (!chatContainer) return;

    // Basic markdown replacement for bold and bullets
    const html = markdownText
      .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
      .replace(/\*(.*?)\*/g, '<em>$1</em>')
      .replace(/\n\n/g, '<br><br>')
      .replace(/\n• /g, '<br>• ')
      .replace(/\n(\d+)\. /g, '<br>$1. ');

    const bubble = document.createElement('div');
    bubble.className = 'chat-bubble bot';
    bubble.innerHTML = `
      <div class="d-flex align-items-center gap-1 mb-1 text-primary fw-bold small">
        <i class="fa-solid fa-robot"></i> AI Welfare Guide
      </div>
      <div>${html}</div>
      <div class="mt-2 pt-1 border-top small text-muted" style="font-size: 0.72rem;">
        <i class="fa-solid fa-info-circle me-1"></i> Informational AI response. Always verify details on the official government portal.
      </div>
    `;

    chatContainer.appendChild(bubble);
    chatContainer.scrollTop = chatContainer.scrollHeight;
  },

  async ask(question) {
    if (!question || !question.trim()) return;

    this.addUserMessage(question);

    // Show temporary typing indicator
    const chatContainer = document.getElementById('chat-messages');
    const typingBubble = document.createElement('div');
    typingBubble.className = 'chat-bubble bot text-muted fst-italic';
    typingBubble.id = 'bot-typing';
    typingBubble.innerHTML = '<i class="fa-solid fa-circle-notch fa-spin me-1"></i> Analyzing government welfare criteria...';
    chatContainer.appendChild(typingBubble);
    chatContainer.scrollTop = chatContainer.scrollHeight;

    try {
      const res = await API.post('/ai/ask', { question });
      if (document.getElementById('bot-typing')) {
        document.getElementById('bot-typing').remove();
      }

      const answer = res && res.data && res.data.answer ? res.data.answer : "I couldn't process your question at the moment. Please try asking about student, farmer, or senior citizen schemes.";
      this.addBotMessage(answer);
    } catch (e) {
      if (document.getElementById('bot-typing')) {
        document.getElementById('bot-typing').remove();
      }
      this.addBotMessage("Apologies, I encountered a temporary connection glitch. Please explore schemes via our Scheme Finder.");
    }
  },

  sendQuickQuery(queryText) {
    const input = document.getElementById('chat-input');
    if (input) input.value = queryText;
    this.ask(queryText);
    if (input) input.value = '';
  }
};

