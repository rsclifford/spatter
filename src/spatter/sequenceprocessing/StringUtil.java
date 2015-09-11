/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spatter.sequenceprocessing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author faroq
 */

/*
 * Copyright (C) 2011-2 Faroq AL-Tam and Hamid Reza Shahbazkia, 2015 Sean Clifford
 * This program is part of DNACLI.
 * 
 * If you want to improve the software, this is free software for non-commercial purposes.
 */

public class StringUtil {
    public static String getNumberonly(String string) {
        String number = "";
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(string);

        if (m.find() == true)
            number = m.group();

        return number;

    }

    public static String getoutliers(String string, String regex) {
        String outlies = "";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        if (m.find() == true)
            outlies = m.group();
        return outlies;
    }

    public static String getAsHexaFormated(String string) {
        Pattern p = Pattern.compile("[-\\d\\d]+");
        Matcher m = p.matcher(string);
        if (m.find() == true)
            return m.group();

        return null;
    }

    public static String sliceAndMatch(String fstring, String rstring) {
        String resultString = "";
        for (int i = 0; i < fstring.length(); i++) {
            String subString = fstring.substring(i, fstring.length());

            if (rstring.startsWith(subString)) {
                resultString += subString
                        + rstring.substring(subString.length());
                break;
            }
            resultString += fstring.charAt(i);
        }
        return resultString;
    }

}
