package com.ifoods.common.util;

import com.ifoods.common.model.ConsoleContext;

/**
 * To change this template use File | Settings | File Templates.
 */
public class ThreadLocalManager {
    private static final ThreadLocal<ConsoleContext> manager = new ThreadLocal<ConsoleContext>();

    public static void setConsoleContext(ConsoleContext consoleContext) {
        ConsoleContext context = getConsoleContext();
        if (context != null) {
            manager.remove();
        }
        manager.set(consoleContext);
    }

    public static ConsoleContext getConsoleContext() {
        return manager.get();
    }

    public static void clear() {
        manager.remove();
    }
}
