import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Main implements ActionListener {

    public class Answer {
        public int result = 0;
        public String errorType;


        public Answer() {
        }
        public Answer(int result, String errorType) {
            this.result = result;
            this.errorType = errorType;
        }
    }


    private State state = State.NULL;


    private enum State{
        NULL,
        NUM1,
        OPERATOR,
        NUM2,


    }


    //Declarations

    int answer, num1,num2;


    private String problem ="";
    private static final String operators = "[+\\-*/]";
    private String operator;
    private JLabel label;
    private JFrame frame;
    private JPanel panel;


    //main method
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        System.out.println(state);
        frame = new JFrame();
        createLabel();
        createPanel();
        createButton();

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Calculator");
        frame.pack();
        frame.setSize(400,600);
        frame.setBackground(new Color(40,40,40));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    private void createLabel(){
        label = new JLabel("");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        label.setBackground(new Color(40, 40, 40)); // light gray background
        label.setForeground(Color.WHITE);             // white text
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(200,50));
        label.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1)); // subtle border

    }
    private void createPanel(){
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(6, 4, 4,4));
        panel.setBackground(new Color(40, 40, 40));  // 🔹 dark gray background
        panel.add(label);
    }
    private JButton createJButton(String text){
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setActionCommand(text);

        // 🔹 Dark theme style
        button.setBackground(new Color(60, 60, 60));   // dark gray background
        button.setForeground(Color.WHITE);             // white text
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);                 // remove focus outline
        button.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        return button;
    }

    private void createButton(){

        // Operators
        panel.add(createJButton("="));
        panel.add(createJButton("CE"));
        panel.add(createJButton("/"));
        panel.add(createJButton("*"));
        panel.add(createJButton("-"));
        panel.add(createJButton("+"));

        // Numbers
        panel.add(createJButton("1"));
        panel.add(createJButton("2"));
        panel.add(createJButton("3"));
        panel.add(createJButton("4"));
        panel.add(createJButton("5"));
        panel.add(createJButton("6"));
        panel.add(createJButton("7"));
        panel.add(createJButton("8"));
        panel.add(createJButton("9"));
        panel.add(createJButton("0"));


    }



    //Actions for buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (state){
            case NULL:
                if (command.matches("[0-9]")){
                    problem+=command;
                    label.setText(problem);
                    //Changes the state to num1
                    state = State.NUM1;
                    System.out.println(state);
                } else if (command.equals("CE")) {
                    problem = "";
                    label.setText("");
                }
                break;
            case NUM1:
                if (command.matches("[0-9]")){
                    problem+=command;
                    label.setText(problem);
                } else if (command.matches(operators)) {
                    problem+=command;
                    label.setText(problem);
                    operator = command;
                    try {
                        num1 = Integer.parseInt(problem.substring(0, problem.length() - 1));
                        System.out.println("num1: " + num1);
                        state = State.OPERATOR;
                    } catch (NumberFormatException ex) {
                        System.out.println("Number too large or invalid: " + ex.getMessage());
                        problem = "";
                        label.setText("Error: number too large");
                        state = State.NULL;
                    }



                }else if (command.equals("CE")) {
                    problem = "";
                    state = State.NULL;
                    label.setText("");
                }
                break;
            case OPERATOR:
                if (command.matches(operators)) {
                    operator = command;
                    problem = problem.substring(0,problem.length()-1) + command;
                    label.setText(problem);

                } else if (command.matches("[0-9]")){
                    problem+=command;
                    label.setText(problem);
                    state = State.NUM2;
                    System.out.println(state);
                }else if (command.equals("CE")) {
                    problem = "";
                    state = State.NULL;
                    label.setText("");
                }
                break;
            case NUM2:
                if (command.matches("[0-9]")){
                    problem+=command;
                    label.setText(problem);

                } else if (command.equals("CE")) {
                    problem = "";
                    state = State.NULL;
                    label.setText("");
                } else if (command.equals("=")) {
                    try {
                        num2 = Integer.parseInt(problem.substring(problem.indexOf(operator) + 1));
                        System.out.println("num2: " + num2);
                    }catch (NumberFormatException ex) {
                        System.out.println("Number too large or invalid: " + ex.getMessage());
                        problem = "";
                        label.setText("Error: number too large");
                        state = State.NULL;
                    }
                      Answer answer = Calculate(operator,num1,num2);
                    switch (answer.errorType){
                        case "Error":
                            label.setText("Error");
                            state = State.NULL;
                            break;
                        case "Answer too big":
                            label.setText("Answer too big");
                            state = State.NULL;
                            break;
                        default:
                            label.setText(String.valueOf(answer.result));
                            state = State.NUM1;

                    }

                }
        }

        label.repaint();
    }

    public Answer Calculate(String operator, int num1, int num2){
        Answer answer1 = new Answer();
        switch (operator){
            case "+":
                try{
                    answer1.result = num1 + num2;
                    problem = String.valueOf(answer1.result);
                    answer1.errorType ="";
                }catch (NumberFormatException ex ){
                    answer1.result = 0;
                    answer1.errorType = "Answer too big";

                }

                state = State.NUM1;
                break;
            case "-":
                try{
                    answer1.result = num1 - num2;
                    problem = String.valueOf(answer1.result);
                    answer1.errorType ="";
                }catch (NumberFormatException ex ){
                    answer1.result = 0;
                    answer1.errorType = "Answer too big";

                }
                state = State.NUM1;
                break;
            case "*":
                try{
                    answer1.result = num1 * num2;
                    problem = String.valueOf(answer1.result);
                    answer1.errorType ="";
                }catch (NumberFormatException ex ){
                    answer1.result = 0;
                    answer1.errorType = "Answer too big";
// tady to nejak zprznim
                    // uplny error
                }
                state = State.NUM1;
                break;
            case "/":

                if (num2 != 0){
                    answer1.result = num1 / num2;
                    answer1.errorType ="";
                    problem = String.valueOf(answer1.result);
                    state = State.NUM1;
                } else {
                    answer1.result = 0;
                    answer1.errorType ="Error";
                    problem = "";
                    state = State.NULL;
                    return answer1;
                }
                break;

        }
        return answer1;
    }


}
