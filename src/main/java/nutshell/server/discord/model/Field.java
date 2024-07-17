package nutshell.server.discord.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record Field (

    String name,
    String value,
    boolean inline

){}
