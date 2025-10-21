package exe;

import java.lang.reflect.Proxy;

public class TestProxyOriginal {
	public static void main(String[] args) {
		NestedCallsI nc = new NestedCalls();
		
		NestedCallsI ncP = (NestedCallsI) Proxy.newProxyInstance(nc.getClass().getClassLoader(), 
												 nc.getClass().getInterfaces(),
												 new NestedCallsHandlerOriginal());
		System.out.println("a() :- " + ncP.a());
		System.out.println("b(a()) :- " + ncP.b(ncP.a()));
		System.out.println("c(b(a())) :- " + ncP.c(ncP.b(ncP.a())));
	}
}
