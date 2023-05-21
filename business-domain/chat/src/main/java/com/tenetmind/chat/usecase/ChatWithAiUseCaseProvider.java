package com.tenetmind.chat.usecase;

import static lombok.AccessLevel.PRIVATE;

import com.tenetmind.chat.port.in.ChatWithAiUseCase;
import com.tenetmind.chat.port.out.GetChatResponsePort;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatWithAiUseCaseProvider {

  public static ChatWithAiUseCase getUseCase(GetChatResponsePort getChatResponsePort) {
    return new ChatWithAiFacade(getChatResponsePort);
  }
}
