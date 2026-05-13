INSERT INTO tb_user (name, email, password) VALUES ('Lucas Andrade', 'lucas@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (name, email, password) VALUES ('Alex Novaes', 'alex@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

INSERT INTO tb_role (authority) VALUES ('ROLE_EMPLOYEE');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);

INSERT INTO tb_tasks (titulo, descricao, status, prioridade, criado, atualizado) VALUES ('Estudar Spring Boot', 'Revisar controllers, services e repositories do projeto.', 'PENDENTE', 'ALTA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_tasks (titulo, descricao, status, prioridade, criado, atualizado) VALUES ('Criar tela de tarefas', 'Implementar a listagem de tarefas no frontend.', 'PENDENTE', 'MEDIA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_tasks (titulo, descricao, status, prioridade, criado, atualizado) VALUES ('Corrigir validações', 'Ajustar mensagens de erro e validações dos DTOs.', 'PENDENTE', 'URGENTE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);