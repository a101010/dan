import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

public class DanProgTest
{
	public static void main(String [] args) throws Exception
	{
		ANTLRInputStream input = new ANTLRInputStream(System.in);

		DanProgLexer lexer = new DanProgLexer(input);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		DanProgParser parser = new DanProgParser(tokens);

		DanProgParser.prog_return result = parser.prog();

		Tree t = (Tree) result.getTree();

		System.out.println(t.toStringTree());
	}
}