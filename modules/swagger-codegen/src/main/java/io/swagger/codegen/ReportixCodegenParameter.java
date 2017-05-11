package io.swagger.codegen;

import io.swagger.codegen.utils.ReportixUtils;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportixCodegenParameter extends CodegenParameter {

    public enum Kind
    {
        NORMAL,
        HARDCODED
    }

    public void computeEffectiveValues(DefaultCodegen codegen, Parameter parameter) {
        String overriddenName = ReportixUtils.getStringValue(vendorExtensions, ReportixUtils.OVERRIDE_NAME);
        if (overriddenName !=null)
          paramName = codegen.toParamName((String)overriddenName);
        else
          paramName = codegen.toParamName(parameter.getName());

        String overriddenDescription = ReportixUtils.getStringValue(vendorExtensions, ReportixUtils.OVERRIDE_DESCRIPTION);
        if (overriddenDescription !=null)
            description = codegen.escapeText((String)overriddenDescription);
        else
            description = codegen.escapeText(parameter.getDescription());

        String overriddenDefaultValue = ReportixUtils.getAtomicValue(vendorExtensions, ReportixUtils.HARDCODED_VALUE);
        if (overriddenDefaultValue !=null)
            defaultValue = overriddenDefaultValue;
    }

    public Kind getParameterKind()
    {
        String hardcodedValue = ReportixUtils.getAtomicValue(vendorExtensions, ReportixUtils.HARDCODED_VALUE);
        if (hardcodedValue != null)
            return Kind.HARDCODED;
        return Kind.NORMAL;
    }

    @Override
    public ReportixCodegenParameter copy() {
        ReportixCodegenParameter output = new ReportixCodegenParameter();
        copyTo(output);
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportixCodegenParameter that = (ReportixCodegenParameter) o;
        if (!equalFields(that))
            return false;

        return true;
    }
}
