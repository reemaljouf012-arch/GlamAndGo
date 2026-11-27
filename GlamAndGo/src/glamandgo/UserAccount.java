
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
                new Account(
                        "Admin",
                        "admin@glamandgo.com",
                        "admin123"
                )
        );
    }

    // ===================== Register =====================

    public static boolean register(
            String name,
            String email,
            String password) {

        if (name == null || email == null || password == null) {
            return false;
        }

        String cleanName = name.trim();
        String key = email.trim().toLowerCase();

        if (cleanName.isEmpty()
                || key.isEmpty()
                || password.isEmpty()
                || accounts.containsKey(key)) {
            return false;
        }

        accounts.put(
                key,
                new Account(cleanName, key, password)
        );

        return true;
    }

    // ===================== Login =====================

    public static boolean login(String email, String password) {

        if (email == null || password == null) {
            return false;
        }

        String key = email.trim().toLowerCase();

        Account account = accounts.get(key);

        if (account == null || !account.password.equals(password)) {
            return false;
        }

        currentUserName = account.name;
        currentUserEmail = account.email;

        return true;
    }

    // ===================== Set Current User =====================

    public static void setCurrentUser(String email) {

        if (email == null) {
            return;
        }

        Account account = accounts.get(
                email.trim().toLowerCase()
        );

        if (account != null) {
            currentUserName = account.name;
            currentUserEmail = account.email;
        }
    }

    // ===================== Update Current User =====================

    public static boolean updateCurrentUser(
            String newName,
            String newEmail) {

        if (currentUserEmail == null
                || newName == null
                || newEmail == null) {
            return false;
        }

        String cleanName = newName.trim();
        String newKey = newEmail.trim().toLowerCase();
        String oldKey = currentUserEmail.toLowerCase();

        if (cleanName.isEmpty() || newKey.isEmpty()) {
            return false;
        }

        Account account = accounts.get(oldKey);

        if (account == null) {
            return false;
        }

        Account existingAccount = accounts.get(newKey);

        if (existingAccount != null && !newKey.equals(oldKey)) {
            return false;
        }

        accounts.remove(oldKey);

        account.name = cleanName;
        account.email = newKey;

        accounts.put(newKey, account);

        currentUserName = cleanName;
        currentUserEmail = newKey;

        return true;
    }

    // ===================== Get Current User =====================

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    // ===================== Logout =====================

    public static void logout() {
        currentUserName = null;
        currentUserEmail = null;
    }
}
