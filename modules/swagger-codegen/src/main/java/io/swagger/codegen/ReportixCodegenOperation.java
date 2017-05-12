package io.swagger.codegen;

import java.util.ArrayList;
import java.util.List;

public class ReportixCodegenOperation extends CodegenOperation {

    public List<CodegenParameter> hardcodedQueryParams = new ArrayList<CodegenParameter>();
    public List<CodegenParameter> patternQueryParams = new ArrayList<CodegenParameter>();

    public void recomputeOperationLists()
    {
        if (queryParams != null)
        {
            List<CodegenParameter> removeQueryParams = new ArrayList<CodegenParameter>();
            for (CodegenParameter p : queryParams) {
                ReportixCodegenParameter param = (ReportixCodegenParameter) p;
                if (param.kind == ReportixCodegenParameter.Kind.HARDCODED) {
                    removeQueryParams.add(p);
                    hardcodedQueryParams.add(param.copy());
                } else if (param.kind == ReportixCodegenParameter.Kind.PATTERN)
                {
                    removeQueryParams.add(p);
                    patternQueryParams.add(param.copy());
                }

            }
            for (CodegenParameter p : removeQueryParams) {
                queryParams.remove(p);
            }
        }

        if (allParams != null)
        {
            List<CodegenParameter> removeAllParams = new ArrayList<CodegenParameter>();
            for (CodegenParameter p : allParams)
            {
                ReportixCodegenParameter param = (ReportixCodegenParameter) p;
                if(param.kind == ReportixCodegenParameter.Kind.HARDCODED)
                {
                    removeAllParams.add(p);
                }
            }
            for (CodegenParameter p : removeAllParams)
                allParams.remove(p);
        }

        recomputeListProperties(hardcodedQueryParams);
        recomputeListProperties(patternQueryParams);
        recomputeListProperties(allParams);
    }

    private void recomputeListProperties(List<CodegenParameter> list)
    {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).secondaryParam = i > 0;
                list.get(i).hasMore = i < list.size() - 1;
            }
        }
    }
}
