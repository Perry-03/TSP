import javassist.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;

public class OnTheFly {
	public static void main(String... args) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		
		CtClass person = pool.makeClass("Person");
		
		CtField nameField = new CtField(pool.get("java.lang.String"), "name", person);
		nameField.setModifiers(Modifier.PRIVATE);
		person.addField(nameField);
		
		CtField dateBirthField = new CtField(pool.get("java.util.Date"), "birthDate", person);
		dateBirthField.setModifiers(Modifier.PRIVATE);
		person.addField(dateBirthField);
		
		CtConstructor constructor = new CtConstructor(
					new CtClass[] { pool.get("java.lang.String"), pool.get("java.util.Date") },
					person
				);
		
		constructor.setBody("{ this.name = $1; this.birthDate = $2; }");
		person.addConstructor(constructor);
		
		person.addMethod(CtNewMethod.getter("getName", nameField));
		person.addMethod(CtNewMethod.getter("getBirthDate", dateBirthField));
		
		final String ageMethodBody = 
				"public int age() {"
				+ "java.util.Calendar now = java.util.Calendar.getInstance();"
				+ "java.util.Calendar birth = java.util.Calendar.getInstance();"
				+ "birth.setTime(this.birthDate);"
				+ "int age = now.get(java.util.Calendar.YEAR) - birth.get(java.util.Calendar.YEAR);"
				+ "return age;"
				+ "}";
		
		person.addMethod(CtNewMethod.make(ageMethodBody, person));
		Class<?> personClazz = person.toClass();
		
		CtClass testPerson = pool.makeClass("TestPerson");
		final String testBody = 
				"public static void main(String[] args) {"
				+ "Person p = new Person(\"Luca\", new java.util.GregorianCalendar(2003, java.util.Calendar.FEBRUARY, 15).getTime());"
				+ "System.out.println(\"Testing name... \" + p.getName());"
				+ "System.out.println(\"Testing birthDate... \" + p.getBirthDate());"
				+ "System.out.println(\"Testing age... \" + p.age());"
				+ "}";
		
		testPerson.addMethod(CtNewMethod.make(testBody, testPerson));
		
		Class<?> testClass = testPerson.toClass();
		
		Method main = testClass.getMethod("main", String[].class);
		main.invoke(null, (Object) new String[] {});
	}
}
