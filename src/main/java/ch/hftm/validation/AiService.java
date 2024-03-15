package ch.hftm.validation;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface AiService {

    @SystemMessage("Analyzing text for validation")
    @UserMessage("""
                Analyze this text: '{text}'. 
                Determine if it's appropriate and does not contain prohibited content.
            """)
    ValidationResponse analyzeText(String text);
}