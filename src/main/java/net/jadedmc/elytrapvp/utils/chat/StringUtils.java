package net.jadedmc.elytrapvp.utils.chat;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static String join(List<String> args, String separator) {
        StringBuilder temp = new StringBuilder();

        for(String str : args) {
            if(!temp.toString().equals("")) {
                temp.append(separator);
            }

            temp.append(str);
        }

        return temp.toString();
    }
    public static String join(String[] args, String separator) {
        return join(Arrays.asList(args), separator);
    }
}
