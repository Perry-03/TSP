package exe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class NestedCallsHandlerOriginal extends NestedCalls implements InvocationHandler {
	private final NestedCallsI proxy;
	private int counter = 0;
	
	public NestedCallsHandlerOriginal() {
		proxy = (NestedCallsI) Proxy.newProxyInstance(getClass().getClassLoader(), 
													  new Class[] { NestedCallsI.class }, 
													  this);
	}
	
	@Override
	public Object invoke(Object proxy, Method m, Object[] args) {
		Object result = null;
		try {
			final Method superm = NestedCalls.class.getDeclaredMethod(m.getName(), m.getParameterTypes());
			counter++;
			System.out.println(repeat("#", counter) + " " + m.getName());
			final MethodHandle methodHandle = MethodHandles.lookup().unreflectSpecial(superm, getClass());
			result = methodHandle.bindTo(this).invokeWithArguments(args);
			counter--;
		} catch(Throwable e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public int a() {
		return proxy.a();
	}
	
	@Override
	public int b(int a) {
		return proxy.b(a);
	}
	
	@Override
	public int c(int a) {
		return proxy.c(a);
	}
	
	public String repeat(String str, int n) {
		StringBuilder sb = new StringBuilder(str.length() * n);
		for(int i = 0; i < counter; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
}
