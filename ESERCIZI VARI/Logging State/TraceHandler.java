import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class TraceHandler implements InvocationHandler {
    private Object baseObject;

    public TraceHandler(Object base) { this.baseObject = base; }

    public Object invoke(Object proxy, Method m, Object[] args) {
        try {
            printState("before");
            Object result = m.invoke(baseObject, args); 
            printState("After");
            return result;
        } catch (Throwable t) { t.printStackTrace(); return null; }
    }

    private void printState(String str) throws Throwable {
        System.out.println(str);
        for (Field f : baseObject.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            System.out.println(f.getName() + " -> " + f.get(baseObject));
        }
    }

    public String toString() { return "th:- " + this.baseObject; }
    
}
