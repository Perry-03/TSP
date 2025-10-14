package annotations;

public class Annotations implements AnnotationsI {
    
    public void printStr(@NotNull String str) { System.out.println(str); }

    @TODO(
        text="This method is useless",
        duration=5,
        severity=TODO.Severity.TRIVIAL
    )
    public String longerThan(@NotNull String str1, @NotNull String str2) {
        if (str1.length() < str2.length()) 
            return str1;

        return null;
    }

    public void testCallingAnnotations() {}
}