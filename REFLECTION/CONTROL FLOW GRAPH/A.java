import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class A {

    @Calls({@Call(className = "B", methodName = "m1"),
            @Call(className = "C", methodName = "m2")})
    public void f() { 
        // il corpo non importa
    }
}
