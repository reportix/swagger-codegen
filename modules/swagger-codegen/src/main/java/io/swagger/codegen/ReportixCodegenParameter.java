package io.swagger.codegen;

import io.swagger.codegen.utils.ReportixUtils;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportixCodegenParameter extends CodegenParameter {

    public void computeEffectiveParameterName(DefaultCodegen codegen, Parameter parameter) {
        String overriddenName = ReportixUtils.getStringValue(vendorExtensions, ReportixUtils.OVERRIDE_NAME);
        if (overriddenName !=null)
          paramName = codegen.toParamName((String)overriddenName);
        else
          paramName = codegen.toParamName(parameter.getName());
    }

    public void computeEffectiveParameterDescription(DefaultCodegen codegen, Parameter parameter) {
        String overriddenDescription = ReportixUtils.getStringValue(vendorExtensions, ReportixUtils.OVERRIDE_DESCRIPTION);
        if (overriddenDescription !=null)
            description = codegen.escapeText((String)overriddenDescription);
        else
            description = codegen.escapeText(parameter.getDescription());
    }
}
