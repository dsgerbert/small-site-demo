package ru.h562.smallsite;

public class SmallSiteUtils {

    /**
     * Restrict string length and replace empty strings
     * @param str - initial string
     * @param len - restrict length
     * @return
     */
    public static String restrictcStr(String str, int len) {
        if (str.isEmpty()) {
            str = SmallSiteConst.VALUE_NULL;
        } else if (str.length() > len) {
            str = str.substring(0, len);
        }

        return str;
    }
}
