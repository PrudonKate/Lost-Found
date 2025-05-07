import javax.swing.*;
import java.util.ArrayList;

public class LostAndFound {

    static class Item {
        String name;
        String description;
        String status;

        Item(String name, String description, String status) {
            this.name = name;
            this.description = description;
            this.status = status;
        }

        @Override
        public String toString() {
            return status + " - " + name + ": " + description;
        }
    }

    public static void main(String[] args) {
        ArrayList<Item> items = new ArrayList<>();
        String[] options = {"Report Lost Item", "Report Found Item", "View All Items", "Exit"};

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Welcome to the Lost and Found System",
                    "Lost and Found",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0 || choice == 1) {
                String name = JOptionPane.showInputDialog("Enter item name:");
                if (name == null) continue;

                String description = JOptionPane.showInputDialog("Enter item description:");
                if (description == null) continue;

                String status = (choice == 0) ? "Lost" : "Found";
                items.add(new Item(name, description, status));
                JOptionPane.showMessageDialog(null, status + " item added!");
            } else if (choice == 2) {
                if (items.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No items reported yet.");
                } else {
                    StringBuilder message = new StringBuilder("Items:\n");
                    for (Item item : items) {
                        message.append(item).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, message.toString());
                }
            } else {
                break;
            }
        }
    }
}
