import java.lang.reflect.Field;

class ChangeValue {
    public static void main(String... args) {
        TestingFields t = new TestingFields(7, 3.14);
        System.out.println("Valore di s: " + t.s);   
        
        try {
        Class<?> cls = TestingFields.class;
        Field f = cls.getDeclaredField("s");
        f.setAccessible(true);
        f.set(t, "passato!!!");
        System.out.println("Valore di s: " + f.get(t));
           
        } catch (Exception ignored) {}

        TestingFields t2 = new TestingFields(7, 2.4);
        System.out.println("Valore di s: " + t2.s);
    }
}
