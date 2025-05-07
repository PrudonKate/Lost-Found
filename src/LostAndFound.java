import javax.swing.*;
import java.util.ArrayList;

public class LostAndFound {

    static class Item {
        String reporter;
        String itemName;
        String description;
        String status;

        Item(String reporter, String itemName, String description, String status) {
            this.reporter = reporter;
            this.itemName = itemName;
            this.description = description;
            this.status = status;
        }

        @Override
        public String toString() {
            return status + " - " + itemName + " (by " + reporter + "): " + description;
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
                // Custom input panel
                JTextField reporterField = new JTextField();
                JTextField itemField = new JTextField();
                JTextField descField = new JTextField();

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.add(new JLabel("Name of person reporting:"));
                panel.add(reporterField);
                panel.add(new JLabel("Item name:"));
                panel.add(itemField);
                panel.add(new JLabel("Item description:"));
                panel.add(descField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Enter Item Details",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String reporter = reporterField.getText();
                    String item = itemField.getText();
                    String desc = descField.getText();
                    String status = (choice == 0) ? "Lost" : "Found";

                    items.add(new Item(reporter, item, desc, status));
                    JOptionPane.showMessageDialog(null, status + " item saved.");
                }

            } else if (choice == 2) {
                if (items.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No items reported yet.");
                } else {
                    StringBuilder sb = new StringBuilder("Items:\n");
                    for (Item item : items) {
                        sb.append(item).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());
                }

            } else {
                break;
            }
        }
    }
}