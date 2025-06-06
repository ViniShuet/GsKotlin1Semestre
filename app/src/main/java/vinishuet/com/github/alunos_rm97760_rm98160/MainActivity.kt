package vinishuet.com.github.alunos_rm97760_rm98160

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vinishuet.com.github.alunos_rm97760_rm98160.ui.theme.Alunos_rm97760_rm98160Theme
import android.content.Intent
import vinishuet.com.github.alunos_rm97760_rm98160.model.Evento
import vinishuet.com.github.alunos_rm97760_rm98160.viewmodel.EventoAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventoAdapter
    private val listaEventos = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nomeLocal: EditText = findViewById(R.id.nomeLocal)
        val pessoasAfetadas: EditText = findViewById(R.id.pessoas)
        val dataEvento: EditText = findViewById(R.id.data)
        val eventos: EditText = findViewById(R.id.evento)
        val incluir: Button = findViewById(R.id.incluir)
        val btnIntegrantes: Button = findViewById(R.id.Integrantes)

        val spinnerImpacto: Spinner = findViewById(R.id.spinnerImpacto)
        val adapterSpinner = ArrayAdapter.createFromResource(
            this,
            R.array.grau_impacto_opcoes,
            android.R.layout.simple_spinner_item
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerImpacto.adapter = adapterSpinner

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EventoAdapter(listaEventos) { eventoDeletar ->
            val pos = listaEventos.indexOf(eventoDeletar)
            if (pos != -1) {
                listaEventos.removeAt(pos)
                adapter.notifyItemRemoved(pos)
            }
        }
        recyclerView.adapter = adapter

        incluir.setOnClickListener {
            val nomelocal = nomeLocal.text.toString().trim()
            val impacto = spinnerImpacto.selectedItem.toString()
            val pessoasStr = pessoasAfetadas.text.toString().trim()
            val data = dataEvento.text.toString().trim()
            val eventoClimatico = eventos.text.toString().trim()

            var validacaoItem = true

            if (nomelocal.isEmpty()) {
                nomeLocal.error = "Preencha o local"
                validacaoItem = false
            } else {
                nomeLocal.error = null
            }

            if (data.isEmpty()) {
                dataEvento.error = "Preencha a data"
                validacaoItem = false
            } else {
                dataEvento.error = null
            }

            if (eventoClimatico.isEmpty()) {
                eventos.error = "Preencha o tipo de evento"
                validacaoItem = false
            } else {
                eventos.error = null
            }

            if (spinnerImpacto.selectedItemPosition == 0) {
                Toast.makeText(this, "Selecione um grau de impacto", Toast.LENGTH_SHORT).show()
                validacaoItem = false
            }

            val pessoas = pessoasStr.toIntOrNull()
            if (pessoasStr.isEmpty()) {
                pessoasAfetadas.error = "Preencha o número de pessoas"
                validacaoItem = false
            } else if (pessoas == null || pessoas <= 0) {
                pessoasAfetadas.error = "Digite um número maior que 0"
                validacaoItem = false
            } else {
                pessoasAfetadas.error = null
            }

            if (!validacaoItem) {
                Toast.makeText(this, "Corrija os campos destacados!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val evento = Evento(nomelocal, impacto, pessoas!!, data, eventoClimatico)
            listaEventos.add(evento)
            adapter.notifyItemInserted(listaEventos.size - 1)

            nomeLocal.text.clear()
            pessoasAfetadas.text.clear()
            dataEvento.text.clear()
            eventos.text.clear()
            spinnerImpacto.setSelection(0)
        }

        btnIntegrantes.setOnClickListener {
            val intent = Intent(this, IntegrantesActivity::class.java)
            startActivity(intent)
        }
    }
}