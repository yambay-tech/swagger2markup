package io.github.swagger2markup.internal.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import io.github.swagger2markup.OpenAPI2MarkupConverter;
import io.github.swagger2markup.adoc.ast.impl.SectionImpl;
import io.github.swagger2markup.adoc.ast.impl.TableImpl;
import io.github.swagger2markup.extension.MarkupComponent;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.apache.commons.collections.MapUtils;
import org.asciidoctor.ast.Section;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.ast.Table;

import static io.github.swagger2markup.config.OpenAPILabels.TABLE_HEADER_DESCRIPTION;
import static io.github.swagger2markup.config.OpenAPILabels.TABLE_HEADER_NAME;
import static io.github.swagger2markup.config.OpenAPILabels.TABLE_HEADER_SCHEMA;
import static io.github.swagger2markup.config.OpenAPILabels.TABLE_HEADER_TYPE;
import static io.github.swagger2markup.config.OpenAPILabels.TABLE_TITLE_PARAMETERS;
import static io.github.swagger2markup.internal.helper.OpenApiHelpers.boldUnconstrained;
import static io.github.swagger2markup.internal.helper.OpenApiHelpers.generateInnerDoc;
import static io.github.swagger2markup.internal.helper.OpenApiHelpers.getSchemaTypeAsString;

public class RequestComponent extends MarkupComponent<StructuralNode, RequestBody, StructuralNode> {

    private final SchemaComponent schemaComponent;

    public RequestComponent(OpenAPI2MarkupConverter.OpenAPIContext context) {
        super(context);
        this.schemaComponent = new SchemaComponent(context);
    }

    @Override
    public StructuralNode apply(StructuralNode parent, RequestBody requestBody) {
        if (requestBody == null || MapUtils.isEmpty(requestBody.getContent())) {
            return parent;
        }

        Section section = new SectionImpl(parent);
        section.setTitle("Request Body");

        TableImpl pathParametersTable = new TableImpl(section, new HashMap<>(), new ArrayList<>());
        pathParametersTable.setOption("header");
        pathParametersTable.setAttribute("caption", "", true);
        pathParametersTable.setAttribute("cols", ".^2a,.^6a,.^4a", true);
        pathParametersTable.setHeaderRow("Content Type", "Schema");

        requestBody.getContent().forEach((ct, mt) -> pathParametersTable.addRow(ct, mt.getSchema().get$ref()));

        section.append(pathParametersTable);
        parent.append(section);
        return parent;
    }

//    public static class Parameters {
//        private final RequestBody ;
//
//        public Parameters(RequestBody requestBody) {
//            this.requestBody = requestBody;
//        }
//    }
}
