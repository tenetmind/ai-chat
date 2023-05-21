package com.tenetmind.chat.port.in;

import static com.google.common.base.Strings.isNullOrEmpty;
import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;
import lombok.Value;

public interface ChatWithAiUseCase {

  /** Accepts a command with a message from the user and returns a response from the AI chatbot */
  String chat(ChatCommand command);

  @Value
  @RequiredArgsConstructor(access = PRIVATE)
  class ChatCommand {
    String message;

    public static ChatCommand of(String message) {

      if (isNullOrEmpty(message)) {
        throw new ValidationException("Chat command message cannot be null or empty");
      }

      return new ChatCommand(message);
    }
  }

  final class ValidationException extends RuntimeException {
    public ValidationException(String message) {
      super(message);
    }
  }

  final class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
      super(message);
    }
  }
}
