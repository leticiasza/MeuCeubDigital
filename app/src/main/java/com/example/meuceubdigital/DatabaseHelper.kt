package com.example.meuceubdigital

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "agenda.db", null, 1) {

    // Ativa as chaves estrangeiras
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE disciplinas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                professor TEXT NOT NULL
            )"""
        )
        db.execSQL(
            """CREATE TABLE tarefas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                prazo TEXT NOT NULL,
                disciplina_id INTEGER NOT NULL,
                FOREIGN KEY (disciplina_id) REFERENCES disciplinas(id)
            )"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tarefas")
        db.execSQL("DROP TABLE IF EXISTS disciplinas")
        onCreate(db)
    }

    // ---------- CADASTRO ----------
    fun inserirDisciplina(nome: String, professor: String): Long {
        val valores = ContentValues().apply {
            put("nome", nome)
            put("professor", professor)
        }
        return writableDatabase.insert("disciplinas", null, valores)
    }

    fun inserirTarefa(titulo: String, prazo: String, disciplinaId: Long): Long {
        val valores = ContentValues().apply {
            put("titulo", titulo)
            put("prazo", prazo)
            put("disciplina_id", disciplinaId)
        }
        return writableDatabase.insert("tarefas", null, valores)
    }

    // ---------- CONSULTAS ----------
    // Retorna pares (id, nome) para preencher o Spinner
    fun listarDisciplinas(): List<Pair<Long, String>> {
        val lista = mutableListOf<Pair<Long, String>>()
        val cursor = readableDatabase.rawQuery(
            "SELECT id, nome FROM disciplinas ORDER BY nome", null
        )
        while (cursor.moveToNext()) {
            lista.add(Pair(cursor.getLong(0), cursor.getString(1)))
        }
        cursor.close()
        return lista
    }

    // Consulta com INNER JOIN entre tarefas e disciplinas
    fun listarTarefasComDisciplina(): List<String> {
        val lista = mutableListOf<String>()
        val cursor = readableDatabase.rawQuery(
            """SELECT t.titulo, t.prazo, d.nome, d.professor
               FROM tarefas t
               INNER JOIN disciplinas d ON t.disciplina_id = d.id
               ORDER BY t.id DESC""", null
        )
        while (cursor.moveToNext()) {
            lista.add(
                "📌 ${cursor.getString(0)}\n" +
                        "Prazo: ${cursor.getString(1)}\n" +
                        "Disciplina: ${cursor.getString(2)} (Prof. ${cursor.getString(3)})"
            )
        }
        cursor.close()
        return lista
    }
}