import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TraceHandler implements InvocationHandler {
    private Object baseObject; 
    private int nestedCount = 1;

    public TraceHandler(Object base) { this.baseObject = base; }

    public Object invoke(Object proxy, Method m, Object[] args) {
        try {
            System.out.println("Nested call: " + nestedCount);
            System.out.println("Calling: " + m.getName());
            nestedCount++;
            Object result = m.invoke(baseObject, args);
            return result;
        } catch (Exception e) { e.printStackTrace(); return null; }
    }
}
