package annotations;
import annotations.NotNull;

public interface AnnotationsI {
    public void printStr(@NotNull String str);
    public String longerThan(@NotNull String str1, @NotNull String str2);
    
    @Calls(value =
        {@Call(className="NotNullHandler", methodName="invoke"),
        @Call(className="Annotations", methodName="printStr")}
    )
    public void testCallingAnnotations();
}