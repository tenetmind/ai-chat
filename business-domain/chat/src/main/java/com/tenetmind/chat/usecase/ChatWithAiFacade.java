package com.tenetmind.chat.usecase;

import static com.tenetmind.chat.port.out.GetChatResponsePort.ChatMessage;

import com.tenetmind.chat.port.in.ChatWithAiUseCase;
import com.tenetmind.chat.port.out.GetChatResponsePort;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * © Weborama 2023. All rights reserved.
 *
 * @author arozycki on 20/05/2023.
 */
@RequiredArgsConstructor
class ChatWithAiFacade implements ChatWithAiUseCase {

  private static final String SYSTEM_ROLE = "system";
  private static final String USER_ROLE = "user";
  private static final String LLM_MODEL = "gpt-3.5-turbo-0301";
  private final List<ChatMessage> messages = new ArrayList<>();
  private final GetChatResponsePort getChatResponsePort;

  @Override
  public String chat(ChatCommand command) {
    var userMessage = buildUserMessage(command.getMessage());
    messages.add(userMessage);
    ChatMessage assistantMessage;

    try {
      assistantMessage = getChatResponsePort.getResponse(LLM_MODEL, messages);
    } catch (Exception e) {
      if (e.getMessage().contains("429")) {
        throw new TooManyRequestsException(e.getMessage());
      }

      throw e;
    }

    messages.add(assistantMessage);
    return assistantMessage.content();
  }

  private Message buildUserMessage(String content) {
    return new Message(USER_ROLE, content);
  }

  private record Message(String role, String content) implements GetChatResponsePort.ChatMessage {}
}
