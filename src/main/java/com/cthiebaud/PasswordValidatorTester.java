package com.cthiebaud;

import static com.cthiebaud.AsciiColors.*;

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

/**
 * A utility class for testing `PasswordValidator` implementations.
 * 
 * <p>
 * This class dynamically loads a JAR file containing a custom implementation
 * of the {@link PasswordValidator} interface, validates passwords, and
 * provides interactive user input via the terminal.
 * </p>
 * 
 * <p>
 * The application expects the path to the JAR file as a command-line
 * argument and ensures exactly one implementation of {@link PasswordValidator}
 * is found in the JAR. If the `prompt()` method is implemented, it will be used
 * to display custom prompts; otherwise, a default prompt is shown.
 * </p>
 * 
 * <p>
 * Password validation results are displayed interactively in the terminal.
 * </p>
 * 
 * @author Christophe Thiebaud
 */
public class PasswordValidatorTester {

    /**
     * Default constructor.
     * <p>
     * This constructor is explicitly defined to satisfy Javadoc requirements.
     * </p>
     */
    PasswordValidatorTester() {
    }

    /**
     * The main entry point for the PasswordValidatorTester application.
     *
     * @param args Command-line arguments. The first argument should be the path
     *             to the JAR file containing the PasswordValidator implementation.
     * @throws Exception If an error occurs during JAR loading or class
     *                   instantiation.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println(RED + "Argument missing: <path-to-the-jar-containing-your-implementation>" + RESET);
            return;
        }

        String jarPath = args[0]; // Path to the student's JAR file

        // Load the student's JAR file
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            System.out.println(RED + "JAR file not found!" + RESET);
            return;
        }

        // Find classes implementing PasswordValidator
        List<Class<?>> validatorClasses = findPasswordValidatorClasses(jarFile);

        // Ensure exactly one implementation is found
        if (validatorClasses.size() != 1) {
            System.out.println(RED + "Error: Expected exactly one implementation of PasswordValidator, found: "
                    + validatorClasses.size() + RESET);
            return;
        }

        // Use the found implementation
        Class<?> clazz = validatorClasses.get(0);

        /* test with null: can throw IllegalArgumentException, or be invalid */
        try {
            // Create an instance of the student's implementation
            PasswordValidator nullValidator = (PasswordValidator) clazz.getDeclaredConstructor().newInstance();
            ValidationResult nullParameter = nullValidator.validate(null);
            if (nullParameter.isValid()) {
                throw new Exception("null parameter is not valid");
            }
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException)) {
                System.out.println(YELLOW
                        + "Null password should throw an IllegalArgumentException or return an invalid result" + RESET);
            }
        }

        // Create an instance of the student's implementation
        PasswordValidator validator = (PasswordValidator) clazz.getDeclaredConstructor().newInstance();

        // Check if the `prompt()` method exists
        Method promptMethod = null;
        try {
            promptMethod = clazz.getMethod("prompt");
        } catch (NoSuchMethodException e) {
            System.out.println(YELLOW + "Warning: `prompt()` method not found. Using default prompt." + RESET);
        }

        // Initialize the terminal and use it with try-with-resources
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

            // Loop to validate passwords
            while (true) {

                String prompt = getPrompt(validator, promptMethod);
                String password = readPasswordWithAsterisks(lineReader, prompt);

                // Check if the user wants to quit
                if ("quit".equalsIgnoreCase(password)) {
                    System.out.println(PURPLE + "Exiting the program." + RESET);
                    break;
                }

                // Validate the password using the student's implementation
                ValidationResult result = validator.validate(password);

                // Display the result
                boolean messageNullOrEmpty = result.message() == null || result.message().isBlank();
                if (result.isValid()) {
                    if (!messageNullOrEmpty) {
                        System.out.println(GREEN + result.message() + RESET);
                    }
                    printBigOK();
                    break;
                } else {
                    System.out.println(RED + (messageNullOrEmpty ? "😖" : result.message()) + RESET);
                }
            }
        } catch (IOException e) {
            System.out.println(RED + "Error initializing terminal: " + e.getMessage() + RESET);
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
        System.out.println(GREEN + """
                  ______    __  ___  __
                 /  __  ∖  |  |/  / |  |
                |  |  |  | |  '  /  |  |
                |  |  |  | |    <   |  |
                |  `--'  | |  .  ∖  |__|
                 ∖______/  |__|∖__∖ (__)

                """ + RESET);
    }

    /**
     * Reads a password from the console, echoing '*' for each character typed.
     * 
     * @param lineReader The LineReader instance to use for reading input
     * @param prompt     The prompt to display
     * @return the password entered by the user
     */
    private static String readPasswordWithAsterisks(LineReader lineReader, String prompt) {
        try {
            return lineReader.readLine(prompt, '*');
        } catch (/* CTRL-C */ org.jline.reader.UserInterruptException |
        /* ........ CTRL-D */ org.jline.reader.EndOfFileException e) {
            System.out.println(PURPLE + "Operation interrupted by user. Exiting gracefully." + RESET);
            System.exit(-1);
            return null;
        }
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
        String prompt = "Enter a password to validate (or type 'quit' to exit): ";
        if (promptMethod != null) {
            try {
                String p = (String) promptMethod.invoke(validator);
                if (p != null && !p.isBlank()) {
                    prompt = p;
                }
            } catch (Exception e) {
                // System.err.println("Error invoking `prompt()` method. Using default
                // prompt.");
            }
        }
        return BLUE + prompt + RESET;
    }

}
