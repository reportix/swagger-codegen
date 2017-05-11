package io.swagger.codegen.languages;

import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.utils.ReportixUtils;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportixCSharpClientCodegen extends CSharpClientCodegen {
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

        return super.fromOperation(path, httpMethod, operation, definitions, swagger);
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
