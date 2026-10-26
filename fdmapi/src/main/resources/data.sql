-- ---------------------------------------------------------------
-- Carga inicial do banco
-- Executado a cada inicialização, DEPOIS de o Hibernate criar ou
-- atualizar as tabelas (spring.jpa.defer-datasource-initialization).
-- Cada INSERT deve ser idempotente: só insere se o registro não existir.
-- ---------------------------------------------------------------

-- Gestor inicial
--   e-mail: gestor@fdm.com.br
--   senha:  Gestor@123 (gravada apenas como hash BCrypt)
--   CPF fictício, mas com dígitos verificadores válidos
INSERT INTO cadastro (nome_completo, email, cpf, telefone, ativo, senha, tipo_cadastro)
SELECT 'Gestor FDM', 'gestor@fdm.com.br', '52998224725', '11999999999', 1,
       '$2a$10$hCMJQM0dh7xpar4BkVMw5elebD7B2Ek6bQ.axejN8Ah1OWHoAYV12', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM cadastro WHERE email = 'gestor@fdm.com.br');
