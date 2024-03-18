package ch.hftm.validation;

import io.smallrye.mutiny.Uni;
import java.util.concurrent.CompletableFuture;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class ValidationService {

    @Inject
    Logger logger;

    @Inject
    AiValidationService aiValidationService;

    @Inject
    @Channel("validation-response")
    Emitter<ValidationResponse> validationResponseEmitter;

    @Incoming("validation-request")
    public Uni<Void> processValidationRequest(ValidationRequest request) {
        return Uni.createFrom().completionStage(processTextAsync(request.id(), request.title(), request.content()))
            .onItem().invoke(result -> {
                logger.infof("Validation result: %s", result);
                sendValidationResponse(result);
            })
            .onFailure().invoke(e -> logger.error("Error during text analysis", e))
            .replaceWithVoid();
    }

        private CompletableFuture<AiAnalyseResult> processTextAsync(long id, String title, String content) {
        return CompletableFuture.supplyAsync(() -> aiValidationService.analyzeText(id, title, content));
    }

    private void sendValidationResponse(AiAnalyseResult result) {
        boolean valid = result.evaluation().equals("APPROPRIATE");
        ValidationResponse response = new ValidationResponse(result.id(), valid, result.reasons());
        validationResponseEmitter.send(response);
    }

}