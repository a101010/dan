
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

        if(parser.errorCount > 0){
            System.exit(parser.errorCount);
        }

        // resolve types

        for(ArrayList<TypeRef> tRefs: parser.typeRefs.values()){
            for(TypeRef tRef: tRefs){
                DanType.resolveType(tRef, parser.types);
                DanType resolvedType = tRef.getResolvedType();
                if(resolvedType == null){
                    throw new RuntimeException("unresolved typeref: " + tRef.getLongName());
                }
                else {
                    System.out.println("type " + tRef.getLongName() + " resolved as " + resolvedType.getEmittedType());
                }
            }
        }

        // ensure all TypeRefs resolved
        for(ArrayList<TypeRef> tRefs: parser.typeRefs.values()){
            for(TypeRef tRef: tRefs){
                DanType resolvedType = tRef.getResolvedType();
                if(resolvedType == null){
                    throw new RuntimeException("unresolved typeref: " + tRef.getLongName());
                }
                else {
                    System.out.println("type " + tRef.getLongName() + " resolved as " + resolvedType.getEmittedType());
                }
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