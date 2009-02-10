
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.io.File;
import dan.types.*;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.*;

/**
 * 
 * @author Alan
 */
public class DanProgTest {

    public static void main(String[] args) throws Exception {
        File workingDir = new File(".");
        System.out.println("Starting dan in: " + workingDir.getCanonicalPath());

        //File inputFile = new File(args[0]);
        ANTLRFileStream input = new ANTLRFileStream(args[0]);

        //ANTLRInputStream input = new ANTLRInputStream(System.in);

        DanProgLexer lexer = new DanProgLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        DanProgParser parser = new DanProgParser(tokens);
        
        // Initialize the builtin types
        for(BuiltinType.Builtins b : BuiltinType.Builtins.values()){
            // duplicate for upper and lower case
            BuiltinType bType = new BuiltinType(b);
            parser.types.put(b.name(), bType);
            parser.types.put(b.name().toLowerCase(), bType);
        }

        DanProgParser.prog_return result = parser.prog();

        Tree t = (Tree) result.getTree();

        System.out.println(t.toStringTree());
        RuleBasedCollator en_USCollator = (RuleBasedCollator)
            Collator.getInstance(new Locale("en", "US", ""));

        TreeSet<String> typeNames = new TreeSet<String>(en_USCollator);
        typeNames.addAll(parser.types.keySet());
        String types = "";
        for(String typeName : typeNames){
            types += "\n" + typeName;
        }
        
        System.out.println(types + "\n");
        
        System.out.println("Number of errors: " + parser.errorCount);
    }
}