
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.antlr.stringtemplate.*;
import java.io.*;
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
        // TODO Initialize the channel types


        // Parse input and build AST
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

        // resolve types

        DanType type;
        for(String typeName: parser.typeRefs.keySet()){
            ArrayList<TypeRef> tRefs = parser.typeRefs.get(typeName);
            type = parser.types.get(typeName);
            // TODO it might be a channel type or other generic type
            // TODO add handling for other generic types (and a recursive helper function or two)
            if(type == null){
                // TODO add better error handling, including the token's location
                throw new Exception("type " + typeName + " not found" );
            }
            for(TypeRef tRef: tRefs){
                tRef.setResolvedType(type);
            }
        }

        // load the template group file
        FileReader groupFileR = new FileReader("SingleThread.stg");
        StringTemplateGroup templates = new StringTemplateGroup(groupFileR);
        groupFileR.close();

        // Walk the AST
        // Create stream of tree nodes from the tree
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
        DanGen walker = new DanGen(nodes);
        walker.types = parser.types;
        walker.setTemplateLib(templates);
        DanGen.prog_return r2 = walker.prog();

        // Emit module
        StringTemplate output = (StringTemplate)r2.getTemplate();
        System.out.println(output.toString());
    }
}