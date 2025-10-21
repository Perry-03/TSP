import java.lang.reflect.Proxy;

public class TestProxy {
    public static void main(String... args) {
        NestedCallsI nc = new NestedCalls();

        NestedCallsI nci = (NestedCallsI) Proxy.newProxyInstance(
            nc.getClass().getClassLoader(),
            new Class[]{ NestedCallsI.class },
            new NestedCallsHandler()
        );

        System.out.println("a() :- " + nci.a());
		System.out.println("b(a()) :- " + nci.b(nci.a()));
		System.out.println("c(b(a())) :- " + nci.c(nci.b(nci.a())));
    }
}