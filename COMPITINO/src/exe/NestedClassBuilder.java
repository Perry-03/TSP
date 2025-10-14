package exe;

import javassist.*;

public class NestedClassBuilder {
	public static void main(String[] args) {
		createProxy();
		createTest();
		ClassPool cp = ClassPool.getDefault();
		try {
			Loader cl = new Loader(cp);
			Class<?> test = cl.loadClass("TestProxy");			
			test.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void createProxy() {
		ClassPool cp = ClassPool.getDefault();
		
		try {
			cp.importPackage("java.lang.invoke.MethodHandle");
			cp.importPackage("java.lang.invoke.MethodHandles");
			cp.importPackage("java.lang.reflect.InvocationHandler");
			cp.importPackage("java.lang.reflect.Method");
			cp.importPackage("java.lang.reflect.Proxy");
			cp.importPackage("exe");
			
			CtClass nc = cp.get("exe.NestedCalls");
			CtClass ncH = cp.makeClass("NestedCallsHandler", nc);
			ncH.addInterface(cp.get("java.lang.reflect.InvocationHandler"));
			
			ncH.addField(CtField.make("private final exe.NestedCallsI proxy;", ncH));
			ncH.addField(CtField.make("private int counter = 0;", ncH));
			
			ncH.addConstructor(CtNewConstructor.make(
					  "public NestedCallsHandler() {\n"
					+ "proxy = (exe.NestedCallsI) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { exe.NestedCallsI.class }, this);\n"
					+ "}", ncH));
			
			ncH.addMethod(CtNewMethod.make(
					"public String repeat(String str, int n) {\n" +
						"StringBuilder sb = new StringBuilder(str.length() * n);\n" +
						"for(int i = 0; i < counter; i++) {\n" +
							"sb.append(str);\n" +
						"}\n" +
						"return sb.toString();\n" +
					"}", ncH));
			
			ncH.addMethod(CtNewMethod.make(
					"public Object invoke(Object proxy, Method m, Object[] args) {\n" +
						"Object result = null;\n" +
						"try {\n" +
							"final Method superm = NestedCalls.class.getDeclaredMethod(m.getName(), m.getParameterTypes());\n" +
							"counter++;\n" +
							"System.out.println(repeat(\"#\", counter) + \" \" + m.getName());\n" +
							"final MethodHandle methodHandle = MethodHandles.lookup().unreflectSpecial(superm, getClass());\n" +
							"result = methodHandle.bindTo(this).invokeWithArguments(args);\n" +
							"counter--;\n" +
						"} catch(Throwable e) {\n" +
							"e.printStackTrace();\n" +
						"}\n" +
						"return result;\n" +
					"}", ncH));
			
			ncH.addMethod(CtNewMethod.make(
					"public int a() {\n" +
						"return proxy.a();\n" +
					"}", ncH));
			
			ncH.addMethod(CtNewMethod.make(
					"public int b(int a) {\n" +
						"return proxy.b(a);\n" +
					"}", ncH));
			
			ncH.addMethod(CtNewMethod.make(
					"public int c(int a) {\n" +
						"return proxy.c(a);\n" +
					"}", ncH));
			
			ncH.writeFile("./bin/exe");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static void createTest() {
		ClassPool cp = ClassPool.getDefault();
		cp.clearImportedPackages();
		
		cp.importPackage("java.lang.reflect.proxy");
		cp.importPackage("exe");
		
		CtClass test = cp.makeClass("TestProxy");
		
		try {
			test.addConstructor(CtNewConstructor.make(
					"public TestProxy() {\n" +
						"exe.NestedCallsI nc = new NestedCalls();\n" +
						"exe.NestedCallsI ncP = (exe.NestedCallsI) Proxy.newProxyInstance(nc.getClass().getClassLoader(), nc.getClass().getInterfaces(), new NestedCallsHandler());\n" +
						"System.out.println(\"a() :- \" + ncP.a());\n" +
						"System.out.println(\"b(a()) :- \" + ncP.b(ncP.a()));\n" +
						"System.out.println(\"c(b(a())) :- \" + ncP.c(ncP.b(ncP.a())));\n" +
					"}", test));
			test.writeFile("./bin/exe");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
