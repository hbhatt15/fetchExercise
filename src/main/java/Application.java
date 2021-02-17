import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@ComponentScan(basePackages = {"com.fetch.exercise"})
public class Application {

    public static void main(String[] args) {

        final Class<?>[] sources = {
                Application.class
        };

        SpringApplication.run(sources, args);
    }
}