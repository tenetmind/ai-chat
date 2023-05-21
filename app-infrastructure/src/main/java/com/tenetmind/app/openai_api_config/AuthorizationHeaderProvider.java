package com.tenetmind.app.openai_api_config;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationHeaderProvider {

  private final String apiKey;

  public String getHeader() {
    return "Bearer " + apiKey;
  }
}
