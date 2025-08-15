package Utils;
import java.util.function.Supplier;

public class RetryUtils {
    public static <T> T retryUntilSuccess(Supplier<T> action, int maxRetries, long delayMillis) {
        int attempts = 0;

        while (true) {
            try {
                return action.get(); // intenta ejecutar la acción
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxRetries) {
                    throw e; // relanza la excepción después del último intento
                }

                System.out.println("Reintento " + attempts + " tras error: " + e.getMessage());

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // buena práctica
                    throw new RuntimeException("Thread interrumpido durante reintento", ie);
                }
            }
        }
    }
}
