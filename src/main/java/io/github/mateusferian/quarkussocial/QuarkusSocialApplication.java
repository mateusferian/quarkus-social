package io.github.mateusferian.quarkussocial;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;


@OpenAPIDefinition(
        info = @Info(
                title="API Quarkus Social",
                version = "1.0.1",
                contact = @Contact(
                        name = "Mateus Ferian",
                        url = "https://www.linkedin.com/in/mateus-ferian/?originalSubdomain=br"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class QuarkusSocialApplication extends Application {
}
