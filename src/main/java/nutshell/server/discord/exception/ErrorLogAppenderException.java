package nutshell.server.discord.exception;


import nutshell.server.exception.DiscordException;
import nutshell.server.exception.code.InternalServerErrorCode;

public class ErrorLogAppenderException extends DiscordException {

    public ErrorLogAppenderException() {
        super(InternalServerErrorCode.DISCORD_LOG_APPENDER_ERROR);
    }
}
