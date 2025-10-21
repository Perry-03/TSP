import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class NestedCallsHandler extends NestedCalls implements InvocationHandler {
    private NestedCallsI proxy;
    private int deepness = 0;

    public NestedCallsHandler() {
        proxy = (NestedCallsI) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[]{ NestedCallsI.class },
            this
        );
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) {
        Object result = null;
        try {
            final Method superm = NestedCalls.class.getDeclaredMethod(m.getName(), m.getParameterTypes());
            System.out.println(repeat("#", ++deepness) + " " + m.getName());
            final MethodHandle mh = MethodHandles.lookup().unreflectSpecial(superm, getClass());
            result = mh.bindTo(this).invokeWithArguments(args);
            deepness--;
        } catch (Throwable t) { t.printStackTrace(); }
        return result;
    }

    public StringBuilder repeat(String symbol, int times) { 
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; i++) 
            result.append(symbol);
        return result;
    }

    @Override 
    public int a() { return proxy.a(); }

    @Override 
    public int b(int a) { return proxy.b(a); }

    @Override 
    public int c(int a) { return proxy.c(a); }



}