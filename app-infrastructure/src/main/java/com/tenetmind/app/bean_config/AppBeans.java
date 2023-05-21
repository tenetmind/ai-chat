package com.tenetmind.app.bean_config;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

import com.tenetmind.app.adapter.out.GetChatResponseAdapter;
import com.tenetmind.app.openai_api_config.AuthorizationHeaderProvider;
import com.tenetmind.app.openai_api_config.OpenAiChatClient;
import com.tenetmind.chat.port.in.ChatWithAiUseCase;
import com.tenetmind.chat.port.out.GetChatResponsePort;
import com.tenetmind.chat.usecase.ChatWithAiUseCaseProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/** This class contains all beans configuration except of controller beans. */
@Slf4j
@Configuration
@EnableFeignClients(clients = OpenAiChatClient.class)
public class AppBeans {

  @Value("${openai.api-key}")
  private String apiKey;

  @Bean("openAiApiKey")
  public String openAiApiKey() {
    log.info("Creating openAiApiKey");
    return apiKey;
  }

  @Bean
  public AuthorizationHeaderProvider authorizationHeaderProvider(
      @Qualifier("openAiApiKey") String apiKey) {
    log.info("Creating AuthorizationHeaderProvider");
    return new AuthorizationHeaderProvider(apiKey);
  }

  @Bean
  public GetChatResponsePort getChatResponsePort(
      OpenAiChatClient openAiChatClient, AuthorizationHeaderProvider authorizationHeaderProvider) {
    log.info("Creating GetChatResponsePort");
    return new GetChatResponseAdapter(openAiChatClient, authorizationHeaderProvider);
  }

  @Bean
  @Scope(value = SCOPE_SESSION, proxyMode = TARGET_CLASS)
  public ChatWithAiUseCase chatWithAiUseCase(GetChatResponsePort getChatResponsePort) {
    log.info("Creating ChatWithAiUseCase");
    return ChatWithAiUseCaseProvider.getUseCase(getChatResponsePort);
  }
}
