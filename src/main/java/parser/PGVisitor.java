// Generated from C:/Users/Tom/IdeaProjects/ParityGames/src/main/java/nl/tvandijk/pg\PG.g4 by ANTLR 4.9.1
package parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PGParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PGVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PGParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(PGParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link PGParser#header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader(PGParser.HeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link PGParser#count}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCount(PGParser.CountContext ctx);
	/**
	 * Visit a parse tree produced by {@link PGParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(PGParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link PGParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(PGParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link PGParser#priority}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPriority(PGParser.PriorityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PGParser#owner}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwner(PGParser.OwnerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PGParser#edge}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEdge(PGParser.EdgeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PGParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(PGParser.LabelContext ctx);
}