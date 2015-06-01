package net.sf.okapi.applications.lapwing.soap;

import java.util.List;

import org.languagetool.rules.RuleMatch;

public class TextMatch {

	private int line;
	private int column;
	private int endColumn;
	private int endLine;
	private int fromPos;
	private int toPos;
	private String message;
	private String ruleId;
	private List<String> replacements;

	public TextMatch() {

	}

	public TextMatch(RuleMatch match) {
		this.line = match.getLine();
		this.column = match.getColumn();
		this.endColumn = match.getEndColumn();
		this.endLine = match.getEndLine();
		this.fromPos = match.getFromPos();
		this.toPos = match.getToPos();
		this.message = match.getMessage();
		this.ruleId = match.getRule().getId();
		this.replacements = match.getSuggestedReplacements();
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public int getFromPos() {
		return fromPos;
	}

	public void setFromPos(int fromPos) {
		this.fromPos = fromPos;
	}

	public int getToPos() {
		return toPos;
	}

	public void setToPos(int toPos) {
		this.toPos = toPos;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public List<String> getReplacements() {
		return replacements;
	}

	public void setReplacements(List<String> replacements) {
		this.replacements = replacements;
	}

}
