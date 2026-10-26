package fdmapi.comum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Disponibiliza o codificador de senhas para injeção (ex.: no CadastroService).
 * Quando o Spring Security completo entrar (UC04), ele reutilizará este bean.
 */
@Configuration
public class CriptografiaConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
