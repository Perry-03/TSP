import java.time.Duration;
import java.time.Instant;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

public class PatchingCode {
	public static void main(String[] args) throws Exception {
		// carico la classe 
		JavaClass jClass = Repository.lookupClass("java.lang.StringBuilder");
		
		// ottengo il metodo append 
		MethodGen mgAppend = getMethodGen(jClass, "append", "([C)Ljava/lang/StringBuilder;");
		
		// cambio il codice 
		ConstantPoolGen cp = mgAppend.getConstantPool();
		InstructionList il = mgAppend.getInstructionList();
		
		InstructionList injectStart = new InstructionList();
		injectStart.append(
				new INVOKESTATIC(cp.addMethodref("java.lang.System", "nanoTime", "()J"))
			);
		injectStart.append(new LSTORE(1));
		il.insert(injectStart); // inserisco il codice che inizia a calcolare il tempo 
		// ora lo inserisco prima del return (calcolo il tempo totale)
		
		InstructionList injectEnd = new InstructionList();
		injectEnd.append(
				new INVOKESTATIC(cp.addMethodref("java.lang.System", "nanoTime", "()J"))
			);
		injectEnd.append(new LLOAD(1));
		injectEnd.append(new LSUB());
		injectEnd.append(new LSTORE(3));
		injectEnd.append(new GETSTATIC(cp.addFieldref("java.lang.System", "out", "Ljava/io/PrintStream;")));
		injectEnd.append(new LLOAD(3));
		injectEnd.append(new INVOKEVIRTUAL(cp.addMethodref("java.io.PrintStream", "println", "(J)V")));
		
		il.append(injectEnd);
		mgAppend.setMaxStack();
		mgAppend.setMaxLocals();
		
		// ottengo il metodo insert 
		// MethodGen mgInsert = getMethodGen(jClass, "insert", "(I[Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;");
		
		// cambio il codice 
		
		// scrivo la classe sul disco o in un classloader 
		
		ClassGen cg = new ClassGen(jClass);
		byte[] modifiedBytes = cg.getJavaClass().getBytes();
		
		class ByteArrayClassLoader extends ClassLoader {
		    public Class<?> define(String name, byte[] b) {
		        return defineClass(name, b, 0, b.length);
		    }
		}
		
		ByteArrayClassLoader loader = new ByteArrayClassLoader();

		Class<?> clazz = loader.define("ModifiedStringBuilder", modifiedBytes);
		Object sb = clazz.getConstructor(String.class).newInstance("prova");
		clazz.getMethod("append", char[].class).invoke(sb, new char[]{'x', 'y', 'z'});

		Class<?> cls = loader.define("mypackage.ModifiedStringBuilder", modifiedBytes);
        Object testSb = cls.getConstructor().newInstance();
        cls.getMethod("append", char[].class).invoke(testSb, new char[]{'a', 'b', 'c'});
		
	}
	
	private static MethodGen getMethodGen(final JavaClass clazz, final String methodName, final String signature) {
		for (Method m : clazz.getMethods())
			if (m.getName().equals(methodName) && (m.getSignature().equals(signature)))
				return new MethodGen(m, clazz.getClassName(), new ConstantPoolGen(clazz.getConstantPool()));
		
		throw new RuntimeException("Method not found: " + methodName + " having signature: " + signature);
	}
	
	
}
