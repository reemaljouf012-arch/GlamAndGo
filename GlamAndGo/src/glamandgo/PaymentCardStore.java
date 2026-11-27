
package glamandgo;

import java.util.ArrayList;
import java.util.List;

public class PaymentCardStore {

    private static final ArrayList<SavedCard> savedCards =
            new ArrayList<>();

    private PaymentCardStore() {
        // Prevent creating instances of this class.
    }

    // ===================== Saved Card =====================

    public static class SavedCard {

        public final String holderName;
        public final String cardType;
        public final String lastFour;
        public final String expiryDate;

        public SavedCard(
                String holderName,
                String cardType,
                String lastFour,
                String expiryDate) {

            this.holderName = holderName;
            this.cardType = cardType;
            this.lastFour = lastFour;
            this.expiryDate = expiryDate;
        }
    }

    // ===================== Get Saved Cards =====================

    public static List<SavedCard> getSavedCards() {
        return new ArrayList<>(savedCards);
    }

    // ===================== Add Card =====================

    public static void addCard(SavedCard card) {

        if (card != null) {
            savedCards.add(card);
        }
    }

    // ===================== Remove Card =====================

    public static void removeCard(SavedCard card) {
        savedCards.remove(card);
    }

    // ===================== Check Card =====================

    public static boolean containsCard(SavedCard card) {
        return savedCards.contains(card);
    }

    // ===================== Check Empty =====================

    public static boolean hasCards() {
        return !savedCards.isEmpty();
    }
}
