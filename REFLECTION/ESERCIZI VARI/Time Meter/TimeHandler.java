import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.Duration;

public class TimeHandler implements InvocationHandler {
    private Object baseObject;

    public TimeHandler(Object base) { this.baseObject = base; }

    public Object invoke(Object proxy, Method m, Object[] args) {
        try {
            Instant start = Instant.now();
            Object result = m.invoke(baseObject, args);
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            System.out.println("Method: " + m.getName() + " computed in: "  + duration.toMillis() + " ms");
            return result;
        } catch (Throwable t) { t.printStackTrace(); return null; }
    }

    public String toString() { return "th - " + baseObject; }
}