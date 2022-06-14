package dev.teamhelios.stonepowered.console;

import dev.teamhelios.stonepowered.StonePowered;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author CloudNet
 */
public class ConsoleHandler {

    private static final String USER = System.getProperty("user.name");
    private static final String HISTORY_FILE = System.getProperty("stonepowered.history.file", "local/.consolehistory");
    private String prompt = System.getProperty("stonepowered.prompt", "&c%user%&r@&7StonePowered &f=> &r");
    private final Terminal terminal;
    private final ConsoleReadThread consoleReadThread = new ConsoleReadThread(this);
    private final LineReaderImpl lineReader;
    private StonePowered stonePowered;

    public static LineReaderImpl globalLineReader;

    public static ConsoleHandler instance;

    private final Lock printLock = new ReentrantLock(true);

    public ConsoleHandler(StonePowered stonePowered) throws Exception {
        try {
            AnsiConsole.systemInstall();
        } catch (Throwable ignored) {
        }
        this.stonePowered = stonePowered;

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
        this.lineReader.option(LineReader.Option.USE_FORWARD_SLASH, true);

        setGlobalLineReader(lineReader);

        instance = this;

        this.updatePrompt();
    }

    public void start() {
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

    public void handleConsole() {

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

    public ConsoleHandler forceWriteLine(String text) {
        this.printLock.lock();
        try {
            // ensure that the given text is formatted properly
            text = ConsoleColor.toColouredString('&', text);
            if (!text.endsWith(System.lineSeparator())) {
                text += System.lineSeparator();
            }

            this.print(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + '\r' + text + Ansi.ansi().reset().toString());
            // increases the amount of lines the running animations is off the current printed lines
        } finally {
            this.printLock.unlock();
        }

        return this;
    }

    public void stop() {
        consoleReadThread.end();
    }

    public StonePowered getStonePowered() {
        return stonePowered;
    }
}
