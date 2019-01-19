package busime.cm.catchussd.util;

import android.support.annotation.Nullable;

public class Strings {
    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.length() == 0;
    }
}
