package mylene;

import java.util.Scanner;

class Employee {

    public void employeeInfo() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("\n----EMPLOYEE INFORMATION----");
            System.out.println("1. ADD EMPLOYEE");
            System.out.println("2. VIEW EMPLOYEES");
            System.out.println("3. UPDATE EMPLOYEE");
            System.out.println("4. DELETE EMPLOYEE");
            System.out.println("5. EXIT");
            System.out.println("\n----------------------------");

            System.out.print("Enter Action: ");
            int action = getValidAction(sc);

            switch (action) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    viewEmployees();
                    updateEmployee();
                    viewEmployees();
                    break;
                case 4:
                    viewEmployees();
                    deleteEmployee();
                    viewEmployees();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
            System.out.print("Do you want to continue? (yes/no): ");
            response = sc.next();
        } while (response.equalsIgnoreCase("yes"));

        System.out.println("Thank you, See you soon!");
    }

    public int getValidAction(Scanner sc) {
        int action = 0;
        boolean validInput = false;

        while (!validInput) {
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                sc.next();
            }
            action = sc.nextInt();

            if (action >= 1 && action <= 5) {
                validInput = true;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        return action;
    }

    public void addEmployee() {
    Scanner sc = new Scanner(System.in);
    config conf = new config(); // Ensure this class handles database interaction correctly.

    int id = 0;
    while (true) {
        System.out.print("Enter Employee ID (must be 1001 or higher): ");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a numeric Employee ID.");
            sc.next(); // Consume invalid input
        }
        id = sc.nextInt();

        if (id >= 1001) {
            String checkQuery = "SELECT COUNT(*) FROM tbl_employee WHERE e_id = ?";
            boolean exists = conf.recordExists(checkQuery, id);
            if (exists) {
                System.out.println("Error: The Employee ID " + id + " already exists. Please try a different ID.");
            } else {
                System.out.println("Employee ID is valid. Proceeding to add employee.");
                break;
            }
        } else {
         
        }
    }
    sc.nextLine(); 

    String fname = getInput(sc, "Enter First Name: ");
    String lname = getInput(sc, "Enter Last Name: ");
    String position = getInput(sc, "Enter Position: ");
    String department = getInput(sc, "Enter Department: ");

    String sql = "INSERT INTO tbl_employee (e_id, fname, lname, position, department) VALUES (?, ?, ?, ?, ?)";
    conf.addRecord(sql, id, fname, lname, position, department);

    System.out.println("Employee added successfully!");
}

    public void viewEmployees() {
        config conf = new config();

        String query = "SELECT e_id, fname, lname, position, department FROM tbl_employee";
        String[] headers = {"ID", "First Name", "Last Name", "Position", "Department"};
        String[] columns = {"e_id", "fname", "lname", "position", "department"};

        conf.viewRecords(query, headers, columns);
    }

    public void updateEmployee() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        int id = getValidId(sc, conf, "Enter the ID to update: ");

        String fname = getInput(sc, "New Employee First Name: ");
        String lname = getInput(sc, "New Employee Last Name: ");
        String position = getInput(sc, "New Employee Position: ");
        String department = getInput(sc, "New Employee Department: ");

        String sql = "UPDATE tbl_employee SET fname = ?, lname = ?, position = ?, department = ? WHERE e_id = ?";
        conf.updateRecord(sql, fname, lname, position, department, id);

        System.out.println("Employee updated successfully!");
    }

    public void deleteEmployee() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        int id = getValidId(sc, conf, "Enter Employee ID to delete: ");

        String sql = "DELETE FROM tbl_employee WHERE e_id = ?";
        conf.deleteRecord(sql, id);

        System.out.println("Employee deleted successfully.");
    }

    private String getInput(Scanner sc, String message) {
        String input;
        while (true) {
            System.out.print(message);
            input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                break;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
        return input;
    }

    private int getValidId(Scanner sc, config conf, String message) {
        int id = 0;
        while (true) {
            System.out.print(message);
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid Employee ID.");
                sc.next();
            }
            id = sc.nextInt();
            if (conf.recordExists("SELECT COUNT(*) FROM tbl_employee WHERE e_id = ?", id)) {
                break;
            } else {
                System.out.println("Error: Employee ID does not exist. Please try again.");
            }
        }
        return id;
    }
}
