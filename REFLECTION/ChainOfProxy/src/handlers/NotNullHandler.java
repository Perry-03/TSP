package handlers;

import java.lang.reflect.Method;
import java.lang.annotation.*;
import annotations.*;

public class NotNullHandler implements AnnotationsHandler {
    private Object baseObject;

    public NotNullHandler(Object base) { this.baseObject = base; }

    public Object invoke(Object proxy, Method method, Object... args) {
        try {
            Annotation[][] anns = method.getParameterAnnotations();
            for (int i = 0; i < anns.length; i++) 
                for (Annotation a : anns[i])
                    if (a.annotationType() == NotNull.class && args[i] == null) 
                        throw new IllegalArgumentException("@NotNull contract violeted for parameter number: " + i);
                
            return method.invoke(baseObject, args);
        } catch (IllegalArgumentException e) { 
            System.out.println(e.getMessage()); 
        } catch (Throwable t) { t.printStackTrace(); }
        return null;
    }

}