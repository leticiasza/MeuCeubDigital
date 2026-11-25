package com.example.meuceubdigital

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var spinner: Spinner
    private lateinit var tvTarefas: TextView
    private var disciplinas: List<Pair<Long, String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)

        val edtNomeDisc = findViewById<EditText>(R.id.edtNomeDisciplina)
        val edtProfessor = findViewById<EditText>(R.id.edtProfessor)
        val btnDisc = findViewById<Button>(R.id.btnSalvarDisciplina)
        val edtTitulo = findViewById<EditText>(R.id.edtTitulo)
        val edtPrazo = findViewById<EditText>(R.id.edtPrazo)
        val btnTarefa = findViewById<Button>(R.id.btnSalvarTarefa)
        spinner = findViewById(R.id.spinnerDisciplinas)
        tvTarefas = findViewById(R.id.tvTarefas)

        // Salvar disciplina
        btnDisc.setOnClickListener {
            val nome = edtNomeDisc.text.toString().trim()
            val prof = edtProfessor.text.toString().trim()
            if (nome.isEmpty() || prof.isEmpty()) {
                Toast.makeText(this, "Preencha nome e professor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            db.inserirDisciplina(nome, prof)
            edtNomeDisc.text.clear()
            edtProfessor.text.clear()
            Toast.makeText(this, "Disciplina salva!", Toast.LENGTH_SHORT).show()
            carregarSpinner()
        }

        // Salvar tarefa
        btnTarefa.setOnClickListener {
            val titulo = edtTitulo.text.toString().trim()
            val prazo = edtPrazo.text.toString().trim()
            if (disciplinas.isEmpty()) {
                Toast.makeText(this, "Cadastre uma disciplina primeiro", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (titulo.isEmpty() || prazo.isEmpty()) {
                Toast.makeText(this, "Preencha título e prazo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val disciplinaId = disciplinas[spinner.selectedItemPosition].first
            db.inserirTarefa(titulo, prazo, disciplinaId)
            edtTitulo.text.clear()
            edtPrazo.text.clear()
            Toast.makeText(this, "Tarefa salva!", Toast.LENGTH_SHORT).show()
            carregarTarefas()
        }

        carregarSpinner()
        carregarTarefas()
    }

    private fun carregarSpinner() {
        disciplinas = db.listarDisciplinas()
        val nomes = disciplinas.map { it.second }
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nomes)
    }

    private fun carregarTarefas() {
        val lista = db.listarTarefasComDisciplina()
        tvTarefas.text = if (lista.isEmpty()) "Nenhuma tarefa cadastrada."
        else lista.joinToString("\n\n")
    }
}