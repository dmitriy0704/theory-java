package dev.folomkin.pro.qa;

import java.util.ArrayList;
import java.util.List;

class Person {
}

class Employee extends Person {
}

class Manager extends Employee {
}

public class Demo {
    public static void main(String[] args) {

        List<Person> persons = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        List<Manager> managers = new ArrayList<>();

        processUpperBounded(persons);
        processUpperBounded(employees);
        processUpperBounded(managers);


        processLowerBounded(persons);
        processLowerBounded(employees);
        processLowerBounded(managers);


    }

    public static void processUpperBounded(List<? extends Employee> employees) {
        List<Employee> upperBoundedEmployees = new ArrayList<>();
        Person person = upperBoundedEmployees.get(0);
        Employee employee = upperBoundedEmployees.get(0);
        Manager manager = upperBoundedEmployees.get(0);
    }

    public static void processLowerBounded(List<? super Employee> employees) {
        employees.add(new Person()); //compilation error
        employees.add(new Employee());
        employees.add(new Manager());
        Object employee = employees.getFirst();
        if (employee instanceof Employee) {
            Employee myEmployee = (Employee) employee;
        }
        Employee employee = employees.getFirst();//compilation error
    }
}

