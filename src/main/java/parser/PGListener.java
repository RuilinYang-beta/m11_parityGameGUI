// Generated from C:/Users/Tom/IdeaProjects/ParityGames/src/main/java/nl/tvandijk/pg\PG.g4 by ANTLR 4.9.1
package parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PGParser}.
 */
public interface PGListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PGParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(PGParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link PGParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(PGParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link PGParser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(PGParser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link PGParser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(PGParser.HeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link PGParser#count}.
	 * @param ctx the parse tree
	 */
	void enterCount(PGParser.CountContext ctx);
	/**
	 * Exit a parse tree produced by {@link PGParser#count}.
	 * @param ctx the parse tree
	 */
	void exitCount(PGParser.CountContext ctx);
	/**
	 * Enter a parse tree produced by {@link PGParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(PGParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link PGParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(PGParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link PGParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(PGParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PGParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(PGParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PGParser#priority}.
	 * @param ctx the parse tree
	 */
	void enterPriority(PGParser.PriorityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PGParser#priority}.
	 * @param ctx the parse tree
	 */
	void exitPriority(PGParser.PriorityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PGParser#owner}.
	 * @param ctx the parse tree
	 */
	void enterOwner(PGParser.OwnerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PGParser#owner}.
	 * @param ctx the parse tree
	 */
	void exitOwner(PGParser.OwnerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PGParser#edge}.
	 * @param ctx the parse tree
	 */
	void enterEdge(PGParser.EdgeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PGParser#edge}.
	 * @param ctx the parse tree
	 */
	void exitEdge(PGParser.EdgeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PGParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(PGParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PGParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(PGParser.LabelContext ctx);
}