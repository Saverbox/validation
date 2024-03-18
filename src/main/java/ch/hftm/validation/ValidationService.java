package ch.hftm.validation;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.util.concurrent.CompletableFuture;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ValidationService {

    @Inject
    Logger logger;

    @Inject
    AiValidationService aiValidationService;

    @Incoming("validation-request")
    public Uni<Void> processValidationRequest(ValidationRequest request) {
        return Uni.createFrom().completionStage(processTextAsync(request.id(), request.text()))
            .onItem().invoke(result -> logger.infof("Validation result: %s", result))
            .onFailure().invoke(e -> logger.error("Error during text analysis", e))
            .replaceWithVoid();
    }

        private CompletableFuture<String> processTextAsync(long id, String text) {
        return CompletableFuture.supplyAsync(() -> aiValidationService.analyzeText(id, text));
    }

    /*@Incoming("validation-request")
    @Outgoing("validation-response")
    public Multi<ValidationResponse> validateTextMessages(Multi<ValidationRequest> requests) {
        return requests
                .onItem().transform(request -> {
                    boolean valid = !request.text().contains("hftm sucks");
                    logger.debug("Text-Validation: " + request.text() + " -> " + valid);
                    return new ValidationResponse(request.id(), valid);
                });
    } */
}