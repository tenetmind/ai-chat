package com.tenetmind.app.adapter.in;

import static com.tenetmind.app.adapter.in.MessageController.ChatRequest.toChatCommand;
import static com.tenetmind.chat.port.in.ChatWithAiUseCase.ChatCommand;
import static com.tenetmind.chat.port.in.ChatWithAiUseCase.TooManyRequestsException;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tenetmind.chat.port.in.ChatWithAiUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

  private final ChatWithAiUseCase chatWithAiUseCase;

  @PostMapping(
      value = "/chat/message",
      consumes = APPLICATION_JSON_VALUE + ";charset=UTF-8",
      produces = APPLICATION_JSON_VALUE + ";charset=UTF-8")
  @ResponseStatus(OK)
  ChatResponse chatWithAi(@RequestBody ChatRequest request) {
    log.info("Received request: {}", request);
    var command = toChatCommand(request);
    var response = chatWithAiUseCase.chat(command);
    log.info("Sending response message: {}", response);
    return new ChatResponse(response);
  }

  record ChatRequest(String message, String model, String creativity) {
    static ChatCommand toChatCommand(ChatRequest request) {
      return ChatCommand.builder()
          .message(request.message)
          .modelOpt(request.model)
          .creativityOpt(request.creativity)
          .build();
    }
  }

  private record ChatResponse(String message) {}

  @RestControllerAdvice(assignableTypes = MessageController.class)
  static final class ControllerAdvice extends AbstractControllerAdvice {

    @ExceptionHandler(ChatWithAiUseCase.ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorResponse handleBadRequest(Exception e) {
      logWebException(e, BAD_REQUEST);
      return new ErrorResponse(e);
    }

    @ExceptionHandler(TooManyRequestsException.class)
    @ResponseStatus(TOO_MANY_REQUESTS)
    ErrorResponse handleTenantNotFound(Exception e) {
      logWebException(e, TOO_MANY_REQUESTS);
      return new ErrorResponse(e);
    }
  }
}
