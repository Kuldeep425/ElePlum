package com.example.eleplum.Models;

public class Friends {
  private String name;
  private String friendId;
  private String lastMessage;
  private long timeStamp;

  public Friends(String name, String friendId, String lastMessage, long timeStamp) {
    this.name = name;
    this.friendId = friendId;
    this.lastMessage = lastMessage;
    this.timeStamp = timeStamp;
  }

  public Friends() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFriendId() {
    return friendId;
  }

  public void setFriendId(String friendId) {
    this.friendId = friendId;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(String lastMessage) {
    this.lastMessage = lastMessage;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }
}
