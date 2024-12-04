package mylene;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Report {

    private final config dbConfig = new config();

    // Method to generate an individual report
    public void generateIndividualReport(int employeeId) {
        String employeeQuery = "SELECT e_id, fname, lname, position, department FROM tbl_employee WHERE e_id = ?";
        String payslipQuery = "SELECT p_id, basic_salary, allowances, deductions, overtime_pay, bonuses, gross_salary, net_salary FROM tbl_gpayslip WHERE e_id = ?";

        try {
            // Fetch employee data
            ResultSet employeeResult = dbConfig.getData(employeeQuery, employeeId);

            if (employeeResult.next()) {
                // Print employee details
                System.out.println("--------------- Individual Report ---------------");
                System.out.printf("Employee ID   : %d%n", employeeResult.getInt("e_id"));
                System.out.printf("First Name    : %s%n", employeeResult.getString("fname"));
                System.out.printf("Last Name     : %s%n", employeeResult.getString("lname"));
                System.out.printf("Position      : %s%n", employeeResult.getString("position"));
                System.out.printf("Department    : %s%n", employeeResult.getString("department"));

                // Fetch payslip data
                ResultSet payslipResult = dbConfig.getData(payslipQuery, employeeId);

                if (!payslipResult.isBeforeFirst()) { // No payslip data
                    System.out.println("No payslip data found for Employee ID: " + employeeId);
                } else {
                    System.out.println("\nPayslip Details:");
                    System.out.println("------------------------------------------------");
                    System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s |%n", 
                                      "Payslip ID", "Basic Salary", "Allowances", "Deductions", 
                                      "Overtime", "Bonuses", "Gross", "Net");
                    System.out.println("------------------------------------------------");

                    while (payslipResult.next()) {
                        System.out.printf("| %-10d | %-15.2f | %-10.2f | %-10.2f | %-10.2f | %-10.2f | %-10.2f | %-10.2f |%n", 
                                          payslipResult.getInt("p_id"),
                                          payslipResult.getDouble("basic_salary"),
                                          payslipResult.getDouble("allowances"),
                                          payslipResult.getDouble("deductions"),
                                          payslipResult.getDouble("overtime_pay"),
                                          payslipResult.getDouble("bonuses"),
                                          payslipResult.getDouble("gross_salary"),
                                          payslipResult.getDouble("net_salary"));
                    }
                    System.out.println("------------------------------------------------");
                }
            } else {
                System.out.println("No employee found with ID: " + employeeId);
            }
        } catch (SQLException e) {
            System.out.println("Error generating individual report: " + e.getMessage());
        }
    }

    // Method to generate a general report
    public void generateGeneralReport() {
        String employeeQuery = "SELECT e.e_id, e.fname, e.lname, e.position, e.department, "
                             + "p.basic_salary, p.net_salary "
                             + "FROM tbl_employee e LEFT JOIN tbl_gpayslip p ON e.e_id = p.e_id";

        try {
            ResultSet resultSet = dbConfig.getData(employeeQuery);

            System.out.println("----------------- General Report -----------------");
            System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |%n",
                              "Employee ID", "First Name", "Last Name", "Position", 
                              "Department", "Basic Salary", "Net Salary");
            System.out.println("---------------------------------------------------------------------------------------------");

            if (!resultSet.isBeforeFirst()) { // No data
                System.out.println("No data available for the general report.");
                return;
            }

            while (resultSet.next()) {
                System.out.printf("| %-10d | %-15s | %-15s | %-15s | %-15s | %-15.2f | %-15.2f |%n",
                                  resultSet.getInt("e_id"),
                                  resultSet.getString("fname"),
                                  resultSet.getString("lname"),
                                  resultSet.getString("position"),
                                  resultSet.getString("department"),
                                  resultSet.getDouble("basic_salary"),
                                  resultSet.getDouble("net_salary"));
            }
            System.out.println("---------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error generating general report: " + e.getMessage());
        }
    }
}
