package io.github.dylanrusselldev.utilities.logging;

/*
 * Filename: ExceptionFormatter.java
 * Author: Dylan Russell
 * Purpose: Format a Selenium exception message into a more readable format.
 *          Other exceptions will still be outputted, just not formatted.
 */
public class ExceptionFormatter {

    /**
     * Format a Selenium exception message into a more readable format.
     *
     * @param e the thrown Exception
     * @return the formatted Exception object
     */
    public static Exception formatException(Throwable e) {
        String cause = getExceptionCause(e);
        StackTraceElement[] steArr = getExceptionInfo(e);
        String name = e.getClass().getName();

        if (name.contains("NoSuchElementException")) {

            String formattedCause = cause.substring(
                            cause.indexOf("Unable to locate"),
                            cause.indexOf("(Session info: "))
                    .replaceAll("\\n", "");

            Exception ex = new Exception("org.openqa.selenium.NoSuchElementException: " + formattedCause);
            ex.setStackTrace(steArr);
            return ex;

        } else if (name.contains("ElementNotInteractableException")) {

            String formattedCause = cause.substring(
                            cause.indexOf(" -> "),
                            cause.indexOf("Session ID:"))
                    .replaceAll("\\n", "");
            Exception ex = new Exception("org.openqa.selenium.ElementNotInteractableException: element not interactable" + formattedCause);
            ex.setStackTrace(steArr);
            return ex;

        } else if (name.contains("InvalidElementStateException")) {

            String formattedCause = cause.substring(
                            cause.indexOf(" -> "),
                            cause.indexOf("Session ID:"))
                    .replaceAll("\\n", "");
            Exception ex = new Exception("org.openqa.selenium.ElementNotInteractableException: element not interactable" + formattedCause);
            ex.setStackTrace(steArr);
            return ex;

        } else {

            Exception ex = new Exception(e.getClass() + ": " + cause);
            ex.setStackTrace(steArr);
            return ex;

        }

    }

    private static StackTraceElement[] getExceptionInfo(Throwable e) {

        StackTraceElement[] arr = e.getStackTrace();
        String className = "";
        String methodName = "";
        int lineNumber = 0;

        for (StackTraceElement traceElement : arr) {
            String localClassName = traceElement.getClassName();

            if (localClassName.contains("Steps") || localClassName.contains("CommonMethods")) {
                className = localClassName;
                methodName = traceElement.getMethodName();
                lineNumber = traceElement.getLineNumber();
                break;
            } // end if

        } // end for

        StackTraceElement stackTraceElement = new StackTraceElement(className, methodName, "Line", lineNumber);
        return new StackTraceElement[]{stackTraceElement};

    }

    private static String getExceptionCause(Throwable e) {
        String cause;

        try {
            cause = e.getCause().toString();
        } catch (NullPointerException n) {
            cause = e.getMessage();
        }

        return cause;
    }

}
