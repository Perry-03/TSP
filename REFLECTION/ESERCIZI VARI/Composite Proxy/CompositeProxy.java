import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface CompositeProxy extends InvocationHandler {
    CompositeProxy NULL_HANDLER = (proxy, m, args) -> null;

    Object invoke(Object proxy, Method m, Object[] args);
}