package com.group10.cse5236project;

public class infoClass {
    private String currentUserName;
    private String currentChatRoomName;
    private String currentChatRoomKey;
    public String getCurrentUserName() {return currentUserName;}

    public void setCurrentUserName(String currentUserName) {this.currentUserName = currentUserName;}

    public String getCurrentChatRoomName() {return currentChatRoomName;}

    public void setCurrentChatRoomName(String currentChatRoomName) {this.currentChatRoomName = currentChatRoomName;}

    public String getCurrentChatRoomKey() {return currentChatRoomKey;}
    public void setCurrentChatRoomKey(String currentChatRoomKey) {this.currentChatRoomKey = currentChatRoomKey;}

    private static final infoClass holder = new infoClass();
    public static infoClass getInstance() {return holder;}
}
