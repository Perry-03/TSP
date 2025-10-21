package employee;

public class GlasnotEmployeeMain {
    public static void main(String[] args) throws Exception {
        Employee angela = new Employee("Angela", "Runedottir");
        System.out.println(angela);
        angela.instVarAtPut(angela.getEmployeeLookup(), "surname", "Odindottir");
        System.out.println(angela);
    }
}