package nutshell.server.repository;

import nutshell.server.domain.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {
    // Optional은 메소드의 결과가 null이 될 수 있으며, null에 의해 오류가 발생할 가능성이 매우 높을 때 반환값으로만 사용
    Optional<Token> findByRefreshToken(final String refreshToken);
    Optional<Token> findById(final Long id);
}
