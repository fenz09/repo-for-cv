
package Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProcessBuilder {
    public static void main(String[] args) {
        // Create a single-threaded executor service
        ExecutorService servicePool = Executors.newSingleThreadExecutor();
        String os = System.getProperty("os.name").toLowerCase();
        java.lang.ProcessBuilder processBuilder;

        
        if (os.contains("win")) {
            // Windows command
            processBuilder = new java.lang.ProcessBuilder().command("cmd.exe", "/c", "ping -n 10 www.Google.com");
        } else {
            
            processBuilder = new java.lang.ProcessBuilder().command("bash", "-c", "ping -c 10 www.Google.com");
        }

        try {
            // Start the process
            Process process = processBuilder.start();
            // Create a Callable to handle the InputStream
            InputStreamHandler inputStreamHandler = new InputStreamHandler(process.getInputStream());
            // Submit the Callable to the executor service and get a Future
            Future<List<String>> future = servicePool.submit(inputStreamHandler);

            // Get the result from the Future and print it
            List<String> result = future.get();
            for (String line : result) {
                System.out.println(line);
            }

            // Wait for the process to complete and print the exit code
            int exitCode = process.waitFor();
            System.out.println("\nExited with code: " + exitCode);
        } catch (IOException | InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
        } finally {
            // Shutdown the executor service
            servicePool.shutdown();
        }
    }
}

class InputStreamHandler implements Callable<List<String>> {
    private BufferedReader reader;

    // Constructor to initialize the BufferedReader with the InputStream
    public InputStreamHandler(java.io.InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public List<String> call() throws Exception {
        List<String> result = new ArrayList<>();
        String line;
        // Read lines from the InputStream and add them to the result list
        while ((line = reader.readLine()) != null) {
            result.add(line);
        }
        return result;
    }
}
