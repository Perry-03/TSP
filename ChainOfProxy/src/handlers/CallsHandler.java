package handlers;
import java.lang.reflect.Method;
import java.lang.annotation.*;
import annotations.*;

public class CallsHandler implements AnnotationsHandler {
    private Object baseObject;

    public CallsHandler(Object base) { this.baseObject = base; }
    
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            Annotation[] anns = method.getDeclaredAnnotations();
            for (Annotation a : anns)
                System.out.println(a);
                    //findCalls(method, a);
                
            return method.invoke(baseObject, args);
        } catch (IllegalArgumentException e) { 
            System.out.println(e.getMessage()); 
        } catch (Throwable t) { t.printStackTrace(); }
        return null;
    }

    private void findCalls(Method method, Annotation a) {
        System.out.println(
                        "Method: " + method.getName() +
                        "of class: " + method.getDeclaringClass().getName() +
                        "calls: ");
    }
}