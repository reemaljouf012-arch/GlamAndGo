
package glamandgo;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserAccount {

    private static class Account {
        String name;
        String email;
        String password;

        Account(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }

    private static final Map<String, Account> accounts =
            new LinkedHashMap<>();

    private static String currentUserName;
    private static String currentUserEmail;

    static {
        accounts.put(
                "admin@glamandgo.com",
                new Account("Admin", "admin@glamandgo.com", "admin123")
        );
    }

    public static boolean register(
            String name, String email, String password) {

        String key = email.trim().toLowerCase();

        if (accounts.containsKey(key)) {
            return false;
        }

        accounts.put(
                key,
                new Account(name.trim(), key, password)
        );

        return true;
    }

    public static boolean login(String email, String password) {

        String key = email.trim().toLowerCase();

        Account account = accounts.get(key);

        if (account == null || !account.password.equals(password)) {
            return false;
        }

        currentUserName = account.name;
        currentUserEmail = account.email;

        return true;
    }

    public static void setCurrentUser(String email) {

        Account account = accounts.get(email.trim().toLowerCase());

        if (account != null) {
            currentUserName = account.name;
            currentUserEmail = account.email;
        }
    }

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public static void logout() {
        currentUserName = null;
        currentUserEmail = null;
    }
}
