package com.tenetmind.chat.port.out;

import java.util.List;

public interface GetChatResponsePort {

  Integer MAX_TOKENS = 2048;

  ChatMessage getResponse(String model, List<? extends ChatMessage> chatMessages, double creativity);

  interface ChatMessage {
    String role();

    String content();
  }
}
