package ua.com.znannya.client.security;

import com.sun.jna.Library;

public interface FsxxLibrary extends Library {
    boolean StartstoP(com.sun.jna.platform.win32.W32API.HWND hW, boolean isStart);
}
