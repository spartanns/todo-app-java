import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TodoListApp {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(TodoListFrame::new);
  }
}

class TodoListFrame extends JFrame {
  private DefaultListModel<String> todoListModel;
  private JList<String> todoList;
  private JTextField taskField;
  private static final String FILENAME = "todos.txt";

  public TodoListFrame() {
    setTitle("TODO App");
    setSize(400, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    todoListModel = new DefaultListModel<>();
    loadTasks();

    todoList = new JList<>(todoListModel);
    todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    add(new JScrollPane(todoList), BorderLayout.CENTER);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    taskField = new JTextField();
    panel.add(taskField, BorderLayout.CENTER);

    JButton addButton = new JButton("Add Task");
    addButton.addActionListener(new AddTaskListener());
    panel.add(addButton, BorderLayout.EAST);

    add(panel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

    JButton completeButton = new JButton("Mark as Done");
    completeButton.addActionListener(new CompleteTaskListener());
    buttonPanel.add(completeButton);

    JButton removeButton = new JButton("Remove Selected");
    removeButton.addActionListener(new RemoveTaskListener());
    buttonPanel.add(removeButton);

    add(buttonPanel, BorderLayout.SOUTH);

    setVisible(true);
  }

  private class AddTaskListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String task = taskField.getText().trim();
      if (!task.isEmpty()) {
        todoListModel.addElement(task);
        taskField.setText("");
        saveTasks();
      }
    }
  }

  private class RemoveTaskListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      int selectedIndex = todoList.getSelectedIndex();
      if (selectedIndex != -1) {
        todoListModel.remove(selectedIndex);
        saveTasks();
      }
    }
  }

  private class CompleteTaskListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      int selectedIndex = todoList.getSelectedIndex();
      if (selectedIndex != -1) {
        String task = todoListModel.getElementAt(selectedIndex);
        if (!task.startsWith("[X] ")) {
          todoListModel.set(selectedIndex, "[X] " + task);
          saveTasks();
        }
      }
    }
  }

  private void saveTasks() {
    try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
      for (int i = 0; i < todoListModel.size(); i++) {
        writer.println(todoListModel.get(i));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadTasks() {
    File file = new File(FILENAME);
    if (file.exists()) {
      try (Scanner scanner = new Scanner(file)) {
        while (scanner.hasNextLine()) {
          todoListModel.addElement(scanner.nextLine());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
