package reflection;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public interface SmartFieldAccess {
    default public Object instVarAt(Lookup lookup, String name) throws Exception {
        Class<?> clazz = this.getClass();
        Field f = clazz.getDeclaredField(name);
        MethodHandles.Lookup privateLookup = MethodHandles.privateLookupIn(clazz, lookup);
        VarHandle handle = privateLookup.unreflectVarHandle(f);
        if (!Modifier.isStatic(f.getModifiers())) 
            return handle.get(this);
        return null;
    }

    default public void instVarAtPut(Lookup lookup, String n, Object v) throws Exception{
        Class<?> clazz = this.getClass();
        Field f = clazz.getDeclaredField(n);
        MethodHandles.Lookup privateLookup = MethodHandles.privateLookupIn(clazz, lookup);
        VarHandle handle = privateLookup.unreflectVarHandle(f);
        if (!Modifier.isStatic(f.getModifiers())) handle.set(this, v);
    }
}