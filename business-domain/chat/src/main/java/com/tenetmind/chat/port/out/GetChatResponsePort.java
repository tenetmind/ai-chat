package com.tenetmind.chat.port.out;

import java.util.List;

public interface GetChatResponsePort {

  Integer MAX_TOKENS = 2048;
  Double TEMPERATURE = 0.;

  ChatMessage getResponse(String model, List<? extends ChatMessage> chatMessages);

  interface ChatMessage {
    String role();

    String content();
  }
}
