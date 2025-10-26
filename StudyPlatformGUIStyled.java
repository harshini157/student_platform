import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

// --- StudyMaterial class ---
class StudyMaterial {
    private String title;
    private String subject;
    private String filePath;

    public StudyMaterial(String title, String subject, String filePath){
        this.title = title;
        this.subject = subject;
        this.filePath = filePath;
    }

    public String getTitle(){ return title; }
    public String getSubject(){ return subject; }
    public String getFilePath(){ return filePath; }
}

// --- Task class ---
class Task {
    private String title;
    private String deadline;
    private boolean completed;

    public Task(String title, String deadline){
        this.title = title;
        this.deadline = deadline;
        this.completed = false;
    }

    public void markCompleted(){ this.completed = true; }
    public String getTitle(){ return title; }
    public String getDeadline(){ return deadline; }
    public boolean isCompleted(){ return completed; }
}

// --- Main GUI Class ---
public class StudyPlatformGUIStyled {
    private java.util.List<StudyMaterial> materials = new ArrayList<>();
    private java.util.List<Task> tasks = new ArrayList<>();

    private JFrame frame;
    private DefaultListModel<String> materialListModel = new DefaultListModel<>();
    private DefaultListModel<String> taskListModel = new DefaultListModel<>();
    private JList<String> materialList = new JList<>(materialListModel);
    private JList<String> taskList = new JList<>(taskListModel);

    public StudyPlatformGUIStyled() {
        // --- Frame ---
        frame = new JFrame("📚 Personal Study Platform");
        frame.setSize(900, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1,2,20,10));
        frame.getContentPane().setBackground(new Color(245,245,245));

        // --- Left Panel: Study Materials ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(15,15));
        leftPanel.setBackground(new Color(230,245,255));
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,2),
                "📘 Study Materials", 0,0, new Font("Segoe UI", Font.BOLD, 18)));

        JPanel inputPanel = new JPanel(new GridLayout(4,1,10,10));
        inputPanel.setBackground(new Color(230,245,255));
        JTextField titleField = new JTextField();
        titleField.setBorder(BorderFactory.createTitledBorder("Title"));
        JTextField subjectField = new JTextField();
        subjectField.setBorder(BorderFactory.createTitledBorder("Subject"));
        JTextField pathField = new JTextField();
        pathField.setBorder(BorderFactory.createTitledBorder("PDF File Path"));
        JButton addMaterialBtn = new JButton("Add Material");
        addMaterialBtn.setBackground(new Color(0,123,255));
        addMaterialBtn.setForeground(Color.WHITE);
        addMaterialBtn.setFocusPainted(false);
        addMaterialBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        inputPanel.add(titleField);
        inputPanel.add(subjectField);
        inputPanel.add(pathField);
        inputPanel.add(addMaterialBtn);

        leftPanel.add(inputPanel, BorderLayout.NORTH);

        materialList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        materialList.setCellRenderer(new MaterialListRenderer());
        leftPanel.add(new JScrollPane(materialList), BorderLayout.CENTER);

        // Add material action
        addMaterialBtn.addActionListener(e -> {
            String title = titleField.getText();
            String subject = subjectField.getText();
            String path = pathField.getText();
            if(!title.isEmpty() && !subject.isEmpty() && !path.isEmpty()){
                StudyMaterial sm = new StudyMaterial(title, subject, path);
                materials.add(sm);
                materialListModel.addElement(title + " - " + subject);
                titleField.setText(""); subjectField.setText(""); pathField.setText("");
            }
        });

        // Double-click to open PDF
        materialList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(evt.getClickCount() == 2){
                    int index = materialList.getSelectedIndex();
                    if(index != -1){
                        StudyMaterial sm = materials.get(index);
                        try {
                            File file = new File(sm.getFilePath());
                            if(file.exists()){
                                Desktop.getDesktop().open(file);
                            } else {
                                JOptionPane.showMessageDialog(frame, "File not found!");
                            }
                        } catch(Exception ex){
                            JOptionPane.showMessageDialog(frame, "Cannot open file!");
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // --- Right Panel: Tasks ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(15,15));
        rightPanel.setBackground(new Color(255,245,220));
        rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,2),
                "📝 Tasks", 0,0, new Font("Segoe UI", Font.BOLD, 18)));

        JPanel taskInputPanel = new JPanel(new GridLayout(3,1,10,10));
        taskInputPanel.setBackground(new Color(255,245,220));
        JTextField taskTitleField = new JTextField();
        taskTitleField.setBorder(BorderFactory.createTitledBorder("Task Title"));
        JTextField deadlineField = new JTextField();
        deadlineField.setBorder(BorderFactory.createTitledBorder("Deadline"));
        JButton addTaskBtn = new JButton("Add Task");
        addTaskBtn.setBackground(new Color(40,167,69));
        addTaskBtn.setForeground(Color.WHITE);
        addTaskBtn.setFocusPainted(false);
        addTaskBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        taskInputPanel.add(taskTitleField);
        taskInputPanel.add(deadlineField);
        taskInputPanel.add(addTaskBtn);

        rightPanel.add(taskInputPanel, BorderLayout.NORTH);

        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taskList.setCellRenderer(new TaskListRenderer());
        rightPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Add task action
        addTaskBtn.addActionListener(e -> {
            String title = taskTitleField.getText();
            String deadline = deadlineField.getText();
            if(!title.isEmpty() && !deadline.isEmpty()){
                Task t = new Task(title, deadline);
                tasks.add(t);
                taskListModel.addElement(title + " - " + deadline + " (Incomplete)");
                taskTitleField.setText(""); deadlineField.setText("");
            }
        });

        // Double-click task to complete
        taskList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt){
                if(evt.getClickCount() == 2){
                    int index = taskList.getSelectedIndex();
                    if(index != -1){
                        tasks.get(index).markCompleted();
                        taskListModel.set(index, "✅ " + tasks.get(index).getTitle() + " - " +
                                tasks.get(index).getDeadline() + " (Completed)");
                    }
                }
            }
        });

        frame.add(leftPanel);
        frame.add(rightPanel);
        frame.setVisible(true);
    }

    // Custom renderer for materials (like cards)
    static class MaterialListRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus){
            JLabel label = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
            label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            label.setOpaque(true);
            label.setBackground(new Color(200,230,255));
            if(isSelected){
                label.setBackground(new Color(0,123,255));
                label.setForeground(Color.WHITE);
            } else {
                label.setForeground(Color.BLACK);
            }
            return label;
        }
    }

    // Custom renderer for tasks (like cards)
    static class TaskListRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus){
            JLabel label = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
            label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            label.setOpaque(true);
            label.setBackground(new Color(255,230,200));
            if(isSelected){
                label.setBackground(new Color(40,167,69));
                label.setForeground(Color.WHITE);
            } else {
                label.setForeground(Color.BLACK);
            }
            return label;
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new StudyPlatformGUIStyled());
    }
}
