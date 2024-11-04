package capstoneProject;


public class ExceptionHandler {
    
    public ExceptionHandler() {}

    // c. Resource Management
    public void resourceManagement() {
        try (java.io.FileReader fr = new java.io.FileReader("file.txt")) {
            // Code to read from the file
        } catch (java.io.IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }
    }

    // d. Chaining Exceptions
    public void chainingExceptions() throws Exception {
        try {
            throw new RuntimeException("Runtime Exception");
        } catch (RuntimeException e) {
            throw new Exception("Chained Exception", e); // Chaining the exception
        }
    }
}

