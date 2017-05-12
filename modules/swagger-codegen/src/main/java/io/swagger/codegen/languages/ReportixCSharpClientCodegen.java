package io.swagger.codegen.languages;

import io.swagger.codegen.*;
import io.swagger.codegen.utils.ReportixUtils;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportixCSharpClientCodegen extends CSharpClientCodegen {
    boolean generateInlineModels;
    protected static final Logger LOGGER = LoggerFactory.getLogger(ReportixCSharpClientCodegen.class);
    
    public ReportixCSharpClientCodegen()
    {
        super();
        CodegenModelFactory.setTypeMapping(CodegenModelType.OPERATION, ReportixCodegenOperation.class);
        CodegenModelFactory.setTypeMapping(CodegenModelType.PARAMETER, ReportixCodegenParameter.class);
        generateInlineModels = System.getProperty(ReportixUtils.NO_INLINE_MODELS) != null ? false:true;
    };

    @Override
    public void processOpts() {
        super.processOpts();
        additionalProperties.put("noAutoAcceptHeader", true);
        additionalProperties.put("noInlineModels", !generateInlineModels);
    }


    @Override
    public String getName() {
        return "reportix-csharp";
    }


    /**
     * Convert Swagger Operation object to Codegen Operation object (without providing a Swagger object)
     *
     * @param path the path of the operation
     * @param httpMethod HTTP method
     * @param operation Swagger operation object
     * @param definitions a map of Swagger models
     * @return Codegen Operation object
     */
    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions) {
        return fromOperation(path, httpMethod, operation, definitions, null);
    }

    /**
     * Convert Swagger Operation object to Codegen Operation object
     *
     * @param path the path of the operation
     * @param httpMethod HTTP method
     * @param operation Swagger operation object
     * @param definitions a map of Swagger models
     * @param swagger a Swagger object representing the spec
     * @return Codegen Operation object
     */
    @Override
    public CodegenOperation fromOperation(String path,
                                          String httpMethod,
                                          Operation operation,
                                          Map<String, Model> definitions,
                                          Swagger swagger)
    {
        /*
         * Remove excluded parameters
         */
        List<Parameter> parameters = operation.getParameters();
        List<Parameter> removeParams = new ArrayList<Parameter>();
        if (parameters != null)
        {
            for (Parameter param : parameters)
                if (ReportixUtils.isExcluded(param.getVendorExtensions()))
                    removeParams.add(param);

            for (Parameter param : removeParams)
                parameters.remove(param);

            operation.setParameters(parameters);
        }

        CodegenOperation codegenOperation =  super.fromOperation(path, httpMethod, operation, definitions, swagger);

        /*
         * Moves the hardcoded/pattern parameters in the correct list
         */
        ((ReportixCodegenOperation) codegenOperation).recomputeOperationLists();

        return codegenOperation;
    }

    @Override
    public CodegenParameter fromParameter(Parameter param, Set<String> imports)
    {
        ReportixCodegenParameter parameter = (ReportixCodegenParameter) super.fromParameter(param, imports);

        /*
         * If the generation of models is disabled, reset user defined types to Object
         */
        if (!generateInlineModels && param instanceof BodyParameter)
        {
            BodyParameter bp = (BodyParameter) param;
            Model model = bp.getSchema();
            if (
                    model != null &&
                            (
                                    (parameter.baseType != null && !parameter.baseType.equals("Object")) ||
                                            !parameter.dataType.equals("Object")
                            )
                    )
            {
                LOGGER.warn("Generation of inline models is disabled, resetting the type of " + parameter.paramName + " to Object");
                parameter.baseType = "Object";
                parameter.dataType = "Object";
                parameter.isPrimitiveType = true;
            }
        }

        /*
         * Compute the overridden name, description and default value
         */
        parameter.init(this, param);

        return parameter;
    }

    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> operations)
    {
        /*
         * Remove excluded operations
         */
        Map<String, Object> objs = (Map<String, Object>) operations.get("operations");
        List<CodegenOperation> ops = (List<CodegenOperation>) objs.get("operation");
        List<CodegenOperation> removeOps = new ArrayList<CodegenOperation>();
        for (CodegenOperation op : ops)
            if (ReportixUtils.isExcluded(op.vendorExtensions))
                removeOps.add(op);

        for (CodegenOperation o : removeOps)
            ops.remove(o);

        return super.postProcessOperations(operations);
    }
}
