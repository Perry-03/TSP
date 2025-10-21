import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LoopHandler extends Loop implements InvocationHandler {
    private ILoop proxy;

    public LoopHandler() { 
        proxy = (ILoop) Proxy.newProxyInstance(
            getClass().getClassLoader(), 
            new Class[] { ILoop.class },
            this
        );
    }
    
    public Object invoke(Object proxy, Method m, Object[] args) {
        Object result; 
        try {
            if (loopBreaker(getClass().getName(), m.getName())) { System.out.println("Loop Detected: " + m.getName()); return null; }

            final Method superm = Loop.class.getDeclaredMethod(m.getName(), m.getParameterTypes());
            final MethodHandle h = MethodHandles.lookup().unreflectSpecial(superm, getClass());

            result = h.bindTo(this).invokeWithArguments(args);
            return result;

        } catch (Throwable t) { t.printStackTrace(); return null; }
    }

    private static boolean loopBreaker(final String className, final String methodName) {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        int sameCall = 0;
        for (StackTraceElement e : ste) {
            if (sameCall > 1) return true;
            if (e.getClassName().equals(className) && e.getMethodName().equals(methodName)) sameCall++;
        }

        return false;
    }

    @Override public void a() { proxy.a(); }
    @Override public void b() { proxy.b(); }
    @Override public void c() { proxy.c(); }
    @Override public void d(int n) { proxy.d(n); } 
}