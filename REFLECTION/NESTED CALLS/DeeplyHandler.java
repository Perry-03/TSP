import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.invoke.*;

public class DeeplyHandler extends NestedCalls implements InvocationHandler {
    private final NestedCallsI proxy;
    private int counter = 1;

    public DeeplyHandler() {
        proxy = (NestedCallsI) Proxy.newProxyInstance(
            getClass().getClassLoader(), 
            new Class[]{ NestedCallsI.class }, 
            this);
    }

    public Object invoke(Object proxy, Method m, Object[] args) {
        Object result = null;
        try {
        
            final Method superm = NestedCalls.class.getDeclaredMethod(m.getName(), m.getParameterTypes());
            counter++;
            System.out.println(repeat("#", counter) + " " + m.getName());
            final MethodHandle h = MethodHandles.lookup().unreflectSpecial(superm, getClass());
            result = h.bindTo(this).invokeWithArguments(args);
            counter--;
        } catch (Throwable e) { e.printStackTrace(); return null; }
        return result;
    }

    private StringBuilder repeat(String str, int times) { 
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++)
            sb.append(str);

        return sb;
     }

    @Override public int a() { return proxy.a(); }
    @Override public int b(int a) { return proxy.b(a); }
    @Override public int c(int a) { return proxy.c(a); }
}
