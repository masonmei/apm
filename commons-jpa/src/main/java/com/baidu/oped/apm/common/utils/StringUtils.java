package com.baidu.oped.apm.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Created by mason on 10/20/15.
 */
public abstract class StringUtils {
    /**
     * Get the section in the original string.
     *
     * @param original  original string
     * @param delimiter delimiter to split original string
     * @param section   the section index
     *
     * @return the section content
     */
    public static String getSection(String original, String delimiter, int section) {
        Assert.hasLength(original, "cannot get section for empty string.");
        String[] split = original.split(delimiter);
        Assert.state(section >= 0 && section < split.length, "invalid section");
        return split[section];
    }

    /**
     * Get the section in the original string split with whitespace character.
     *
     * @param original original string
     * @param section  the section index
     *
     * @return the section content
     */
    public static String getSection(String original, int section) {
        return getSection(original, "\\s+", section);
    }

    /**
     * Get the section in the original string, between fromKey and toKey
     *
     * @param original original string
     * @param fromKey  from key
     * @param toKey    to key
     *
     * @return the section content
     */
    public static String getSection(String original, String fromKey, String toKey) {
        Assert.hasLength(original, "cannot get section for empty string.");
        String originalInLowerCase = original.toLowerCase();
        String fromKeyInLowerCase = fromKey.toLowerCase();
        String toKeyInLowerCase = toKey.toLowerCase();
        int fromIndex = originalInLowerCase.indexOf(fromKeyInLowerCase);
        fromIndex += fromKeyInLowerCase.length();
        if(fromIndex == -1){
            return "";
        }
        int toIndex = originalInLowerCase.indexOf(toKeyInLowerCase, fromIndex);
        if(toIndex == -1){
            toIndex = originalInLowerCase.length();
        }

        return original.substring(fromIndex, toIndex).trim();
    }

    /**
     * Get the sections of each section
     *
     * @param original         original string
     * @param delimiter        delimiter
     * @param sectionDelimiter delimiter in section
     * @param sectionIndex     index in the section
     *
     * @return the sections content
     */
    public static String[] getSections(String original, String delimiter, String sectionDelimiter, int sectionIndex) {
        Assert.hasLength(original, "cannot get sections for empty string.");
        String[] split = original.split(delimiter);

        List<String> result = new ArrayList<>(split.length);
        for (String ori : split) {
            result.add(getSection(ori, sectionDelimiter, sectionIndex));
        }
        return result.toArray(new String[result.size()]);
    }
}
