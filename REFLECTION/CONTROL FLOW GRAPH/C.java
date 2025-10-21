import java.lang.annotation.*; 

public class C {
    @Calls({
        @Call(className = "B", methodName = "m1")
        })
    public void m2() {}
}

