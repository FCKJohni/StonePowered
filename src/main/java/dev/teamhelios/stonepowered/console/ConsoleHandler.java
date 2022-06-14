package dev.teamhelios.stonepowered.console;

import org.checkerframework.checker.units.qual.C;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author CloudNet
 */
public class ConsoleHandler {

    private static final String USER = System.getProperty("user.name");
    private static final String HISTORY_FILE = System.getProperty("stonepowered.history.file", "local/.consolehistory");
    private String prompt = System.getProperty("stonepowered.prompt", "&c%user%&r@&7StonePowered &f=> &r");
    private Terminal terminal;
    private ConsoleReadThread consoleReadThread = new ConsoleReadThread(this);
    private LineReaderImpl lineReader;

    public static LineReaderImpl globalLineReader;

    public ConsoleHandler() throws Exception{
        try{
            AnsiConsole.systemInstall();
        }catch (Throwable ignored){}

        this.terminal = TerminalBuilder.builder().system(true).encoding(StandardCharsets.UTF_8).build();
        this.lineReader = new InternalLineReader(this.terminal);

        this.lineReader.setAutosuggestion(LineReader.SuggestionType.COMPLETER);

        this.lineReader.option(LineReader.Option.AUTO_GROUP, false);
        this.lineReader.option(LineReader.Option.AUTO_MENU_LIST, true);
        this.lineReader.option(LineReader.Option.AUTO_FRESH_LINE, true);
        this.lineReader.option(LineReader.Option.EMPTY_WORD_OPTIONS, false);
        this.lineReader.option(LineReader.Option.HISTORY_TIMESTAMPED, false);
        this.lineReader.option(LineReader.Option.DISABLE_EVENT_EXPANSION, true);

        this.lineReader.variable(LineReader.BELL_STYLE, "none");
        this.lineReader.variable(LineReader.HISTORY_SIZE, 500);
        this.lineReader.variable(LineReader.HISTORY_FILE_SIZE, 2500);
        this.lineReader.variable(LineReader.HISTORY_FILE, Path.of(HISTORY_FILE));
        this.lineReader.variable(LineReader.COMPLETION_STYLE_LIST_BACKGROUND, "inverse");

        setGlobalLineReader(lineReader);

        this.updatePrompt();
        this.consoleReadThread.start();
    }


    private void print(String text) {
        // print out the raw given line
        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        this.lineReader.getTerminal().puts(InfoCmp.Capability.clr_eol);
        this.lineReader.getTerminal().writer().print(text);
        this.lineReader.getTerminal().writer().flush();

        // re-displays the prompt to ensure everything is lined up
        this.redisplay();
    }

    private void redisplay() {
        if (this.lineReader.isReading()) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE);
            this.lineReader.callWidget(LineReader.REDISPLAY);
        }
    }

    public void handleConsole(){

    }

    private void updatePrompt() {
        this.prompt = ConsoleColor.toColouredString('&', this.prompt)
                .replace("%user%", USER);
        this.lineReader.setPrompt(this.prompt);
    }

    public static boolean usingMatchingHistoryComplete() {
        return true;
    }

    public LineReaderImpl lineReader() {
        return lineReader;
    }

    public String prompt() {
        return prompt;
    }

    public static void setGlobalLineReader(LineReaderImpl globalLineReader) {
        ConsoleHandler.globalLineReader = globalLineReader;
    }
}
