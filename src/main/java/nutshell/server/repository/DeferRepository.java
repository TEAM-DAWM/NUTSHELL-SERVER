package nutshell.server.repository;

import nutshell.server.domain.Defer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeferRepository extends JpaRepository<Defer, Long> {
}