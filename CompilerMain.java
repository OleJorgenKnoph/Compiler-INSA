import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.IOException;

public class CompilerMain {
    public static void main(String[] args) {
        try {
            String myFile="/Users/OJ/Desktop/INSA/SoftwareHardware/Prosjekt/Compiler_Project/src/testFile2.txt";

            CharStream input = CharStreams.fromFileName(myFile);
            CompilerGrammarLexer lexer = new CompilerGrammarLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CompilerGrammarParser parser = new CompilerGrammarParser(tokens);

            //Adding customized listener with the code
            //that executes all operations
            parser.addParseListener(new CustomListener());

            //Starting the parsing
            parser.prog();

            CustomListener listenerObj = new CustomListener();
            listenerObj.assembly();

        } catch (IOException e){
                e.printStackTrace();
        }
    }
}
