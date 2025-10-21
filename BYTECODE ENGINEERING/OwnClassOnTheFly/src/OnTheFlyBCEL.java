import org.apache.bcel.generic.*;

import java.io.IOException;
import java.lang.reflect.Modifier;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.Const;


public class OnTheFlyBCEL {
	public static void main(String... args) throws IOException {
		ClassGen cg = new ClassGen("Person", "java.lang.Object", "Person.java", Modifier.PUBLIC, null);
		
		ConstantPoolGen cp = cg.getConstantPool();
		addField(cp, cg, "name", Modifier.PRIVATE, Type.STRING);
		addField(cp, cg, "birthDate", Modifier.PRIVATE, Type.getType(java.util.Date.class));
		
		// getName
		InstructionList il = new InstructionList();	
		
		il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
		il.append(new GETFIELD(cp.addFieldref("Person", "name", "Ljava/lang/String;")));
		il.append(InstructionFactory.createReturn(Type.STRING));
		
		addMethod(Modifier.PUBLIC, Type.STRING, Type.NO_ARGS, "getName", "Person", il, cp, cg);
		
		il = new InstructionList();	
		
		il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
		il.append(new GETFIELD(cp.addFieldref("Person", "birthDate", "Ljava/util/Date;")));
		il.append(InstructionFactory.createReturn(Type.getType(java.util.Date.class)));
		addMethod(Modifier.PUBLIC, Type.getType(java.util.Date.class), Type.NO_ARGS, "getBirthDate", "Person", il, cp, cg);
		
		// age 
		il= new InstructionList();
		
		il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
		il.append(new GETFIELD(cp.addFieldref("Person", "birthDate", "Ljava/util/Date;")));
		il.append(new INVOKEVIRTUAL(cp.addMethodref("java.util.Date", "getYear", "()I")));
		il.append(new ISTORE(1));
		
		il.append(new INVOKESTATIC(cp.addMethodref("java.util.Calendar", "getInstance", "()Ljava/util/Calendar;")));
		il.append(new PUSH(cp, java.util.Calendar.YEAR));
		
		il.append(new INVOKEVIRTUAL(cp.addMethodref("java.util.Calendar", "get", "(I)I")));
		il.append(new ISTORE(2)); // currentYear

		// currentYear - birthYear
		il.append(new ILOAD(2));
		il.append(new ILOAD(1));
		il.append(new ISUB());
		
		il.append(InstructionFactory.createReturn(Type.INT));
		
		addMethod(Modifier.PUBLIC, Type.INT, Type.NO_ARGS, "age", "Person", il, cp, cg);
		
		// Costruttore	
		InstructionList ilCons = new InstructionList();
		Type[] paramTypes = new Type[] { Type.STRING, Type.getType(java.util.Date.class) };
		String[] paramNames = new String[] { "name", "birthDate" };
		
		ilCons.append(InstructionFactory.createLoad(Type.OBJECT, 0));
		ilCons.append(new INVOKESPECIAL(cp.addMethodref("java.lang.Object", "<init>", "()V")));
		
		ilCons.append(InstructionFactory.createLoad(Type.OBJECT, 0));
		ilCons.append(InstructionFactory.createLoad(Type.OBJECT, 1));
		ilCons.append(new PUTFIELD(cp.addFieldref("Person", "name", "Ljava/lang/String;")));
		
		ilCons.append(InstructionFactory.createLoad(Type.OBJECT, 0));
		ilCons.append(InstructionFactory.createLoad(Type.OBJECT, 2));
		ilCons.append(new PUTFIELD(cp.addFieldref("Person", "birthDate", "Ljava/util/Date;")));
		
		ilCons.append(InstructionFactory.createReturn(Type.VOID));
		
		MethodGen constructorM = new MethodGen(
				Modifier.PUBLIC,
				Type.VOID,
				paramTypes,
				paramNames,
				"<init>",
				"Person",
				ilCons,
				cp
			);
		
		constructorM.setMaxStack();
		cg.addMethod(constructorM.getMethod());
		
		// creo il .class
		cg.getJavaClass().dump("Person.class");
		
		// Classe di test 
		ClassGen testCg = new ClassGen("TestPerson", "java.lang.Object", "TestPerson.java", Modifier.PUBLIC, null);
		ConstantPoolGen cpTest = testCg.getConstantPool();
		InstructionFactory factory = new InstructionFactory(testCg, cpTest);
		InstructionList ilTest = new InstructionList();
		
		ilTest.append(factory.createNew("Person"));
		ilTest.append(new DUP());
		ilTest.append(new PUSH(cpTest, "Luca"));
		
		ilTest.append(factory.createNew("java.util.Date"));
		ilTest.append(new DUP());
		ilTest.append(new PUSH(cpTest, 2003));
		ilTest.append(new PUSH(cpTest, 2));
		ilTest.append(new PUSH(cpTest, 20));
		ilTest.append(factory.createInvoke("java.util.Date", "<init>", Type.VOID,
		        new Type[]{Type.INT, Type.INT, Type.INT}, Const.INVOKESPECIAL));
		ilTest.append(factory.createInvoke("Person", "<init>", Type.VOID,
		        new Type[]{Type.STRING, Type.getType("Ljava/util/Date;")}, Const.INVOKESPECIAL));
		ilTest.append(InstructionFactory.createStore(Type.OBJECT, 1)); // p = ...

		// System.out.println("Name: " + p.getName())
		ilTest.append(factory.createFieldAccess("java.lang.System", "out", Type.getType("Ljava/io/PrintStream;"), Const.GETSTATIC));
		ilTest.append(InstructionFactory.createLoad(Type.OBJECT, 1));
		ilTest.append(factory.createInvoke("Person", "getName", Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
		ilTest.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[]{Type.STRING}, Const.INVOKEVIRTUAL));

		// System.out.println("Age: " + p.age())
		ilTest.append(factory.createFieldAccess("java.lang.System", "out", Type.getType("Ljava/io/PrintStream;"), Const.GETSTATIC));
		ilTest.append(InstructionFactory.createLoad(Type.OBJECT, 1));
		ilTest.append(factory.createInvoke("Person", "age", Type.INT, Type.NO_ARGS, Const.INVOKEVIRTUAL));
		ilTest.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[]{Type.INT}, Const.INVOKEVIRTUAL));

		// return
		ilTest.append(InstructionFactory.createReturn(Type.VOID));

		MethodGen main = new MethodGen(
		    Modifier.PUBLIC | Modifier.STATIC,
		    Type.VOID,
		    new Type[]{ new ArrayType(Type.STRING, 1) },
		    new String[]{ "args" },
		    "main",
		    "TestPerson",
		    ilTest,
		    cpTest
		);
		main.setMaxStack();
		main.setMaxLocals();
		testCg.addMethod(main.getMethod());

		testCg.getJavaClass().dump("TestPerson.class");
		/* 
		 * Per compilare e runnare:
		 * CP="path_al_jar:secondo_path_al_secondo_jar" (nel modo /c/Users/... se sono su Linux)
		 * 
		 * javac -cp "$CP" OnTheFlyBCEL.java
		 * java -cp "$CP" OnTheFlyBCEL --> questo crea i .class di Person e TestPerson
		 * java -cp "$CP" TestPerson --> esegue il main della classe TestPerson
		*/			
	}
	
	private static void addField(final ConstantPoolGen cp, final ClassGen cg,
			final String fieldName, int modifier, Type t) {
		FieldGen nameField = new FieldGen(
				modifier,
				t,
				fieldName,
				cp
			);
		cg.addField(nameField.getField());
	}
	
	private static void addMethod(int modifier, final Type returnType, final Type[] args, final String methodName,
			final String className, final InstructionList il, final ConstantPoolGen cp, final ClassGen cg) {		
		MethodGen mg = new MethodGen(
				modifier, 
				returnType,
				args, 
				null,
				methodName,
				className,
				il,
				cp
			);
		
		mg.setMaxStack();
		mg.setMaxLocals();
        cg.addMethod(mg.getMethod());
        il.dispose();
	}
}










