package ch.hftm.validation;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface AiValidationService {

    @SystemMessage("You are an AI trained to analyze text for sensitive content.")
    @UserMessage("""
        Analyze the following text: '{text}'. 
        Determine if it is appropriate considering sexual, hateful, or other prohibited content. 

        For example:
        - "I love this movie, it was amazing!" should be evaluated as 'APPROPRIATE'
        - "This school is dump" should be evaluated as 'INAPPROPRIATE'
        - "I hate to work so hard" should be evaluated as 'APPROPRIATE'

        Respond with a JSON document containing:
        - the 'text' key set to the original text
        - the 'evaluation' key set to 'APPROPRIATE' if the text is deemed acceptable, 'INAPPROPRIATE' otherwise

        ---
        Original Key: {id}
        Text to Analyze: {text}
        ---
        """)
    String analyzeText(long id, String text);
}