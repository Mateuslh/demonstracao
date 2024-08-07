package com.projetoDemonstracao.demonstracao.config;

import com.projetoDemonstracao.demonstracao.dto.ResponseDto;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class GlobalOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiResponses apiResponses = operation.getResponses();

        ApiResponse badRequestResponse = new ApiResponse()
                .description("Falha ao executar operação")
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<ResponseDto>().jsonSchemaImpl(ResponseDto.class))
                                .example("{\"sucesso\": false, \"mensagem\": \"Falha ao excecutar operação\", \"retorno\": null}")
                ));

        apiResponses.addApiResponse("400", badRequestResponse);

        return operation;
    }
}
