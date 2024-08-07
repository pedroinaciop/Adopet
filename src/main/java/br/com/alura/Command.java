package br.com.alura;

import java.io.IOException;

public interface Command {
    void execute() throws IOException, InterruptedException;
}
