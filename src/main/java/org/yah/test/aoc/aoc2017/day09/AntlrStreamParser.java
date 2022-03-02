package org.yah.test.aoc.aoc2017.day09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.yah.test.aoc2017.Day9Lexer;
import org.yah.test.aoc2017.Day9Parser;
import org.yah.test.aoc2017.Day9Parser.GarbageContext;
import org.yah.test.aoc2017.Day9Parser.GroupContext;
import org.yah.test.aoc2017.Day9Parser.GroupElementContext;

public class AntlrStreamParser implements StreamParser {

	@Override
	public Group parse(String input) throws IOException {
		Day9Lexer lexer = new Day9Lexer(CharStreams.fromString(input));
		Day9Parser parser = new Day9Parser(new CommonTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		Group group = createGroup(parser.group(), 1);
		if (parser.getNumberOfSyntaxErrors() > 0)
			throw new IOException("Error parsing " + input);
		return group;
	}

	private static Group createGroup(GroupContext group, int score) {
		List<GroupElementContext> elements = group.groupContent().groupElement();
		List<GroupContent> content = elements.stream()
			.map(gc -> createGroupContent(gc, score + 1))
			.collect(Collectors.toCollection(() -> new ArrayList<>(elements.size())));
		return new Group(content, score);
	}

	private static GroupContent createGroupContent(GroupElementContext element, int score) {
		GroupContext group = element.group();
		if (group != null)
			return createGroup(group, score);
		GarbageContext garbage = element.garbage();
		if (garbage != null)
			return createGarbage(garbage);
		throw new IllegalStateException("Unexpected element, missing group and garbage " + element);
	}

	private static Garbage createGarbage(GarbageContext garbage) {
		return new Garbage(garbage.garbageContent().getText());
	}

}
