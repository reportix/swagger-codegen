package io.swagger.codegen;

import io.swagger.codegen.utils.ReportixUtils;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.SerializableParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportixCodegenParameter extends CodegenParameter {

    public Kind kind;
    public String namePattern;

    public enum Kind
    {
        NORMAL,
        PATTERN,
        HARDCODED
    }

    public void init(DefaultCodegen codegen, Parameter parameter) {
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

        namePattern = ReportixUtils.getAtomicValue(vendorExtensions, ReportixUtils.NAME_PATTERN);
        String hardcodedValue = ReportixUtils.getAtomicValue(vendorExtensions, ReportixUtils.HARDCODED_VALUE);

        if (namePattern != null && hardcodedValue != null)
            throw new RuntimeException(ReportixUtils.NAME_PATTERN + " and " + ReportixUtils.HARDCODED_VALUE + " are not allowed on the same parameter");

        if (namePattern == null && hardcodedValue == null)
            kind = Kind.NORMAL;
        else if (namePattern != null)
            kind = Kind.PATTERN;
        else
            kind = Kind.HARDCODED;

        if (kind == Kind.PATTERN ||
            kind == Kind.HARDCODED)
        {
            SerializableParameter qp = (SerializableParameter) parameter;
            String type = qp.getType();
            Map<PropertyBuilder.PropertyId, Object> args = new HashMap<PropertyBuilder.PropertyId, Object>();
            String format = qp.getFormat();
            args.put(PropertyBuilder.PropertyId.ENUM, qp.getEnum());

            Property inner = PropertyBuilder.build(type, format, args);
            if (inner instanceof ArrayProperty)
                ((ArrayProperty)inner).setItems(qp.getItems());

            CodegenProperty pr = codegen.fromProperty("inner", inner);
            baseType = pr.datatype;
            isContainer = true;
            //imports.add(pr.baseType);

            Property property = new MapProperty(inner);
            CodegenProperty model = codegen.fromProperty(qp.getName(), property);
            dataType = model.datatype;
            isEnum = model.isEnum;
            _enum = model._enum;
        }
    }

    @Override
    public ReportixCodegenParameter copy() {
        ReportixCodegenParameter output = new ReportixCodegenParameter();
        copyTo(output);
        output.kind = this.kind;
        output.namePattern = this.namePattern;
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportixCodegenParameter that = (ReportixCodegenParameter) o;
        if (!equalFields(that))
            return false;

        if (kind != that.kind)
            return false;
        if (namePattern != null ? !namePattern.equals(that.namePattern) : that.namePattern != null)
            return false;

        return true;
    }
}
