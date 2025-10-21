import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.Duration;

public class TimeCounterHandler implements CompositeProxy {
    private Object baseObject;
    private CompositeProxy nextHandler;

    public TimeCounterHandler(Object base, CompositeProxy next) { 
        this.baseObject = base; 
        this.nextHandler = next;
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) {
        try {
            Instant start = Instant.now();
            Object result = m.invoke(baseObject, args);
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            System.out.println("Method: " + m.getName() + " computed in: "  + duration.toMillis() + " ms");
            if (nextHandler == null) 
                return result;
            else return nextHandler.invoke(proxy, m, args);
        } catch (Throwable t) { t.printStackTrace(); return null; }
    }

    public String toString() { return "th - " + baseObject; }
}