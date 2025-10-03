public class Employee {
    private String name;
    private int age;
    private String designation;
    private String department;
    private String managerName;

    public Employee(String name, int age, String designation, String department, String managerName) {
        this.name = name;
        this.age = age;
        this.designation = designation;
        this.department = department;
        this.managerName = managerName;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDepartment() {
        return department;
    }

    public String getManagerName() {
        return managerName;
    }

    @Override
    public String toString() {
        return "Name: " + name + 
               "\nAge: " + age + 
               "\nDesignation: " + designation + 
               "\nDepartment: " + department + 
               "\nManager: " + managerName + "\n";
    }
} 