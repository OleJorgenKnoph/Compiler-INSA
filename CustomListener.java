import java.util.*;

public class CustomListener extends CompilerGrammarBaseListener {

    //Creating a Hashmap to store the variables along with the assigned values.
    //Supporting decimals
    private final Map<String, Double> variables;

    public CustomListener() {
        variables = new HashMap<>();
    }

    //Oneline multiple assignment
    //Creating array to store variables that is assigned at the same time to same value
    ArrayList<String> multipleAssign = new ArrayList<>();

    //This is for easifying the math operations and store only the operations to perform on
    ArrayList<String> arithmeticOperation = new ArrayList<>();

    //True as default because all operations must be able even if there is no if/while statement
    //I will put an if sentence to demand the variable to be true before any function can run
    public static Boolean IF_state = null;
    public static boolean else_state = false;

    //Gathering the ASSEMBLY codes from the instructions
    public static ArrayList<String> assemblyCodes = new ArrayList<>();


    //Accessed when variables gets declared and assigned value and multiple variables to a value
    //as well as delcaring a math equation and assigning it to an variable
    @Override
    public void exitDeclaration(CompilerGrammarParser.DeclarationContext ctx) {
        if (IF_state == null || IF_state) //If is false and else is true
        {
            //If ctx has an expression its either mathematical or assigned letter
            if (ctx.expression().size() != 0) {
                //If the string contains mathematical operators do them in right order
                if (String.valueOf(ctx.expression(0).getText()).contains("+")
                        || String.valueOf(ctx.expression(0).getText()).contains("-")
                        || String.valueOf(ctx.expression(0).getText()).contains("*")
                        || String.valueOf(ctx.expression(0).getText()).contains("/")) {

                    //Var for numbers with multiple digits
                    StringBuilder mulNumbers = new StringBuilder();

                    //Getting the math expression in an arraylist to perform operations on later
                    for (int i = 0; i < ctx.expression(0).getText().length(); ++i) {

                        //Trying the catch IndexOutOfBounds
                        try {
                            //If true the next char is also a digit so i can get all number individually
                            //but numbers that belong together must be at same index
                            if (Character.isDigit(ctx.expression(0).getText().charAt(i + 1))) {

                                //Run as long as the char is a number and the next is a number
                                while (Character.isDigit(ctx.expression(0).getText().charAt(i)) && Character.isDigit(ctx.expression(0).getText().charAt(i + 1))) {
                                    mulNumbers.append(ctx.expression(0).getText().charAt(i));

                                    //increase i to loop over next
                                    i++;
                                }
                                //If the next one is not a digit but current one its the last number of the whole digit
                                if (Character.isDigit(ctx.expression(0).getText().charAt(i))) {
                                    mulNumbers.append(ctx.expression(0).getText().charAt(i));
                                }
                            }
                        }
                        //If caught then we are at the last index and cant check the index out of bounds
                        catch (StringIndexOutOfBoundsException e) {
                            //If current is digit then add it to the stringbuilder
                            if (Character.isDigit(ctx.expression(0).getText().charAt(i))) {
                                mulNumbers.append(ctx.expression(0).getText().charAt(i));
                            }
                            //Else its an operator we still want a part of our arraylist
                            else {
                                arithmeticOperation.add(String.valueOf(ctx.expression(0).getText().charAt(i)));
                            }
                        }
                        //If the stringbuilder is empty we have a new Char as current and we add it
                        if (mulNumbers.length() == 0) {
                            arithmeticOperation.add(String.valueOf(ctx.expression(0).getText().charAt(i)));
                        }
                        //If not empty its means that every digit of the number is added and we
                        //add the stringbuilder to arraylist and clear the sb for new use
                        else {
                            //Adding all the coherrent digits
                            arithmeticOperation.add(String.valueOf(mulNumbers));
                            mulNumbers.setLength(0);
                        }
                    }

                    //Three loops for math to make sure the right operation order
                    //For parenthesis
                    //Helpvariabel to make sure the others two loops start after all the parenthesis are sorted
                    int startIndex = 0;
                    int newStart;
                    //For parenthesis
                    for (int k = startIndex; k < arithmeticOperation.size(); ++k) {
                        if (arithmeticOperation.get(k).equals("(")) {

                            //Remove the (
                            arithmeticOperation.remove(k);

                            k++;
                            newStart = k;

                            //Multiplication and dividing
                            while (!arithmeticOperation.get(k).equals(")")) {

                                //Multiplication
                                if (arithmeticOperation.get(k).equals("*")) {
                                    //Init variables
                                    double variable1;
                                    double variable2;

                                    //check if its a number or not. in that case a letter
                                    if (checkDigit(arithmeticOperation.get(k - 1))) {
                                        variable1 = Double.parseDouble(arithmeticOperation.get(k - 1));
                                    } else {
                                        String curVar = arithmeticOperation.get(k - 1);

                                        variable1 = variables.get(curVar);
                                    }

                                    if (checkDigit(arithmeticOperation.get(k + 1))) {
                                        variable2 = Double.parseDouble(arithmeticOperation.get(k + 1));
                                    } else {
                                        String curVar = arithmeticOperation.get(k + 1);

                                        variable2 = variables.get(curVar);
                                    }

                                    double result = variable1 * variable2;
                                    //ASSEMBLY
                                    assemblyCodes.add("mul2");

                                    arithmeticOperation.set(k, String.valueOf(result));
                                    arithmeticOperation.remove(k - 1);
                                    arithmeticOperation.remove(k);

                                    if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                        variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                    } else {
                                        variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                    }
                                    k--;
                                }

                                //Divide
                                if (arithmeticOperation.get(k).equals("/")) {
                                    //Init variables
                                    double variable1;
                                    double variable2;

                                    //check if its a number or not. in that case a letter
                                    if (checkDigit(arithmeticOperation.get(k - 1))) {
                                        variable1 = Integer.parseInt(arithmeticOperation.get(k - 1));
                                    } else {
                                        String curVar = arithmeticOperation.get(k - 1);

                                        variable1 = variables.get(curVar);
                                    }

                                    if (checkDigit(arithmeticOperation.get(k + 1))) {
                                        variable2 = Integer.parseInt(arithmeticOperation.get(k + 1));
                                    } else {
                                        String curVar = arithmeticOperation.get(k + 1);

                                        variable2 = variables.get(curVar);
                                    }

                                    double result = variable1 / variable2;
                                    //ASSEMBLY
                                    assemblyCodes.add("div4");

                                    arithmeticOperation.set(k, String.valueOf(result));
                                    arithmeticOperation.remove(k - 1);
                                    arithmeticOperation.remove(k);

                                    if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                        variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                    } else {
                                        variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                    }
                                    k--;
                                }

                                k++;
                            }
                            k = newStart;
                            //Addition and subtraction
                            while (!arithmeticOperation.get(k).equals(")")) {

                                //Addition
                                if (arithmeticOperation.get(k).equals("+")) {
                                    //Init variables
                                    double variable1;
                                    double variable2;

                                    //check if its a number or not. in that case a letter
                                    if (checkDigit(arithmeticOperation.get(k - 1))) {
                                        variable1 = Double.parseDouble(arithmeticOperation.get(k - 1));
                                    } else {
                                        String curVar = arithmeticOperation.get(k - 1);

                                        variable1 = variables.get(curVar);
                                    }

                                    if (checkDigit(arithmeticOperation.get(k + 1))) {
                                        variable2 = Double.parseDouble(arithmeticOperation.get(k + 1));
                                    } else {
                                        String curVar = arithmeticOperation.get(k + 1);

                                        variable2 = variables.get(curVar);
                                    }

                                    double result = variable1 + variable2;
                                    //ASSEMBLY
                                    assemblyCodes.add("add1");

                                    arithmeticOperation.set(k, String.valueOf(result));
                                    arithmeticOperation.remove(k - 1);
                                    arithmeticOperation.remove(k);

                                    if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                        variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                    } else {
                                        variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                    }
                                    k--;
                                }

                                //Subtraction
                                if (arithmeticOperation.get(k).equals("-")) {
                                    //Init variables
                                    double variable1;
                                    double variable2;

                                    //check if its a number or not. in that case a letter
                                    if (checkDigit(arithmeticOperation.get(k - 1))) {
                                        variable1 = Double.parseDouble(arithmeticOperation.get(k - 1));
                                    } else {
                                        String curVar = arithmeticOperation.get(k - 1);

                                        variable1 = variables.get(curVar);
                                    }

                                    if (checkDigit(arithmeticOperation.get(k + 1))) {
                                        variable2 = Integer.parseInt(arithmeticOperation.get(k + 1));
                                    } else {
                                        String curVar = arithmeticOperation.get(k + 1);

                                        variable2 = variables.get(curVar);
                                    }

                                    double result = variable1 - variable2;
                                    //ASSEMBLY
                                    assemblyCodes.add("sou3");

                                    arithmeticOperation.set(k, String.valueOf(result));
                                    arithmeticOperation.remove(k - 1);
                                    arithmeticOperation.remove(k);

                                    if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                        variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                    } else {
                                        variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                    }
                                    k--;
                                }
                                k++;
                            }
                            k = 1;
                            if (arithmeticOperation.get(k).equals(")")) {
                                arithmeticOperation.remove(k);
                                k--;
                            }
                        }
                    }
                    //Multiplication and dividing
                    for (int j = startIndex; j < arithmeticOperation.size(); ++j) {

                        //Multiplication
                        if (arithmeticOperation.get(j).equals("*")) {
                            //Init variables
                            double variable1;
                            double variable2;

                            //check if its a number or not. in that case a letter
                            if (checkDigit(arithmeticOperation.get(j - 1))) {
                                variable1 = Double.parseDouble(arithmeticOperation.get(j - 1));
                            } else {
                                String curVar = arithmeticOperation.get(j - 1);

                                variable1 = variables.get(curVar);
                            }

                            if (checkDigit(arithmeticOperation.get(j + 1))) {
                                variable2 = Double.parseDouble(arithmeticOperation.get(j + 1));
                            } else {
                                String curVar = arithmeticOperation.get(j + 1);

                                variable2 = variables.get(curVar);
                            }

                            double result = variable1 * variable2;
                            //ASSEMBLY
                            assemblyCodes.add("mul2");

                            arithmeticOperation.set(j, String.valueOf(result));
                            arithmeticOperation.remove(j - 1);
                            arithmeticOperation.remove(j);

                            if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                            } else {
                                variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                            }
                            j--;
                        }

                        //Divide
                        if (arithmeticOperation.get(j).equals("/")) {
                            //Init variables
                            double variable1;
                            double variable2;

                            //check if its a number or not. in that case a letter
                            if (checkDigit(arithmeticOperation.get(j - 1))) {
                                variable1 = Integer.parseInt(arithmeticOperation.get(j - 1));
                            } else {
                                String curVar = arithmeticOperation.get(j - 1);

                                variable1 = variables.get(curVar);
                            }

                            if (checkDigit(arithmeticOperation.get(j + 1))) {
                                variable2 = Integer.parseInt(arithmeticOperation.get(j + 1));
                            } else {
                                String curVar = arithmeticOperation.get(j + 1);

                                variable2 = variables.get(curVar);
                            }

                            double result = variable1 / variable2;
                            //ASSEMBLY
                            assemblyCodes.add("div4");

                            arithmeticOperation.set(j, String.valueOf(result));
                            arithmeticOperation.remove(j - 1);
                            arithmeticOperation.remove(j);

                            if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                            } else {
                                variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                            }
                            j--;
                        }
                    }
                    //Addition and subtraction
                    for (int j = startIndex; j < arithmeticOperation.size(); ++j) {

                        //Addition
                        if (arithmeticOperation.get(j).equals("+")) {
                            //Init variables
                            double variable1;
                            double variable2;

                            //check if its a number or not. in that case a letter
                            if (checkDigit(arithmeticOperation.get(j - 1))) {
                                variable1 = Double.parseDouble(arithmeticOperation.get(j - 1));
                            } else {
                                String curVar = arithmeticOperation.get(j - 1);

                                variable1 = variables.get(curVar);
                            }

                            if (checkDigit(arithmeticOperation.get(j + 1))) {
                                variable2 = Double.parseDouble(arithmeticOperation.get(j + 1));
                            } else {
                                String curVar = arithmeticOperation.get(j + 1);

                                variable2 = variables.get(curVar);
                            }

                            double result = variable1 + variable2;
                            //ASSEMBLY
                            assemblyCodes.add("add1");

                            arithmeticOperation.set(j, String.valueOf(result));
                            arithmeticOperation.remove(j - 1);
                            arithmeticOperation.remove(j);

                            if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                            } else {
                                variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                            }
                            j--;
                        }

                        //Subtraction
                        if (arithmeticOperation.get(j).equals("-")) {
                            //Init variables
                            double variable1;
                            double variable2;

                            //check if its a number or not. in that case a letter
                            if (checkDigit(arithmeticOperation.get(j - 1))) {
                                variable1 = Integer.parseInt(arithmeticOperation.get(j - 1));
                            } else {
                                String curVar = arithmeticOperation.get(j - 1);

                                variable1 = variables.get(curVar);
                            }

                            if (checkDigit(arithmeticOperation.get(j + 1))) {
                                variable2 = Integer.parseInt(arithmeticOperation.get(j + 1));
                            } else {
                                String curVar = arithmeticOperation.get(j + 1);

                                variable2 = variables.get(curVar);
                            }

                            double result = variable1 - variable2;
                            //ASSEMBLY
                            assemblyCodes.add("sou3");

                            arithmeticOperation.set(j, String.valueOf(result));
                            arithmeticOperation.remove(j - 1);
                            arithmeticOperation.remove(j);

                            if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                //ASSEMBLY
                                assemblyCodes.add("afc6");
                            } else {
                                variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                //ASSEMBLY
                                assemblyCodes.add("afc6");
                            }
                            j--;
                        }
                    }
                }
                //It means var is assigned a letter/another variabel
                else {
                    //Get the letter assigned
                    String varAssigned = String.valueOf(ctx.expression(0).getText());

                    //Get the value of the letter
                    double value = variables.get(varAssigned);

                    //If VAR_NAME already exists update, or else put new
                    if (variables.containsKey(ctx.VAR_NAME().getText())) {
                        variables.replace(ctx.VAR_NAME().getText(), value);
                        //ASSEMBLY
                        assemblyCodes.add("afc6");
                    } else {
                        variables.put(ctx.VAR_NAME().getText(), value);
                        //ASSEMBLY
                        assemblyCodes.add("afc6");
                    }
                }
            } else {
                //If an value is assigned to a NUM in the current
                if (ctx.NUM() != null) {
                    variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(ctx.NUM().getText()));
                    //ASSEMBLY
                    assemblyCodes.add("afc6");
                }

                //If the exist either a sub-path to another declaration or finalDeclaration
                // it means that the code assigns more variables to a value
                if ((ctx.declaration() != null || ctx.finalDeclaration() != null)) {
                    //Adding values
                    multipleAssign.add(ctx.VAR_NAME().getText());

                    //When this is true. We have reached the top and every value is in the array
                    if (ctx.getRuleIndex() != ctx.parent.getRuleIndex()) {

                        //Storing the value and parsing it
                        String curValue = multipleAssign.get(0);
                        double value = Double.parseDouble(curValue);

                        //Starting from index 1 and running thru
                        for (int i = 1; i < multipleAssign.size(); ++i) {
                            //Adding every variabel with the value
                            variables.put(multipleAssign.get(i), value);
                            //ASSEMBLY
                            assemblyCodes.add("afc6");
                        }
                        //Clear for new use
                        multipleAssign.clear();
                    }
                }
            }
        }
    }

    //This is to assigned the last value in a multiple assign sentence and to also make sure instructions dont come after declaration
    @Override
    public void exitFinalDeclaration(CompilerGrammarParser.FinalDeclarationContext ctx) {
        if (IF_state == null || IF_state)
        {
            //This function is only when multiple variables is assigned same line and value
            //Adding NUM to the array in index 0
            if (ctx.NUM() != null) {
                multipleAssign.add(ctx.NUM().getText());
            }
            //Adding the variabel
            multipleAssign.add(ctx.VAR_NAME().getText());
        }

    }

    //Statements is to print out a variables value aswell as a raw number
    @Override
    public void exitStatements(CompilerGrammarParser.StatementsContext ctx) {
        if (IF_state == null || IF_state) //If is false and else is true
        {
            if (ctx.PRINT() != null) {

                //If it is not a digit then it is a variable to find and print
                if (!checkDigit(ctx.expression().getText())) {
                    //Finding the variable that is going to be written out
                    String key = String.valueOf(ctx.expression().getText());

                    //Retrieving from hashmap
                    System.out.println(variables.get(key));
                    //ASSEMBLY
                    assemblyCodes.add("pric");
                } else if (checkDigit(ctx.expression().getText())) {
                    //Retrieving from hashmap
                    System.out.println(ctx.expression().getText());
                    //ASSEMBLY
                    assemblyCodes.add("pric");
                }
            }
        }
    }

    //This is to assign a already declared variable to either a new value or an expression
    //Also takes care is an if-sentence
    @Override
    public void exitEXPRESSIONS(CompilerGrammarParser.EXPRESSIONSContext ctx) {
        if (IF_state == null || IF_state)//If is true or null
        {

            //This means that a variable is updated
            if (ctx.VAR_NAME() != null) {

                //Means that variabel is assigned to either 1 number or 1 letter(another variabel)
                if (ctx.expression().getChildCount() == 1) {

                    //Trying to parse, if exception it means its a letter
                    try {
                        double valueToAssign = Double.parseDouble(ctx.expression().getText());
                        variables.replace(ctx.VAR_NAME().getText(), valueToAssign);
                        //ASSEMBLY
                        assemblyCodes.add("cop5");
                    } catch (NumberFormatException e) {
                        //Getting the value of the key which was assigned VAR_NAME
                        double assignedValue = variables.get(ctx.expression().children.toString());

                        //Updates the variabel
                        variables.put(ctx.VAR_NAME().getText(), assignedValue);
                        //ASSEMBLY
                        assemblyCodes.add("cop5");
                    }
                } else {
                    //If the string contains mathematical operators do them in right order
                    if (String.valueOf(ctx.expression().getText()).contains("+")
                            || String.valueOf(ctx.expression().getText()).contains("-")
                            || String.valueOf(ctx.expression().getText()).contains("*")
                            || String.valueOf(ctx.expression().getText()).contains("/")) {

                        //Var for numbers with multiple digits
                        StringBuilder mulNumbers = new StringBuilder();

                        //Getting the math expression in an arraylist to perform operations on later
                        for (int i = 0; i < ctx.expression().getText().length(); ++i) {

                            //Trying the catch IndexOutOfBounds
                            try {
                                //If true the next char is also a digit so i can get all number individually
                                //but numbers that belong together must be at same index
                                if (Character.isDigit(ctx.expression().getText().charAt(i + 1))) {

                                    //Run as long as the char is a number and the next is a number
                                    while (Character.isDigit(ctx.expression().getText().charAt(i)) && Character.isDigit(ctx.expression().getText().charAt(i + 1))) {
                                        mulNumbers.append(ctx.expression().getText().charAt(i));

                                        //increase i to loop over next
                                        i++;
                                    }
                                    //If the next one is not a digit but current one its the last number of the whole digit
                                    if (Character.isDigit(ctx.expression().getText().charAt(i))) {
                                        mulNumbers.append(ctx.expression().getText().charAt(i));
                                    }
                                }
                            }
                            //If caught then we are at the last index and cant check the index out of bounds
                            catch (StringIndexOutOfBoundsException e) {
                                //If current is digit then add it to the stringbuilder
                                if (checkDigit(String.valueOf(ctx.expression().getText().charAt(i))) || checkLetter(String.valueOf(ctx.expression().getText().charAt(i)))) {
                                    mulNumbers.append(ctx.expression().getText().charAt(i));
                                }
                                //Else its an operator we still want a part of our arraylist
                                else {
                                    arithmeticOperation.add(String.valueOf(ctx.expression().getText().charAt(i)));
                                }
                            }
                            //If the stringbuilder is empty we have a new Char as current and we add it
                            if (mulNumbers.length() == 0) {
                                arithmeticOperation.add(String.valueOf(ctx.expression().getText().charAt(i)));
                            }
                            //If not empty its means that every digit of the number is added and we
                            //add the stringbuilder to arraylist and clear the sb for new use
                            else {
                                //Adding all the coherrent digits
                                arithmeticOperation.add(String.valueOf(mulNumbers));
                                mulNumbers.setLength(0);
                            }
                        }

                        //Three loops for math to make sure the right operation order
                        //For parenthesis
                        //Helpvariabel to make sure the others two loops start after all the parenthesis are sorted
                        int startIndex = 0;
                        for (int k = startIndex; k < arithmeticOperation.size(); ++k) {
                            if (arithmeticOperation.get(k).equals("(")) {

                                //Remove the (
                                arithmeticOperation.remove(k);

                                k++;

                                //Multiplication and dividing
                                while (!arithmeticOperation.get(k).equals(")")) {

                                    //Multiplication
                                    if (arithmeticOperation.get(k).equals("*")) {
                                        //Init variables
                                        double variable1;
                                        double variable2;

                                        //check if its a number or not. in that case a letter
                                        if (checkDigit(arithmeticOperation.get(k - 1))) {
                                            variable1 = Double.parseDouble(arithmeticOperation.get(k - 1));
                                        } else {
                                            String curVar = arithmeticOperation.get(k - 1);

                                            variable1 = variables.get(curVar);
                                        }

                                        if (checkDigit(arithmeticOperation.get(k + 1))) {
                                            variable2 = Double.parseDouble(arithmeticOperation.get(k + 1));
                                        } else {
                                            String curVar = arithmeticOperation.get(k + 1);

                                            variable2 = variables.get(curVar);
                                        }

                                        double result = variable1 * variable2;
                                        //ASSEMBLY
                                        assemblyCodes.add("mul2");

                                        arithmeticOperation.set(k, String.valueOf(result));
                                        arithmeticOperation.remove(k - 1);
                                        arithmeticOperation.remove(k);

                                        if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                            variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                        } else {
                                            variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                        }
                                        k--;
                                    }

                                    //Divide
                                    if (arithmeticOperation.get(k).equals("/")) {
                                        //Init variables
                                        double variable1;
                                        double variable2;

                                        //check if its a number or not. in that case a letter
                                        if (checkDigit(arithmeticOperation.get(k - 1))) {
                                            variable1 = Integer.parseInt(arithmeticOperation.get(k - 1));
                                        } else {
                                            String curVar = arithmeticOperation.get(k - 1);

                                            variable1 = variables.get(curVar);
                                        }

                                        if (checkDigit(arithmeticOperation.get(k + 1))) {
                                            variable2 = Integer.parseInt(arithmeticOperation.get(k + 1));
                                        } else {
                                            String curVar = arithmeticOperation.get(k + 1);

                                            variable2 = variables.get(curVar);
                                        }

                                        double result = variable1 / variable2;
                                        //ASSEMBLY
                                        assemblyCodes.add("div4");

                                        arithmeticOperation.set(k, String.valueOf(result));
                                        arithmeticOperation.remove(k - 1);
                                        arithmeticOperation.remove(k);

                                        if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                            variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                        } else {
                                            variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                        }
                                        k--;
                                    }

                                    k++;
                                }
                                k = 1;
                                //Addition and subtraction
                                while (!arithmeticOperation.get(k).equals(")")) {

                                    //Addition
                                    if (arithmeticOperation.get(k).equals("+")) {
                                        //Init variables
                                        double variable1;
                                        double variable2;

                                        //check if its a number or not. in that case a letter
                                        if (checkDigit(arithmeticOperation.get(k - 1))) {
                                            variable1 = Double.parseDouble(arithmeticOperation.get(k - 1));
                                        } else {
                                            String curVar = arithmeticOperation.get(k - 1);

                                            variable1 = variables.get(curVar);
                                        }

                                        if (checkDigit(arithmeticOperation.get(k + 1))) {
                                            variable2 = Double.parseDouble(arithmeticOperation.get(k + 1));
                                        } else {
                                            String curVar = arithmeticOperation.get(k + 1);

                                            variable2 = variables.get(curVar);
                                        }

                                        double result = variable1 + variable2;
                                        //ASSEMBLY
                                        assemblyCodes.add("add1");

                                        arithmeticOperation.set(k, String.valueOf(result));
                                        arithmeticOperation.remove(k - 1);
                                        arithmeticOperation.remove(k);

                                        if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                            variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                        } else {
                                            variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                        }
                                        k--;
                                    }

                                    //Subtraction
                                    if (arithmeticOperation.get(k).equals("-")) {
                                        //Init variables
                                        double variable1;
                                        double variable2;

                                        //check if its a number or not. in that case a letter
                                        if (checkDigit(arithmeticOperation.get(k - 1))) {
                                            variable1 = Double.parseDouble(arithmeticOperation.get(k - 1));
                                        } else {
                                            String curVar = arithmeticOperation.get(k - 1);

                                            variable1 = variables.get(curVar);
                                        }

                                        if (checkDigit(arithmeticOperation.get(k + 1))) {
                                            variable2 = Integer.parseInt(arithmeticOperation.get(k + 1));
                                        } else {
                                            String curVar = arithmeticOperation.get(k + 1);

                                            variable2 = variables.get(curVar);
                                        }

                                        double result = variable1 - variable2;
                                        //ASSEMBLY
                                        assemblyCodes.add("sou3");

                                        arithmeticOperation.set(k, String.valueOf(result));
                                        arithmeticOperation.remove(k - 1);
                                        arithmeticOperation.remove(k);

                                        if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                            variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                        } else {
                                            variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(k - 1)));
                                        }
                                        k--;
                                    }
                                    k++;
                                }
                                k = 1;
                                if (arithmeticOperation.get(k).equals(")")) {
                                    arithmeticOperation.remove(k);
                                    k--;
                                }
                            }
                        }
                        //Multiplication and dividing
                        for (int j = startIndex; j < arithmeticOperation.size(); ++j) {

                            //Multiplication
                            if (arithmeticOperation.get(j).equals("*")) {
                                //Init variables
                                double variable1;
                                double variable2;

                                //check if its a number or not. in that case a letter
                                if (checkDigit(arithmeticOperation.get(j - 1))) {
                                    variable1 = Double.parseDouble(arithmeticOperation.get(j - 1));
                                } else {
                                    String curVar = arithmeticOperation.get(j - 1);

                                    variable1 = variables.get(curVar);
                                }

                                if (checkDigit(arithmeticOperation.get(j + 1))) {
                                    variable2 = Double.parseDouble(arithmeticOperation.get(j + 1));
                                } else {
                                    String curVar = arithmeticOperation.get(j + 1);

                                    variable2 = variables.get(curVar);
                                }

                                double result = variable1 * variable2;
                                //ASSEMBLY
                                assemblyCodes.add("mul2");

                                arithmeticOperation.set(j, String.valueOf(result));
                                arithmeticOperation.remove(j - 1);
                                arithmeticOperation.remove(j);

                                if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                    variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                } else {
                                    variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                }
                                j--;
                            }

                            //Divide
                            if (arithmeticOperation.get(j).equals("/")) {
                                //Init variables
                                double variable1;
                                double variable2;

                                //check if its a number or not. in that case a letter
                                if (checkDigit(arithmeticOperation.get(j - 1))) {
                                    variable1 = Integer.parseInt(arithmeticOperation.get(j - 1));
                                } else {
                                    String curVar = arithmeticOperation.get(j - 1);

                                    variable1 = variables.get(curVar);
                                }

                                if (checkDigit(arithmeticOperation.get(j + 1))) {
                                    variable2 = Integer.parseInt(arithmeticOperation.get(j + 1));
                                } else {
                                    String curVar = arithmeticOperation.get(j + 1);

                                    variable2 = variables.get(curVar);
                                }

                                double result = variable1 / variable2;
                                //ASSEMBLY
                                assemblyCodes.add("div4");

                                arithmeticOperation.set(j, String.valueOf(result));
                                arithmeticOperation.remove(j - 1);
                                arithmeticOperation.remove(j);

                                if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                    variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                } else {
                                    variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                }
                                j--;
                            }
                        }
                        //Addition and subtraction
                        for (int j = startIndex; j < arithmeticOperation.size(); ++j) {

                            //Addition
                            if (arithmeticOperation.get(j).equals("+")) {
                                //Init variables
                                double variable1;
                                double variable2;

                                //check if its a number or not. in that case a letter
                                if (checkDigit(arithmeticOperation.get(j - 1))) {
                                    variable1 = Double.parseDouble(arithmeticOperation.get(j - 1));
                                } else {
                                    String curVar = arithmeticOperation.get(j - 1);

                                    variable1 = variables.get(curVar);
                                }

                                if (checkDigit(arithmeticOperation.get(j + 1))) {
                                    variable2 = Double.parseDouble(arithmeticOperation.get(j + 1));
                                } else {
                                    String curVar = arithmeticOperation.get(j + 1);

                                    variable2 = variables.get(curVar);
                                }

                                double result = variable1 + variable2;
                                //ASSEMBLY
                                assemblyCodes.add("add1");

                                arithmeticOperation.set(j, String.valueOf(result));
                                arithmeticOperation.remove(j - 1);
                                arithmeticOperation.remove(j);

                                if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                    variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                } else {
                                    variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                }
                                j--;
                            }

                            //Subtraction
                            if (arithmeticOperation.get(j).equals("-")) {
                                //Init variables
                                double variable1;
                                double variable2;

                                //check if its a number or not. in that case a letter
                                if (checkDigit(arithmeticOperation.get(j - 1))) {
                                    variable1 = Double.parseDouble(arithmeticOperation.get(j - 1));
                                } else {
                                    String curVar = arithmeticOperation.get(j - 1);

                                    variable1 = variables.get(curVar);
                                }

                                if (checkDigit(arithmeticOperation.get(j + 1))) {
                                    variable2 = Integer.parseInt(arithmeticOperation.get(j + 1));
                                } else {
                                    String curVar = arithmeticOperation.get(j + 1);

                                    variable2 = variables.get(curVar);
                                }

                                double result = variable1 - variable2;
                                //ASSEMBLY
                                assemblyCodes.add("sou3");

                                arithmeticOperation.set(j, String.valueOf(result));
                                arithmeticOperation.remove(j - 1);
                                arithmeticOperation.remove(j);

                                if (variables.containsKey(ctx.VAR_NAME().getText())) {
                                    variables.replace(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                    //ASSEMBLY
                                    assemblyCodes.add("afc6");
                                } else {
                                    variables.put(ctx.VAR_NAME().getText(), Double.parseDouble(arithmeticOperation.get(j - 1)));
                                    //ASSEMBLY
                                    assemblyCodes.add("afc6");
                                }
                                j--;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void enterFunc_body(CompilerGrammarParser.Func_bodyContext ctx) {
        //If there exists a if loop
            if (ctx.parent.getChild(0).getText().equals("if")){
                IF_state = check_IF_WHILE(ctx.parent.getChild(2).getText());
            }
    }

        //To make sure if the if-sentece is done, the else will be skipped
    @Override
    public void enterElse(CompilerGrammarParser.ElseContext ctx) {
        if (IF_state != null){
            IF_state = !IF_state;
        }
    }

    //To make sure if the else is done everything else runs as usual
    @Override
    public void exitElse(CompilerGrammarParser.ElseContext ctx) {
        IF_state = null;
    }


    //Checking if the if or while sentence is true
    public boolean check_IF_WHILE(String conditions) {

        boolean state = false;

        if (conditions.contains("==")) {
            //Getting the index of the ==
            int operators = conditions.indexOf("==");
            //Leftside from index 0 to (excluding) the gotten index
            String leftSide = conditions.substring(0, operators);
            //Rightside opposite side. from operators +2 to end
            String rightSide = conditions.substring(operators + 2);

            //If both integer
            if (checkDigit(leftSide) && checkDigit(rightSide)) {
                if (Double.valueOf(leftSide).equals(Double.valueOf(rightSide))) {
                    state = true;
                }
            }
            //If left is integer and right is variable
            else if (checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double rightSideValue = variables.get(rightSide);
                if (Double.valueOf(leftSide).equals(rightSideValue)) {
                    state = true;
                }
            }
            //If left is variable and right is integer
            else if (!checkDigit(leftSide) && checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                if (Double.valueOf(rightSide).equals(leftSideValue)) {
                    state = true;
                }
            }
            //If both is variabel
            else if (!checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                Double rightSideValue = variables.get(rightSide);
                if (leftSideValue.equals(rightSideValue)) {
                    state = true;
                }
            }

            if (state) {
                //ASSEMBLY
                assemblyCodes.add("equb");
            }

        } else if (conditions.contains("!=")) {
            //Getting the index of the !=
            int operators = conditions.indexOf("!=");
            //Leftside from index 0 to (excluding) the gotten index
            String leftSide = conditions.substring(0, operators);
            //Rightside opposite side. from operators +2 to end
            String rightSide = conditions.substring(operators + 2);

            //If both integer
            if (checkDigit(leftSide) && checkDigit(rightSide)) {
                if (!(Double.valueOf(leftSide).equals(Double.valueOf(rightSide)))) {
                    state = true;
                }
            }
            //If left is integer and right is variable
            else if (checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double rightSideValue = variables.get(rightSide);
                if (!(Double.valueOf(leftSide).equals(rightSideValue))) {
                    state = true;
                }
            }
            //If left is variable and right is integer
            else if (!checkDigit(leftSide) && checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                if (!(Double.valueOf(rightSide).equals(leftSideValue))) {
                    state = true;
                }
            }
            //If both is variabel
            else if (!checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                Double rightSideValue = variables.get(rightSide);
                if (!(leftSideValue.equals(rightSideValue))) {
                    state = true;
                }
            }
        } else if (conditions.contains("<=") || conditions.contains("=<")) {
            //Getting the index of the <=. Can be written two different ways
            int operators;
            if (conditions.contains("<=")) {
                operators = conditions.indexOf("<=");
            } else {
                operators = conditions.indexOf("=<");
            }
            //Leftside from index 0 to (excluding) the gotten index
            String leftSide = conditions.substring(0, operators);
            //Rightside opposite side. from operators +1 to end
            String rightSide = conditions.substring(operators + 2);

            //If both integer
            if (checkDigit(leftSide) && checkDigit(rightSide)) {
                if (Double.parseDouble(leftSide) <= Double.parseDouble(rightSide)) {
                    state = true;
                }
            }
            //If left is integer and right is variable
            else if (checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double rightSideValue = variables.get(rightSide);
                if (Double.parseDouble(leftSide) <= rightSideValue) {
                    state = true;
                }
            }
            //If left is variable and right is integer
            else if (!checkDigit(leftSide) && checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                if (leftSideValue <= Double.parseDouble(rightSide)) {
                    state = true;
                }
            }
            //If both is variabel
            else if (!checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                Double rightSideValue = variables.get(rightSide);
                if (leftSideValue <= rightSideValue) {
                    state = true;
                }
            }
        } else if (conditions.contains(">=") || conditions.contains("=>")) {
            //Getting the index of the >=. Can be written two different ways
            int operators;
            if (conditions.contains(">=")) {
                operators = conditions.indexOf(">=");
            } else {
                operators = conditions.indexOf("=>");
            }
            //Leftside from index 0 to (excluding) the gotten index
            String leftSide = conditions.substring(0, operators);
            //Rightside opposite side. from operators +1 to end
            String rightSide = conditions.substring(operators + 2);

            //If both integer
            if (checkDigit(leftSide) && checkDigit(rightSide)) {
                if (Double.parseDouble(leftSide) >= Double.parseDouble(rightSide)) {
                    state = true;
                }
            }
            //If left is integer and right is variable
            else if (checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double rightSideValue = variables.get(rightSide);
                if (Double.parseDouble(leftSide) >= rightSideValue) {
                    state = true;
                }
            }
            //If left is variable and right is integer
            else if (!checkDigit(leftSide) && checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                if (leftSideValue >= Double.parseDouble(rightSide)) {
                    state = true;
                }
            }
            //If both is variabel
            else if (!checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                Double rightSideValue = variables.get(rightSide);
                if (leftSideValue >= rightSideValue) {
                    state = true;
                }
            }
        } else if (conditions.contains("<")) {
            //Getting the index of the <
            int operators = conditions.indexOf("<");
            //Leftside from index 0 to (excluding) the gotten index
            String leftSide = conditions.substring(0, operators);
            //Rightside opposite side. from operators +1 to end
            String rightSide = conditions.substring(operators + 1);

            //If both integer
            if (checkDigit(leftSide) && checkDigit(rightSide)) {
                if (Double.parseDouble(leftSide) < Double.parseDouble(rightSide)) {
                    state = true;
                }
            }
            //If left is integer and right is variable
            else if (checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double rightSideValue = variables.get(rightSide);
                if (Double.parseDouble(leftSide) < rightSideValue) {
                    state = true;
                }
            }
            //If left is variable and right is integer
            else if (!checkDigit(leftSide) && checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                if (leftSideValue < Double.parseDouble(rightSide)) {
                    state = true;
                }
            }
            //If both is variabel
            else if (!checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                Double rightSideValue = variables.get(rightSide);
                if (leftSideValue < rightSideValue) {
                    state = true;
                }
            }

            if (state) {
                //ASSEMBLY
                assemblyCodes.add("inf9");
            }
        } else if (conditions.contains(">")) {
            //Getting the index of the >
            int operators = conditions.indexOf(">");
            //Leftside from index 0 to (excluding) the gotten index
            String leftSide = conditions.substring(0, operators);
            //Rightside opposite side. from operators +1 to end
            String rightSide = conditions.substring(operators + 1);

            //If both integer
            if (checkDigit(leftSide) && checkDigit(rightSide)) {
                if (Double.parseDouble(leftSide) > Double.parseDouble(rightSide)) {
                    state = true;
                }
            }
            //If left is integer and right is variable
            else if (checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double rightSideValue = variables.get(rightSide);
                if (Double.parseDouble(leftSide) > rightSideValue) {
                    state = true;
                }
            }
            //If left is variable and right is integer
            else if (!checkDigit(leftSide) && checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                if (leftSideValue > Double.parseDouble(rightSide)) {
                    state = true;
                }
            }
            //If both is variabel
            else if (!checkDigit(leftSide) && !checkDigit(rightSide)) {
                Double leftSideValue = variables.get(leftSide);
                Double rightSideValue = variables.get(rightSide);
                if (leftSideValue > rightSideValue) {
                    state = true;
                }
            }
            if (state) {
                //ASSEMBLY
                assemblyCodes.add("supa");
            }
        }


        if (state) {
            //ASSEMBLY
            assemblyCodes.add("jmp7");
        } else {
            //ASSEMBLY
            assemblyCodes.add("jmf8");
        }

        if (!state){
            else_state = true;
        }

        return state;
    }

        //Printing the assembly codes. Dont know what or how else I was supposed to show the assembly
    public void assembly(){
        System.out.println(assemblyCodes);
    }

    //Created for help to check if input is numeric or alphabetic
    public static boolean checkDigit(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public static boolean checkLetter(String string) {
        return string.matches("^[A-Za-z]");
    }
}

