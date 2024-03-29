
import dan.system.TemplateGroupManager;
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
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

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
        // TODO error handling
        String inputFileName = args[0];
        System.out.println();
        System.out.println("compiling " + inputFileName);
        System.out.println();
        String danExtension = ".dan";
        String inputFileStem = inputFileName.substring(0, inputFileName.length() - danExtension.length());
        ANTLRFileStream input = new ANTLRFileStream(inputFileName);

        //ANTLRInputStream input = new ANTLRInputStream(System.in);

        DanProgLexer lexer = new DanProgLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        DanProgParser parser = new DanProgParser(tokens);
        
        
        // Parse input and build AST
        DanProgParser.prog_return result = parser.prog();

        Tree t = (Tree) result.getTree();

        //System.out.println(t.toStringTree());
        RuleBasedCollator en_USCollator = (RuleBasedCollator)
            Collator.getInstance(new Locale("en", "US", ""));

        TreeSet<String> typeNames = new TreeSet<String>(en_USCollator);
        typeNames.addAll(parser.types.keySet());
        String types = "";
        for(String typeName : typeNames){
            types += "\n" + typeName;
        }
        
        //System.out.println(types + "\n");
        
        System.out.println("Number of errors: " + parser.errorCount);

        if(parser.errorCount > 0){
            System.exit(parser.errorCount);
        }

        // TODO everything after this should be moved to back_end

        // get imported libraries
        // remove duplicate library names
        ArrayList<String> importLibs = new ArrayList<String>();
        for(String libName : parser.importLibs){
            boolean foundDup = false;
            for(String dupName : importLibs){
                if(libName.equals(dupName)){
                    foundDup = true;
                    break;
                }
            }
            if(!foundDup){
                importLibs.add(libName);
            }
        }

        // get the type map from each imported library
        for(String libName : importLibs){
            HashMap<String, DanType> recoveredMap = null;
            try{
                // TODO use search path before the current dir
                // TODO add a bit of error checking
                FileInputStream in = new FileInputStream(libName + ".danti");
                ObjectInputStream rs = new ObjectInputStream(in);
                recoveredMap = (HashMap<String, DanType>) rs.readObject();
                parser.types.putAll(recoveredMap);
            } catch (Exception ex){
                Logger.getLogger(DanProgTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // resolve types

        for(ArrayList<TypeRef> tRefs: parser.typeRefs.values()){
            for(TypeRef tRef: tRefs){
                DanType.resolveType(tRef, parser.types);
                DanType resolvedType = tRef.getResolvedType();
                if(resolvedType == null){
                    throw new RuntimeException("unresolved typeref: " + tRef.getLongName());
                }
                //else {
                //    System.out.println("type " + tRef.getLongName() + " resolved as " + resolvedType.getEmittedType());
                //}
            }
        }

        // ensure all TypeRefs resolved
        for(ArrayList<TypeRef> tRefs: parser.typeRefs.values()){
            for(TypeRef tRef: tRefs){
                DanType resolvedType = tRef.getResolvedType();
                if(resolvedType == null){
                    throw new RuntimeException("unresolved typeref: " + tRef.getLongName());
                }
                //else {
                //    System.out.println("type " + tRef.getLongName() + " resolved as " + resolvedType.getEmittedType());
                //}
            }
        }


        // Emit the type map
        // TODO we want to only emit the type map for the .dan file we compiled
        //      right now this also includes all library types
        FileOutputStream f = null;
        try {
            String dantiFileName = inputFileStem + ".danti";
            f = new FileOutputStream(dantiFileName);
            ObjectOutput s = null;
            s = new ObjectOutputStream(f);
            s.writeObject(parser.types);
            //s.flush();
            s.close();
        } catch (Exception ex) {
            Logger.getLogger(DanProgTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // load the template group file
        File groupFile = new File(installDir.getCanonicalPath(), "SingleThread.stg");
        FileReader groupFileR = new FileReader(groupFile);
        StringTemplateGroup templates = new StringTemplateGroup(groupFileR);
        groupFileR.close();
        TemplateGroupManager.setTemplateLib(templates);

        // create the list of libraries included
        ArrayList<StringTemplate> imports = new ArrayList<StringTemplate>();
        for(String libName : importLibs){
            StringTemplate st = templates.getInstanceOf("include", new STAttrMap().put("fileStem", libName));
            imports.add(st);
        }

        // Walk the AST to generate header
        // Create stream of tree nodes from the tree
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
        DanGenH headerWalker = new DanGenH(nodes);
        headerWalker.types = parser.types;
        headerWalker.importLibs = imports;
        headerWalker.includeGuard = inputFileStem.toUpperCase() + "_H_";
        headerWalker.setTemplateLib(templates);
        DanGenH.prog_return headerReturn = headerWalker.prog();

        // Emit header
        StringTemplate headerOutput = (StringTemplate)headerReturn.getTemplate();
        String hFileName = inputFileStem + ".h";
        FileWriter hWriter = new FileWriter(hFileName);
        hWriter.write(headerOutput.toString());
        hWriter.close();

        // Walk the AST to generate source
        // Create stream of tree nodes from the tree
        nodes = new CommonTreeNodeStream(t);
        DanGen walker = new DanGen(nodes);
        walker.types = parser.types;
        walker.inputFileStem = inputFileStem;
        walker.setTemplateLib(templates);
        DanGen.prog_return r2 = walker.prog();

        // Emit source
        String cFileName = inputFileStem + ".c";
        StringTemplate output = (StringTemplate)r2.getTemplate();
        FileWriter cWriter = new FileWriter(cFileName);
        cWriter.write(output.toString());
        cWriter.close();
    }

    /** allows convenient multi-value initialization:
     *  "new STAttrMap().put(...).put(...)"
     */
    public static class STAttrMap extends HashMap {
      public STAttrMap put(String attrName, Object value) {
        super.put(attrName, value);
        return this;
      }
      public STAttrMap put(String attrName, int value) {
        super.put(attrName, new Integer(value));
        return this;
      }
    }
}