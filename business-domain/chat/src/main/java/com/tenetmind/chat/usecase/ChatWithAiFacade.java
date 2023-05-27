package com.tenetmind.chat.usecase;

import static com.tenetmind.chat.port.out.GetChatResponsePort.ChatMessage;

import com.tenetmind.chat.port.in.ChatWithAiUseCase;
import com.tenetmind.chat.port.out.GetChatResponsePort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  private static final String DEFAULT_MODEL = "gpt-3.5-turbo-0301";
  private static final Map<String, String> modelMap = Map.of(
      "1", "gpt-3.5-turbo-0301",
      "2", "gpt-3.5-turbo"
  );
  private final List<ChatMessage> messages = new ArrayList<>();
  private final GetChatResponsePort getChatResponsePort;
  private String model = DEFAULT_MODEL;
  private double creativity = 0.5;

  @Override
  public String chat(ChatCommand command) {
    var userMessage = buildUserMessage(command.getMessage());
    var modelOpt = command.getModelOpt();
    var creativityOpt = command.getCreativityOpt();

    if (modelOpt.isPresent()) {
      var modelId = modelOpt.get();
      if (modelMap.containsKey(modelId)) {
        model = modelMap.get(modelId);
      }
    }

    creativityOpt.ifPresent(value -> creativity = Double.parseDouble(value));

    messages.add(userMessage);
    ChatMessage assistantMessage;

    try {
      assistantMessage = getChatResponsePort.getResponse(model, messages, creativity);
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

  private record Message(String role, String content) implements GetChatResponsePort.ChatMessage {
  }
}
