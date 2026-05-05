package tavern.ui;

import tavern.dishes.*;
import tavern.model.*;
import tavern.supplements.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TavernGUI extends JFrame {
    private Order currentOrder;
    private JTextArea orderDisplay;
    private JComboBox<String> dishComboBox;
    private JComboBox<String> supplementComboBox;
    private JList<String> dishesList;
    private DefaultListModel<String> listModel;

    private Dish[] availableDishes = {
            new Mead(),
            new Stew(),
            new Bread(),
            new Cheese()
    };

    private String[] availableSupplements = {"Мед (+3)", "Специи (+5)", "Сливки (+4)"};

    public TavernGUI() {
        setTitle("Гарцующая Кобыла - Система заказов");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        currentOrder = new Order();

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        JPanel createPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        createPanel.setBorder(new TitledBorder("1. Создать новый заказ"));

        JButton newOrderButton = new JButton("Новый заказ");
        newOrderButton.addActionListener(e -> createNewOrder());
        createPanel.add(newOrderButton);

        JPanel dishPanel = new JPanel(new FlowLayout());
        dishPanel.add(new JLabel("Выберите блюдо:"));
        dishComboBox = new JComboBox<>();
        for (Dish dish : availableDishes) {
            dishComboBox.addItem(String.format("%s - %.2f золотых",
                    dish.getName(), dish.getPrice()));
        }
        dishPanel.add(dishComboBox);

        JButton addDishButton = new JButton("Добавить блюдо");
        addDishButton.addActionListener(e -> addDishToOrder());
        dishPanel.add(addDishButton);

        createPanel.add(dishPanel);
        JPanel supplementPanel = new JPanel(new FlowLayout());
        supplementPanel.add(new JLabel("Добавка:"));
        supplementComboBox = new JComboBox<>(availableSupplements);
        supplementPanel.add(supplementComboBox);

        JButton addSupplementButton = new JButton("Добавить к выбранному");
        addSupplementButton.addActionListener(e -> addSupplementToSelectedDish());
        supplementPanel.add(addSupplementButton);

        createPanel.add(supplementPanel);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(new TitledBorder("2. Блюда в заказе"));

        listModel = new DefaultListModel<>();
        dishesList = new JList<>(listModel);
        dishesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(dishesList);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel listControlPanel = new JPanel(new FlowLayout());

        JButton modifyButton = new JButton("Изменить");
        modifyButton.addActionListener(e -> modifySelectedDish());
        listControlPanel.add(modifyButton);

        JButton removeButton = new JButton("Удалить");
        removeButton.addActionListener(e -> removeSelectedDish());
        listControlPanel.add(removeButton);

        listPanel.add(listControlPanel, BorderLayout.SOUTH);

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(new TitledBorder("3. Информация о заказе"));

        orderDisplay = new JTextArea();
        orderDisplay.setEditable(false);
        orderDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane displayScrollPane = new JScrollPane(orderDisplay);
        displayPanel.add(displayScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton approveButton = new JButton(" Утвердить заказ");
        approveButton.addActionListener(e -> approveOrder());
        buttonPanel.add(approveButton);

        JButton printButton = new JButton(" Печать чека");
        printButton.addActionListener(e -> printReceipt());
        buttonPanel.add(printButton);

        displayPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(createPanel);
        mainPanel.add(listPanel);
        mainPanel.add(displayPanel);

        add(mainPanel);

        updateOrderDisplay();
    }

    private void layoutComponents() {
    }

    private void createNewOrder() {
        currentOrder = new Order();
        listModel.clear();
        updateOrderDisplay();
        JOptionPane.showMessageDialog(this,
                "Создан новый заказ №" + currentOrder.getOrderId(),
                "Новый заказ",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void addDishToOrder() {
        int selectedIndex = dishComboBox.getSelectedIndex();
        if (selectedIndex >= 0) {
            Dish dish = availableDishes[selectedIndex];
            currentOrder.addDish(dish);
            listModel.addElement(formatDishString(dish, currentOrder.getDishes().size() - 1));
            updateOrderDisplay();
        }
    }

    private void addSupplementToSelectedDish() {
        int selectedIndex = dishesList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите блюдо из списка",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplementIndex = supplementComboBox.getSelectedIndex();
        Dish currentDish = currentOrder.getDishes().get(selectedIndex);

        try {
            Dish decoratedDish = currentDish;
            switch (supplementIndex) {
                case 0: decoratedDish = new Honey(currentDish); break;
                case 1: decoratedDish = new Spices(currentDish); break;
                case 2: decoratedDish = new Cream(currentDish); break;
            }

            currentOrder.modifyDish(selectedIndex, decoratedDish);
            listModel.set(selectedIndex, formatDishString(decoratedDish, selectedIndex));
            updateOrderDisplay();

            JOptionPane.showMessageDialog(this,
                    "Добавлено",
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            // Ловим нашу проверку лимита и показываем пользователю
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Лимит добавок",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modifySelectedDish() {
        int selectedIndex = dishesList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите блюдо для изменения",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int dishIndex = dishComboBox.getSelectedIndex();
        if (dishIndex >= 0) {
            Dish newDish = availableDishes[dishIndex];
            currentOrder.modifyDish(selectedIndex, newDish);
            listModel.set(selectedIndex, formatDishString(newDish, selectedIndex));
            updateOrderDisplay();
        }
    }

    private void removeSelectedDish() {
        int selectedIndex = dishesList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите блюдо для удаления",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentOrder.removeDish(selectedIndex);
        rebuildListModel();
        updateOrderDisplay();
    }

    private void approveOrder() {
        if (currentOrder.getDishes().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Заказ пуст. Добавьте блюда",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentOrder.approve();
        updateOrderDisplay();

        JOptionPane.showMessageDialog(this,
                "Заказ №" + currentOrder.getOrderId() + " подтвержден\n" +
                        "К оплате: " + String.format("%.2f", currentOrder.getTotalPrice()) + " золотых",
                "Заказ подвержден",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void printReceipt() {
        if (currentOrder.getDishes().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Заказ пуст",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextArea receiptArea = new JTextArea(currentOrder.toString());
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JOptionPane.showMessageDialog(this,
                new JScrollPane(receiptArea),
                "Чек заказа №" + currentOrder.getOrderId(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatDishString(Dish dish, int index) {
        return String.format("%d. %s - %.2f з.", index + 1, dish.getDisplayName(), dish.getPrice());
    }

    private void updateOrderDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("ЗАКАЗ №").append(currentOrder.getOrderId()).append("\n");
        sb.append("Время: ").append(currentOrder.getCreationTime()).append("\n\n");

        if (currentOrder.getDishes().isEmpty()) {
            sb.append("Заказ пуст");
        } else {
            for (Dish dish : currentOrder.getDishes()) {
                sb.append(String.format("• %s - %.2f з.\n", dish.getDisplayName(), dish.getPrice()));
            }
            sb.append(String.format("\nИТОГО: %.2f золотых", currentOrder.getTotalPrice()));
        }

        if (currentOrder.isApproved()) {
            sb.append("\n\n ПОДВЕРЖДЕН");
        }

        orderDisplay.setText(sb.toString());
    }
    private void rebuildListModel() {
        listModel.clear();
        for (int i = 0; i < currentOrder.getDishes().size(); i++) {
            listModel.addElement(formatDishString(currentOrder.getDishes().get(i), i));
        }
    }

}