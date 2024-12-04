package mylene;

import java.util.InputMismatchException;
import java.util.Scanner;

class Payslip {

    public void Payslip() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("\n---------------GENERATE PAYSLIP-----------------");
            System.out.println("1. ADD PAYSLIP");
            System.out.println("2. VIEW PAYSLIPS");
            System.out.println("3. UPDATE PAYSLIP");
            System.out.println("4. DELETE PAYSLIP");
            System.out.println("5. EXIT");
            System.out.println("\n------------------------------------------------");

            System.out.print("Enter Action: ");
            int action = getValidIntInput(sc, 1, 5);

            switch (action) {
                case 1:
                    addPayslip();
                    break;
                case 2:
                    viewPayslips();
                    break;
                case 3:
                    viewPayslips();
                    updatePayslip();
                    viewPayslips();
                    break;
                case 4:
                    viewPayslips();
                    deletePayslip();
                    viewPayslips();
                    break;
                case 5:
                    System.out.println("Exiting Generate Payslip ...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
            System.out.print("Do you want to continue? (yes/no): ");
            response = sc.next();
        } while (response.equalsIgnoreCase("yes"));

        System.out.println("Thank you, See you soon!");
    }

    public void addPayslip() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        int payslipId;
        while (true) {
            System.out.print("Enter Payslip ID: ");
            payslipId = getValidPositiveIntInput(sc);
            if (payslipId >= 1) {
                break;
            }
            System.out.println("Invalid Payslip ID. It must be 1 or above.");
        }

        int employeeId;
        while (true) {
            System.out.print("Enter Employee ID: ");
            employeeId = getValidPositiveIntInput(sc);

            // Validate e_id exists in the database
            String checkEmployeeQuery = "SELECT COUNT(*) FROM tbl_employee WHERE e_id = ?";
            if (conf.recordExists(checkEmployeeQuery, employeeId)) {
                break;
            }
            System.out.println("Employee ID does not exist. Please enter a valid Employee ID.");
        }

        System.out.print("Enter Basic Salary: ");
        double basicSalary = getValidPositiveDoubleInput(sc);

        System.out.print("Enter Allowances: ");
        double allowances = getValidPositiveDoubleInput(sc);

        System.out.print("Enter Deductions: ");
        double deductions = getValidNonNegativeDoubleInput(sc);

        System.out.print("Enter Overtime Pay: ");
        double overtimePay = getValidPositiveDoubleInput(sc);

        System.out.print("Enter Bonuses: ");
        double bonuses = getValidPositiveDoubleInput(sc);

        double grossSalary = basicSalary + allowances + overtimePay + bonuses;

        double netSalary = calculateNetSalary(grossSalary, deductions);

        String sql = "INSERT INTO tbl_gpayslip (p_id, e_id, basic_salary, allowances, deductions, overtime_pay, bonuses, gross_salary, net_salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(sql, payslipId, employeeId, basicSalary, allowances, deductions, overtimePay, bonuses, grossSalary, netSalary);

        System.out.println("Payslip added successfully!");
    }

    public void viewPayslips() {
        config conf = new config();

        String query = "SELECT p_id, e_id, basic_salary, allowances, deductions, overtime_pay, bonuses, gross_salary, net_salary FROM tbl_gpayslip";
        String[] headers = {"Payslip ID", "Employee ID", "Basic Salary", "Allowances", "Deductions", "Overtime Pay", "Bonuses", "Gross Salary", "Net Salary"};
        String[] columns = {"p_id", "e_id", "basic_salary", "allowances", "deductions", "overtime_pay", "bonuses", "gross_salary", "net_salary"};

        conf.viewRecords(query, headers, columns);
    }

    public void updatePayslip() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        int payslipId;
        while (true) {
            System.out.print("Enter the Payslip ID to update (1 and above): ");
            payslipId = getValidPositiveIntInput(sc);
            if (payslipId >= 1) {
                break;
            }
            System.out.println("Invalid Payslip ID. It must be 1 or above.");
        }

        // Check if the Payslip ID exists
        String checkPayslipQuery = "SELECT COUNT(*) FROM tbl_gpayslip WHERE p_id = ?";
        if (!conf.recordExists(checkPayslipQuery, payslipId)) {
            System.out.println("Payslip ID does not exist. Update operation canceled.");
            return;
        }

        System.out.print("Enter new Basic Salary: ");
        double basicSalary = getValidPositiveDoubleInput(sc);

        System.out.print("Enter new Allowances: ");
        double allowances = getValidPositiveDoubleInput(sc);

        System.out.print("Enter new Deductions: ");
        double deductions = getValidNonNegativeDoubleInput(sc);

        System.out.print("Enter new Overtime Pay: ");
        double overtimePay = getValidPositiveDoubleInput(sc);

        System.out.print("Enter new Bonuses: ");
        double bonuses = getValidPositiveDoubleInput(sc);

        double grossSalary = basicSalary + allowances + overtimePay + bonuses;

        double netSalary = calculateNetSalary(grossSalary, deductions);

        String sql = "UPDATE tbl_gpayslip SET basic_salary = ?, allowances = ?, deductions = ?, overtime_pay = ?, bonuses = ?, gross_salary = ?, net_salary = ? WHERE p_id = ?";
        conf.updateRecord(sql, basicSalary, allowances, deductions, overtimePay, bonuses, grossSalary, netSalary, payslipId);

        System.out.println("Payslip updated successfully!");
    }

    public void deletePayslip() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter the Payslip ID to delete: ");
        int payslipId = getValidPositiveIntInput(sc);

        String sql = "DELETE FROM tbl_gpayslip WHERE p_id = ?";
        conf.deleteRecord(sql, payslipId);

        System.out.println("Payslip deleted successfully!");
    }

    private double calculateNetSalary(double grossSalary, double deductions) {
        return grossSalary - deductions;
    }

    private int getValidIntInput(Scanner sc, int min, int max) {
        int value = -1;
        while (true) {
            try {
                value = sc.nextInt();
                if (value >= min && value <= max) break;
                System.out.print("Invalid choice. Please enter a number between " + min + " and " + max + ": ");
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                sc.next();
            }
        }
        return value;
    }

    private int getValidPositiveIntInput(Scanner sc) {
        int value = -1;
        while (true) {
            try {
                value = sc.nextInt();
                if (value > 0) break;
                System.out.print("Invalid input. Please enter a positive number: ");
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                sc.next();
            }
        }
        return value;
    }

    private double getValidPositiveDoubleInput(Scanner sc) {
        double value = -1;
        while (true) {
            try {
                value = sc.nextDouble();
                if (value > 0) break;
                System.out.print("Invalid input. Please enter a positive number: ");
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                sc.next();
            }
        }
        return value;
    }

    private double getValidNonNegativeDoubleInput(Scanner sc) {
        double value = -1;
        while (true) {
            try {
                value = sc.nextDouble();
                if (value >= 0) break;
                System.out.print("Invalid input. Please enter a non-negative number: ");
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                sc.next();
            }
        }
        return value;
    }
}
