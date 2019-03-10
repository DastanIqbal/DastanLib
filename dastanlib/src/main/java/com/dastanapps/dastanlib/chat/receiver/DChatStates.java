package com.dastanapps.dastanlib.chat.receiver;

/**
 * Created by DastanIqbal on 20/10/16.
 */

public class DChatStates {
    public static ConnectionState sConnectionState;
    public static LoggedInState sLoggedInState;
    public static MessageState sMessageState;

    public static ConnectionState getsConnectionState() {
        if (sConnectionState == null) {
            return ConnectionState.DISCONNECTED;
        }
        return sConnectionState;
    }

    public static LoggedInState getLoggedInState() {
        if (sLoggedInState == null) {
            return LoggedInState.LOGGED_OUT;
        }
        return sLoggedInState;
    }

    public static MessageState getMessageState() {
        if (sMessageState == null) {
            return MessageState.NONE;
        }
        return sMessageState;
    }

    public enum ConnectionState {CONNECTED, CONNECTING, DISCONNECTING, DISCONNECTED, ERROR, AUTHENTICATED;}

    public enum LoggedInState {LOGGED_IN, LOGGED_OUT;}

    public enum MessageState {SEND, RECEIVE, NONE;}
}
