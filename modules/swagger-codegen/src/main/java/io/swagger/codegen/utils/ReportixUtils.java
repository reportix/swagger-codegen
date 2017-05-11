package io.swagger.codegen.utils;

import java.util.Map;

public class ReportixUtils
{
    private static final String EXCLUDE = "x-exclude";

    public static boolean getBooleanValue(Map<String, Object> vendorExtensions, String parameter, boolean defaultValue)
    {
        Object value = vendorExtensions.get(parameter);
        if (value == null)
            return defaultValue;
        if (value instanceof Boolean)
            return ((Boolean)value).booleanValue();
        else
        {
            String msg = "Invalid value for " + parameter + ", only booleans are allowed";
            throw new RuntimeException(msg);
        }
    }

    public static String getStringValue(Map<String, Object> vendorExtensions, String parameter)
    {
        Object value = vendorExtensions.get(parameter);
        if (value == null)
            return null;
        if (value instanceof String)
            return (String)value;
        else
        {
            String msg = "Invalid value for " + parameter + ", only strings are allowed";
            throw new RuntimeException(msg);
        }
    }

    public static boolean isExcluded(Map<String, Object> vendorExtensions)
    {
        return getBooleanValue(vendorExtensions, EXCLUDE, false);
    }
}
