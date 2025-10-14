package employee;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import reflection.SmartFieldAccess;

class Employee implements SmartFieldAccess {

    private final Lookup lookup;
    private String name; 
    private String surname;

    public Employee(String n, String s){
        lookup = MethodHandles.lookup(); 
        name = n; 
        surname = s;
    }
    
    public Lookup getEmployeeLookup() { return this.lookup; }

    
}