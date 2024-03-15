package ch.hftm.validation;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;


@ApplicationScoped
public class TextValidator {

    private static final Logger Log = Logger.getLogger(TextValidator.class);

    @Incoming("validation-request")
    @Outgoing("validation-response")
public Multi<ValidationResponse> validateTextMessages(Multi<ValidationRequest> requests) {
    return requests
            .onItem().transform(request -> {
                boolean valid = !request.text().contains("hftm sucks");
                Log.debug("Text-Validation: " + request.text() + " -> " + valid);
                return new ValidationResponse(request.id(), valid);
            });
}
}
