# Meu CEUB Digital — Agenda Acadêmica

**Disciplina:** Desenvolvimento Mobile
**Integrante:** Leticia de Souza

## Tema
Agenda Acadêmica.

## Objetivo
Ajudar o estudante do CEUB a organizar suas disciplinas e as tarefas/trabalhos de cada uma, com prazos, em um aplicativo Android simples que guarda tudo localmente.

## Estrutura das tabelas (SQLite)
**disciplinas**
| Campo | Tipo | Observação |
|---|---|---|
| id | INTEGER | Chave primária, autoincremento |
| nome | TEXT | Obrigatório |
| professor | TEXT | Obrigatório |

**tarefas**
| Campo | Tipo | Observação |
|---|---|---|
| id | INTEGER | Chave primária, autoincremento |
| titulo | TEXT | Obrigatório |
| prazo | TEXT | Obrigatório |
| disciplina_id | INTEGER | Chave estrangeira -> disciplinas(id) |

## Relacionamento
Relação 1:N — uma disciplina possui várias tarefas; cada tarefa pertence a uma única disciplina (`tarefas.disciplina_id` referencia `disciplinas.id`).

## Consulta com INNER JOIN
```sql
SELECT t.titulo, t.prazo, d.nome, d.professor
FROM tarefas t
INNER JOIN disciplinas d ON t.disciplina_id = d.id
ORDER BY t.id DESC
```

## Tecnologias
Kotlin, Android Studio, SQLite (SQLiteOpenHelper). Os dados persistem após fechar o app.
