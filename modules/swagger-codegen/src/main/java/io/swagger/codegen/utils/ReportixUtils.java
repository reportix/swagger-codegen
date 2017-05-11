package io.swagger.codegen.utils;

import java.util.Map;

public class ReportixUtils
{
    private static final String EXCLUDE = "x-exclude";
    public static final String OVERRIDE_NAME = "x-name";
    public static final String OVERRIDE_DESCRIPTION = "x-description";
    public static final String HARDCODED_VALUE = "x-hardcoded-value";

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

    public static String getAtomicValue(Map<String, Object> vendorExtensions, String parameter)
    {
        Object value = vendorExtensions.get(parameter);
        if (value == null)
            return null;
        if (value instanceof String)
            return (String)value;
        else
            return value.toString();
    }

    public static boolean isExcluded(Map<String, Object> vendorExtensions)
    {
        return getBooleanValue(vendorExtensions, EXCLUDE, false);
    }
}
