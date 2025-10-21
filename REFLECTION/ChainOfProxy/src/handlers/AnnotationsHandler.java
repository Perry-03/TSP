package handlers;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import annotations.*;

public interface AnnotationsHandler extends InvocationHandler {
    AnnotationsHandler END_OF_CHAIN = (proxy, method, args) -> null;

    public Object invoke(Object proxy, Method method, Object[] args);
}