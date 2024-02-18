package main;

import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageId;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot{
  // Keyboard can be a separate class
  private final InlineKeyboardButton nextInlineButton = InlineKeyboardButton.builder().text("Next").callbackData("next").build();
  private final InlineKeyboardButton backInlineButton = InlineKeyboardButton.builder().text("Back").callbackData("back").build();
  private final InlineKeyboardButton urlInlineButton = InlineKeyboardButton.builder().text("Tutorial").url("https://core.telegram.org/bots/tutorial#introduction").build();
  
  private final InlineKeyboardMarkup keyboard1 = InlineKeyboardMarkup.builder().keyboardRow(List.of(nextInlineButton)).build();
  private final InlineKeyboardMarkup keyboard2 = InlineKeyboardMarkup.builder().keyboardRow(List.of(backInlineButton, nextInlineButton))
          .keyboardRow(List.of(urlInlineButton)).build();
  
  
  @Override
  public String getBotUsername() {
    return "jiufong's bot";
  }
  
  @Override
  public String getBotToken() {
    return "6843693498:AAEsT-6R8Melycb6_A6B6MBMEhJu59pdIYA";
  }
  
  @Override
  public void onUpdateReceived(Update update) {      // will be called automatically when a private message is received
    Message msg = update.getMessage();
    User user = msg.getFrom();
    
    String chatId = user.getId().toString();
    String msgText = msg.getText();
    
    if (msg.isCommand()) {   // checks if a message is a command
      executeCommand(msg);
    } else {
      sendText(chatId, msgText);
      copyMessage(chatId, msg.getMessageId());
      sendMenu(chatId, "<b> MENU </b>", keyboard2);
    }
  }
  
  public void sendText(String chatId, String msgText) {
    SendMessage sm = SendMessage.builder().chatId(chatId).text(msgText).build();
    
    try {
      this.<Message, SendMessage>execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Echoes the message received by the user.
   */
  public void copyMessage(String chatId, Integer msgId) {
    CopyMessage cm = CopyMessage.builder().fromChatId(chatId).chatId(chatId).messageId(msgId).build();
    
    try {
      this.<MessageId, CopyMessage>execute(cm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void sendMenu(String chatId, String msgText, InlineKeyboardMarkup kb) {
    SendMessage sm = SendMessage.builder().chatId(chatId)
            .parseMode("HTML").text(msgText).replyMarkup(kb).build();
    
    System.out.print("here");
    try {
      this.execute(sm);
      System.out.print(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
  
  private void start() {
    System.out.println("starting...");
  }
  
  private void end() {
    System.out.println("ending...");
  }
  
  public void executeCommand(Message msg) {
    String msgText = msg.getText();
    switch (msgText) {
    case "/start" -> start();
    case "/end" -> end();
    default -> System.out.println("nothing has been done");
    }
  }
}