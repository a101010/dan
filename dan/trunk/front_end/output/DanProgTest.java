
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.antlr.stringtemplate.*;
import java.io.*;
import java.io.File;
import dan.types.*;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Alan
 */
public class DanProgTest {

    public static void main(String[] args) throws Exception {
        String installPath = System.getenv("DANGER_INSTALL_DIR");
        File installDir = new File(installPath);
        System.out.println("DANGER_INSTALL_DIR: " + installDir.getCanonicalPath());
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


        // Emit the typ map

        // write the objects
        FileOutputStream f = null;
        try {
            // TODO need to generate .danti with proper filename instead of 'output'
            f = new FileOutputStream("output.danti");
            ObjectOutput s = null;
            s = new ObjectOutputStream(f);
            s.writeObject(parser.types);
            //s.flush();
            s.close();
        } catch (Exception ex) {
            Logger.getLogger(DanProgTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    


        // read the objects TODO remove; this is test code
        HashMap<String, DanType> recoveredMap = null;
        try{
            FileInputStream in = new FileInputStream("output.danti");
            ObjectInputStream rs = new ObjectInputStream(in);
            recoveredMap = (HashMap<String, DanType>) rs.readObject();
            System.out.println("type map has been deserialized");
        } catch (Exception ex){
            Logger.getLogger(DanProgTest.class.getName()).log(Level.SEVERE, null, ex);
        }







        // load the template group file
        File groupFile = new File(installDir.getCanonicalPath(), "SingleThread.stg");
        FileReader groupFileR = new FileReader(groupFile);
        StringTemplateGroup templates = new StringTemplateGroup(groupFileR);
        groupFileR.close();

        // Walk the AST to generate header
        // Create stream of tree nodes from the tree
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
        DanGenH headerWalker = new DanGenH(nodes);
        headerWalker.types = parser.types;
        headerWalker.setTemplateLib(templates);
        DanGenH.prog_return headerReturn = headerWalker.prog();







        // Emit header
        StringTemplate headerOutput = (StringTemplate)headerReturn.getTemplate();
        // TODO emit a file
        System.out.println(headerOutput.toString());
        System.out.println("// end of header \n\n\n\n");

        // Walk the AST to generate source
        // Create stream of tree nodes from the tree
        nodes = new CommonTreeNodeStream(t);
        DanGen walker = new DanGen(nodes);
        walker.types = parser.types;
        walker.setTemplateLib(templates);
        DanGen.prog_return r2 = walker.prog();

        // Emit source
        StringTemplate output = (StringTemplate)r2.getTemplate();
        System.out.println(output.toString());
    }
}