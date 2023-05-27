package com.tenetmind.app.adapter.out;

import com.tenetmind.app.openai_api_config.AuthorizationHeaderProvider;
import com.tenetmind.app.openai_api_config.OpenAiChatClient;
import com.tenetmind.chat.port.out.GetChatResponsePort;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GetChatResponseAdapter implements GetChatResponsePort {

  private final OpenAiChatClient openAiChatClient;
  private final AuthorizationHeaderProvider authorizationHeaderProvider;

  @Override
  public ChatMessage getResponse(String model, List<? extends ChatMessage> chatMessages,
      double temperature) {
    var request = buildRequest(model, chatMessages, temperature);
    var header = authorizationHeaderProvider.getHeader();
    log.info("Sending request: {} with header: {}", request, header);
    var response = openAiChatClient.getCompletion(request, header);
    log.info("Received response: {}", response);
    return extractMessage(response);
  }

  OpenAiChatClient.Request buildRequest(String model, List<? extends ChatMessage> chatMessages,
      double temperature) {
    return OpenAiChatClient.Request.builder()
        .model(model)
        .messages(chatMessages)
        .temperature(temperature)
        .max_tokens(GetChatResponsePort.MAX_TOKENS)
        .build();
  }

  ChatMessage extractMessage(OpenAiChatClient.Response response) {
    return response.choices().get(0).message();
  }
}
