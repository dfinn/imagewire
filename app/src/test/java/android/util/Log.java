package android.util;

// Workaround for a bug in the paging-testing library that prevented unit testing:
// https://issuetracker.google.com/issues/331684448
public class Log {
    public static boolean isLoggable(String tag, int level) {
        return true;
    }

    public static int v(String tag, String msg) {
        return 0;
    }

    public static int v(String tag, String msg, Throwable error) {
        return 0;
    }

    public static int d(String tag, String msg) {
        return 0;
    }

    public static int d(String tag, String msg, Throwable error) {
        return 0;
    }

    public static int i(String tag, String msg) {
        return 0;
    }

    public static int w(String tag, String msg) {
        return 0;
    }

    public static int e(String tag, String msg) {
        return 0;
    }

    private static void print(String type, String tag, String msg) {
    }
}
