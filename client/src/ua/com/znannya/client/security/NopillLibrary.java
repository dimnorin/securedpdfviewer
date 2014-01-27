package ua.com.znannya.client.security;

import com.sun.jna.Library;

public interface NopillLibrary extends Library {
	boolean isVirtualEnv();
}
