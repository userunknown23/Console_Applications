import java.util.*;

public class EmployeeManagementSystem {
    private List<Employee> employees;
    private Scanner scanner;

    public EmployeeManagementSystem() {
        employees = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void addEmployee() {
        System.out.println("\nEnter employee details:");
        
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Designation: ");
        String designation = scanner.nextLine();
        
        System.out.print("Department: ");
        String department = scanner.nextLine();
        
        System.out.print("Manager Name: ");
        String managerName = scanner.nextLine();

        employees.add(new Employee(name, age, designation, department, managerName));
        System.out.println("Employee added successfully!\n");
    }

    public void printAllEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        
        System.out.println("\n=== All Employee Details ===");
        for (Employee emp : employees) {
            System.out.println(emp);
            System.out.println("-------------------------");
        }
    }

    public void searchEmployee() {
        System.out.print("\nEnter employee name to search: ");
        String searchName = scanner.nextLine();
        
        boolean found = false;
        for (Employee emp : employees) {
            if (emp.getName().toLowerCase().contains(searchName.toLowerCase())) {
                System.out.println("\nEmployee found:");
                System.out.println(emp);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No employee found with the given name.");
        }
    }

    public void findEmployeesUnderManager() {
        System.out.print("\nEnter manager name: ");
        String managerName = scanner.nextLine();
        
        System.out.print("Enter department: ");
        String department = scanner.nextLine();
        
        boolean found = false;
        System.out.println("\nEmployees under manager '" + managerName + "' in department '" + department + "':");
        
        for (Employee emp : employees) {
            if (emp.getManagerName().equalsIgnoreCase(managerName) && 
                emp.getDepartment().equalsIgnoreCase(department)) {
                System.out.println(emp);
                System.out.println("-------------------------");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No employees found under the specified manager and department.");
        }
    }

    public void printReportingTree() {
        System.out.print("\nEnter employee name to show reporting tree: ");
        String employeeName = scanner.nextLine();
        
        // Find the employee
        Employee targetEmployee = null;
        for (Employee emp : employees) {
            if (emp.getName().equalsIgnoreCase(employeeName)) {
                targetEmployee = emp;
                break;
            }
        }
        
        if (targetEmployee == null) {
            System.out.println("Employee not found.");
            return;
        }

        // Print reporting tree
        System.out.println("\nReporting Tree for " + employeeName + ":");
        printReportingTreeRecursive(targetEmployee, 0);
    }

    private void printReportingTreeRecursive(Employee employee, int level) {
        // Print the current employee with proper indentation
        String indent = "  ".repeat(level);
        System.out.println(indent + "└─ " + employee.getName() + " (" + employee.getDesignation() + ")");
        
        // Find and print all employees reporting to this employee
        for (Employee emp : employees) {
            if (emp.getManagerName().equalsIgnoreCase(employee.getName())) {
                printReportingTreeRecursive(emp, level + 1);
            }
        }
    }

    public static void main(String[] args) {
        EmployeeManagementSystem ems = new EmployeeManagementSystem();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to Employee Management System!");
        
        // Input 10 employees
        System.out.println("\nPlease enter details for 10 employees:");
        for (int i = 0; i < 3; i++) {
            System.out.println("\nEmployee #" + (i + 1));
            ems.addEmployee();
        }

        while (true) {
            System.out.println("\n=== Employee Management System Menu ===");
            System.out.println("1. Print all employee details");
            System.out.println("2. Search employee details");
            System.out.println("3. Find employees under manager");
            System.out.println("4. Show reporting tree");
            System.out.println("5. Exit");
            System.out.print("\nEnter your choice (1-5): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1:
                    ems.printAllEmployees();
                    break;
                case 2:
                    ems.searchEmployee();
                    break;
                case 3:
                    ems.findEmployeesUnderManager();
                    break;
                case 4:
                    ems.printReportingTree();
                    break;
                case 5:
                    System.out.println("\nThank you for using Employee Management System!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
} 