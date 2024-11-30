package com.cthiebaud;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import com.cthiebaud.passwordvalidator.PasswordValidator;
import com.cthiebaud.passwordvalidator.ValidationResult;

public class PasswordValidatorTester {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Argument missing: <path-to-the-jar-containing-your-implementation>");
            return;
        }

        String jarPath = args[0]; // Path to the student's JAR file

        // Load the student's JAR file
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            System.out.println("JAR file not found!");
            return;
        }

        // Find classes implementing PasswordValidator
        List<Class<?>> validatorClasses = findPasswordValidatorClasses(jarFile);

        // Ensure exactly one implementation is found
        if (validatorClasses.size() != 1) {
            System.out.println("Error: Expected exactly one implementation of PasswordValidator, found: "
                    + validatorClasses.size());
            return;
        }

        // Use the found implementation
        Class<?> clazz = validatorClasses.get(0);

        // Create an instance of the student's implementation
        PasswordValidator validator = (PasswordValidator) clazz.getDeclaredConstructor().newInstance();

        // Check if the `prompt()` method exists
        Method promptMethod = null;
        try {
            promptMethod = clazz.getMethod("prompt");
        } catch (NoSuchMethodException e) {
            System.out.println("Warning: `prompt()` method not found. Using default prompt.");
        }

        // Initialize the terminal and use it with try-with-resources
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

            // Loop to validate passwords
            while (true) {
                String prompt = getPrompt(validator, promptMethod);
                String password = readPasswordWithAsterisks(lineReader,
                        prompt != null && !prompt.isBlank() ? prompt
                                : "Enter a password to validate (or type 'quit' to exit): ");

                // Check if the user wants to quit
                if ("quit".equalsIgnoreCase(password)) {
                    System.out.println("\nExiting the program.");
                    break;
                }

                // Validate the password using the student's implementation
                ValidationResult result = validator.validate(password);

                // Display the result
                if (result.isValid()) {
                    if (result.message() != null && !result.message().isBlank()) {
                        System.out.println(result.message());
                    }
                    printBigOK();
                    break;
                } else {
                    System.out.println("Password is invalid: " + result.message());
                }
            }
        } catch (IOException e) {
            System.out.println("Error initializing terminal: " + e.getMessage());
        }
    }

    private static List<Class<?>> findPasswordValidatorClasses(File jarFile) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        try (JarFile jar = new JarFile(jarFile)) {
            // Iterate through all entries in the JAR file
            jar.stream().forEach(entry -> {
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace("/", ".").substring(0, entry.getName().length() - 6);
                    try {
                        // Load the class
                        Class<?> clazz = Class.forName(className, false,
                                URLClassLoader.newInstance(new URL[] { jarFile.toURI().toURL() }));
                        // Check if it implements PasswordValidator and is not an interface or abstract
                        // class
                        if (PasswordValidator.class.isAssignableFrom(clazz)
                                && !clazz.isInterface()
                                && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                            classes.add(clazz);
                        }
                    } catch (Exception e) {
                        // Handle exception silently; it can occur if the class cannot be loaded
                    }
                }
            });
        }
        return classes;
    }

    private static void printBigOK() {
        System.out.println("  ______    __  ___  __  ");
        System.out.println(" /  __  \\  |  |/  / |  | ");
        System.out.println("|  |  |  | |  '  /  |  | ");
        System.out.println("|  |  |  | |    <   |  | ");
        System.out.println("|  `--'  | |  .  \\  |__| ");
        System.out.println(" \\______/  |__|\\__\\ (__) ");
        System.out.println("                         ");
    }

    /**
     * Reads a password from the console, echoing '*' for each character typed.
     * 
     * @param lineReader The LineReader instance to use for reading input
     * @param prompt     The prompt to display
     * @return the password entered by the user
     */
    private static String readPasswordWithAsterisks(LineReader lineReader, String prompt) {
        return lineReader.readLine(prompt, '*');
    }

    /**
     * Gets the prompt string from the `PasswordValidator` implementation.
     * If the `prompt()` method is not implemented, returns a default prompt.
     * 
     * @param validator    The `PasswordValidator` instance
     * @param promptMethod The `Method` object for `prompt()` or null if not
     *                     available
     * @return The prompt string
     */
    private static String getPrompt(PasswordValidator validator, Method promptMethod) {
        if (promptMethod != null) {
            try {
                return (String) promptMethod.invoke(validator);
            } catch (Exception e) {
                // System.err.println("Error invoking `prompt()` method. Using default
                // prompt.");
            }
        }
        return "(Using default prompt) Enter a password to validate (or type 'quit' to exit): ";
    }
}
