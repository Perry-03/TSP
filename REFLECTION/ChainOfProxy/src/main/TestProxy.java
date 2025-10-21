package main;

import annotations.*;
import handlers.*;
import java.lang.reflect.Proxy;

public class TestProxy {
    public static void main(String... args) {
        AnnotationsI ann = new Annotations();

        AnnotationsI ai = (AnnotationsI) Proxy.newProxyInstance(
            ann.getClass().getClassLoader(),
            new Class[]{ AnnotationsI.class },
            new CallsHandler(ann)
        );

        // ai.printStr("Hello World!");
        // ai.printStr(null);

        // ai.longerThan("ciao", "stringalunga");
        // ai.longerThan("stringalunga", "ciao");

        ai.testCallingAnnotations();
    }
}