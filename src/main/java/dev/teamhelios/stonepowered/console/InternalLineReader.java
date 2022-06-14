package dev.teamhelios.stonepowered.console;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;

public class InternalLineReader extends LineReaderImpl {

    public InternalLineReader(Terminal terminal) {
        super(terminal, "StonePowered-Console", null);
    }

    @Override
    protected boolean historySearchBackward() {
        if (ConsoleHandler.usingMatchingHistoryComplete()) {
            return super.historySearchBackward();
        }

        if (this.history.previous()) {
            this.setBuffer(this.history.current());
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean historySearchForward() {
        if (ConsoleHandler.usingMatchingHistoryComplete()) {
            return super.historySearchForward();
        }

        if (this.history.next()) {
            this.setBuffer(this.history.current());
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean upLineOrSearch() {
        return this.historySearchBackward();
    }

    @Override
    protected boolean downLineOrSearch() {
        return this.historySearchForward();
    }

}
