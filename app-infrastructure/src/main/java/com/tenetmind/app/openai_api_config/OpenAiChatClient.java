package com.tenetmind.app.openai_api_config;

import static com.tenetmind.chat.port.out.GetChatResponsePort.ChatMessage;

import java.util.List;
import lombok.Builder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openAiChatClient", url = "https://api.openai.com/v1/chat")
public interface OpenAiChatClient {

  @PostMapping("/completions")
  Response getCompletion(
      @RequestBody Request requestBody,
      @RequestHeader(value = "Authorization") String authorizationHeader);

  @Builder
  record Request(
      String model, List<? extends ChatMessage> messages, Double temperature, Integer max_tokens) {}

  record Message(String role, String content) implements ChatMessage {}

  record Response(String id, String object, long created, List<Choice> choices, Usage usage) {
    public record Choice(int index, Message message, String finish_reason) {}

    private record Usage(int prompt_tokens, int completion_tokens, int total_tokens) {}
  }
}
