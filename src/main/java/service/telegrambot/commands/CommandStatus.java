package service.telegrambot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandStatus extends CommandsImpl {

    private final String STATUS_OK = "OK";

    @Override
    public void execute(SendMessage message, Update update) {
        Long userId = update.getMessage().getChatId();

        stringBuild();

        log.info(builder.toString());

        messageBuild(message, userId);
    }

    protected void stringBuild() {
        builder.append(STATUS_OK);
    }
}
