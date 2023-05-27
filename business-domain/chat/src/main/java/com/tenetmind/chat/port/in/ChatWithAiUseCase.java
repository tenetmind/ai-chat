package com.tenetmind.chat.port.in;

import static com.google.common.base.Strings.isNullOrEmpty;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;

public interface ChatWithAiUseCase {

  /** Accepts a command with a message from the user and returns a response from the AI chatbot */
  String chat(ChatCommand command);

  @Value
  class ChatCommand {
    String message;
    Optional<String> modelOpt;
    Optional<String> creativityOpt;

    @Builder
    private ChatCommand(String message, String modelOpt, String creativityOpt) {
      if (isNullOrEmpty(message)) {
        throw new ValidationException("Chat command message cannot be null or empty");
      }

      this.message = message;
      this.modelOpt = Optional.ofNullable(modelOpt);
      this.creativityOpt = Optional.ofNullable(creativityOpt);
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
