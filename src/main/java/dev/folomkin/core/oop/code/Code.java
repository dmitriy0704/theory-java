package dev.folomkin.core.oop.code;

class Student {
    private int studentId;
    private String name;


    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Address address;

    public class Address {
        private String city;
        private String street;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }
}

class Code {
    public static void main(String[] args) {
        Student.Address address = new Student().new Address();
    }
}
