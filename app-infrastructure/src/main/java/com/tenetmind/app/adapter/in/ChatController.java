package com.tenetmind.app.adapter.in;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Controller
public class ChatController {

  // Displays templates/chat.html
  @GetMapping("/chat")
  String getChat() {
    return "chat";
  }

  @RestControllerAdvice(assignableTypes = ChatController.class)
  static final class ControllerAdvice extends AbstractControllerAdvice {}
}
