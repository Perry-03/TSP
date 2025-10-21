import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class LoggingStateHandler implements CompositeProxy {
    private Object baseObject;
    private CompositeProxy nextHandler;

    public LoggingStateHandler(Object base, CompositeProxy next) { 
        this.baseObject = base; 
        this.nextHandler = next;
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) {
        try {
            printState("before");
            Object result = m.invoke(baseObject, args); 
            printState("After");
            if (nextHandler == null) 
                return result;
            else return nextHandler.invoke(proxy, m, args);
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
