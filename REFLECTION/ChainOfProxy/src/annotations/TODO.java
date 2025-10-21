package annotations;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TODO {
    public enum Severity { CRITICAL, IMPORTANT, TRIVIAL, DOCUMENTATION };
    String text();
    int duration();
    Severity severity() default Severity.IMPORTANT;
}