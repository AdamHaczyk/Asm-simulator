import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class AssemblerSimulation {

    final private static HashMap<String, Integer> registers = new HashMap<>();
    private static String selectedSourceRegister = "WARTOŚĆ_2";
    private static String selectedTargetRegister = "WARTOŚĆ_1";
    private static String selectedInstruction = "INSTRUKCJA";
    private static String outputString = selectedInstruction + " " + selectedTargetRegister + ", " + selectedSourceRegister;
    private static String valuesDisplay;
    private static boolean isSourceRegister;

    public static void assemblerSimulation() {

        registers.put("AX", 0);
        registers.put("BX", 0);
        registers.put("CX", 0);
        registers.put("DX", 0);

        valuesDisplay = "AX: " + registers.get("AX") +
                        "\nBX: " + registers.get("BX") +
                        "\nCX: " + registers.get("CX") +
                        "\nDX: " + registers.get("DX");

        GridLayout mainFrameLayout = new GridLayout(1, 2, 0, 0);
        GridLayout codePanelLayout = new GridLayout(2, 1, 0, 0);
        GridLayout selectionPanelLayout = new GridLayout(8, 1, 0, 0);

        JFrame mainFrame = new JFrame("Projekt asm");
        mainFrame.setSize(650, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(mainFrameLayout);
        mainFrame.setResizable(false);

        //JPanels
        JPanel codePanel = new JPanel();
        codePanel.setLayout(codePanelLayout);

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(selectionPanelLayout);

        //JPopUpMenus
        JPopupMenu dstRegisterSelection = new JPopupMenu();
        JPopupMenu srcRegisterSelection = new JPopupMenu();
        JPopupMenu instructionSelection = new JPopupMenu();

        //JButtons
        JButton sourceSelectionButton = new JButton("Wybierz źródło");
        sourceSelectionButton.setComponentPopupMenu(srcRegisterSelection);

        JButton targetSelectionButton = new JButton("Wybierz rejestr docelowy");
        targetSelectionButton.setComponentPopupMenu(dstRegisterSelection);

        JButton executeButton = new JButton("Wykonaj");

        JButton instructionSelectionButton = new JButton("Wybierz instrukcję");
        instructionSelectionButton.setComponentPopupMenu(instructionSelection);

        //JTextAreas
        JTextArea codeArea = new JTextArea(outputString, 15, 10);
        codeArea.setEditable(false);
        codeArea.setSize(10, 25);

        JTextArea valuesDisplayArea = new JTextArea(15, 10);
        valuesDisplayArea.setEditable(false);
        valuesDisplayArea.setSize(10, 25);

        valuesDisplayArea.setText(valuesDisplay);

        //JTextFields
        JTextField valueInputField = new JTextField("PODAJ WARTOŚĆ");


        //ActionListeners
        sourceSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                srcRegisterSelection.show(sourceSelectionButton, 0, sourceSelectionButton.getHeight());
                isSourceRegister = true;
            }
        });

        targetSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dstRegisterSelection.show(targetSelectionButton, 0, targetSelectionButton.getHeight());
                isSourceRegister = false;
            }
        });

        instructionSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionSelection.show(instructionSelectionButton, 0, instructionSelectionButton.getHeight());
            }
        });


        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (selectedSourceRegister == "WARTOŚĆ_2" ||
                        selectedTargetRegister == "WARTOŚĆ_1" ||
                        selectedInstruction == "INSTRUKCJA") {
                    codeArea.setText(outputString + "\n#Najpierw należy wybrać rejestry i instrukcję");
                    return;
                }


                if(selectedInstruction == "MOV")
                {

                    if (selectedSourceRegister != "{PODAJ WARTOŚĆ}") {
                        movRegisterToRegister(selectedTargetRegister, selectedSourceRegister);

                        codeArea.setText(   "Przeniesiono wartość " + registers.get(selectedSourceRegister) +
                                            " z rejestru " + selectedSourceRegister + " do rejestru " +
                                            selectedTargetRegister);
                    } else {
                        boolean isExecuted = movImmediateToRegister(selectedTargetRegister, valueInputField.getText());

                        if (isExecuted) {
                            codeArea.setText(   "Przeniesiono wartość " + valueInputField.getText() +
                                                " do rejestru " + selectedTargetRegister);
                        } else {
                            valueInputField.setText("Błędne dane!");
                            return;
                        }

                    }
                }
                else if(selectedInstruction == "XCHG")
                {
                    if(selectedSourceRegister == "{PODAJ WARTOŚĆ}")
                    {
                        codeArea.setText(   outputString + "\n#Instrukcję XCHG można wykonać jedynie" +
                                            "\n#gdy wybrano rejestr źródłowy");
                        return;
                    }
                    xchg(selectedTargetRegister, selectedSourceRegister);

                    codeArea.setText(   "Zamieniono wartości w rejestrach " + selectedTargetRegister +
                                        " i " + selectedSourceRegister);
                }


                valuesDisplay = updateValuesDisplay();
                valuesDisplayArea.setText(valuesDisplay);
                valueInputField.setText("PODAJ WARTOŚĆ");
                selectedSourceRegister = "WARTOŚĆ_2";
                selectedTargetRegister = "WARTOŚĆ_1";
                selectedInstruction = "INSTRUKCJA";
                outputString = selectedInstruction + " " + selectedTargetRegister + ", " + selectedSourceRegister;
                codeArea.append("\n" + outputString);

            }
        });

        ActionListener registerSelectionActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String command = e.getActionCommand();

                if (isSourceRegister) selectedSourceRegister = command;
                else selectedTargetRegister = command;


                if(command != "{PODAJ WARTOŚĆ}") {

                    outputString = selectedInstruction + " " + selectedTargetRegister + ", " + selectedSourceRegister;

                }
                else outputString = selectedInstruction + " " + selectedTargetRegister + ", {PODAJ WARTOŚĆ}";

                codeArea.setText(outputString);

            }
        };

        ActionListener instructionSelectionActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                selectedInstruction = e.getActionCommand();
                outputString = selectedInstruction + " " + selectedTargetRegister + ", " + selectedSourceRegister;
                codeArea.setText(outputString);


            }
        };

        //Putting the window together
        (dstRegisterSelection.add(new JMenuItem("AX"))).addActionListener(registerSelectionActionListener);
        (dstRegisterSelection.add(new JMenuItem("BX"))).addActionListener(registerSelectionActionListener);
        (dstRegisterSelection.add(new JMenuItem("CX"))).addActionListener(registerSelectionActionListener);
        (dstRegisterSelection.add(new JMenuItem("DX"))).addActionListener(registerSelectionActionListener);

        (srcRegisterSelection.add(new JMenuItem("AX"))).addActionListener(registerSelectionActionListener);
        (srcRegisterSelection.add(new JMenuItem("BX"))).addActionListener(registerSelectionActionListener);
        (srcRegisterSelection.add(new JMenuItem("CX"))).addActionListener(registerSelectionActionListener);
        (srcRegisterSelection.add(new JMenuItem("DX"))).addActionListener(registerSelectionActionListener);
        (srcRegisterSelection.add(new JMenuItem("{PODAJ WARTOŚĆ}"))).addActionListener(registerSelectionActionListener);

        (instructionSelection.add(new JMenuItem("MOV"))).addActionListener(instructionSelectionActionListener);
        (instructionSelection.add(new JMenuItem("XCHG"))).addActionListener(instructionSelectionActionListener);

        sourceSelectionButton.add(srcRegisterSelection);
        targetSelectionButton.add(dstRegisterSelection);
        instructionSelectionButton.add(instructionSelection);

        selectionPanel.add(instructionSelectionButton);
        selectionPanel.add(targetSelectionButton);
        selectionPanel.add(sourceSelectionButton);
        selectionPanel.add(valueInputField);
        selectionPanel.add(executeButton);

        selectionPanel.add(dstRegisterSelection);
        selectionPanel.add(srcRegisterSelection);
        selectionPanel.add(instructionSelection);

        codePanel.add(codeArea);
        codePanel.add(valuesDisplayArea);

        mainFrame.add(selectionPanel);
        mainFrame.add(codePanel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    private static boolean movImmediateToRegister(String dest, String value){

        Integer parsedValue = tryParse(value);
        if(parsedValue != null && parsedValue <= 32_767 && parsedValue >= -32_768)
        {
            registers.put(dest, parsedValue);
            return true;
        }
        else return false;
    }
    private static void movRegisterToRegister(String dest, String src) {

            registers.put(dest, registers.get(src));
    }

    private static void xchg(String dest, String src){
        int holder = registers.get(dest);
        registers.put(dest, registers.get(src));
        registers.put(src, holder);
    }

    private static Integer tryParse(String value){

        try{
            return Integer.parseInt(value, 16);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String updateValuesDisplay(){
        return  "AX: " + registers.get("AX") +
                "\nBX: " + registers.get("BX") +
                "\nCX: " + registers.get("CX") +
                "\nDX: " + registers.get("DX");
    }

}
