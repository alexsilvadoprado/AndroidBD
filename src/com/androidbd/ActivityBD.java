package com.androidbd;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 
 * Classe responsável por instanciar um banco de dados SQLite
 *
 * @author Álex Prado <alexsilvadoprado@gmail.com>
 * @since 04/11/2014 20:11:17
 * @version 1.0
 */
class BancoDados extends SQLiteOpenHelper
{
	public BancoDados(Context contexto)
	{
		super(contexto, "RegistroViagens", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE viagem (_id INTEGER PRIMARY KEY," +
								" destino TEXT, tipo_viagem INTEGER," +
								" data_chegada DATE, data_saida DATE," +
								" orcamento DOUBLE, quantidade_pessoas INTEGER);");
		db.execSQL("CREATE TABLE gosto (_id INTEGER PRIMARY KEY," +
								" categoria TEXT, data DATE, valor DOUBLE," +
								" descricao TEXT, local TEXT, viagem_id INTEGER," +
								" FOREIGN KEY (viagem_id) REFERENCES viagem(_id));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
}

/**
 * 
 * Classe responsável por reresentar a Activity responsável pelo controle
 * da tela layout_activity_bd
 *
 * @author Álex Prado <alexsilvadoprado@gmail.com>
 * @since 04/11/2014 20:12:12
 * @version 1.0
 */
public class ActivityBD extends Activity 
{
	private BancoDados banco = null;
	private EditText edtxtDestino = null;
	private EditText edtxtQtdPessoas = null;
	private EditText edtxtOrcamento = null;
	private RadioGroup rbtgTipoViagem = null;
	private Button btnSalvar = null;
	private Calendar datas = null;
	int dia = 0, mes = 0, ano = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_bd);
        
        //Validando variáveis de referência
        edtxtDestino = (EditText) findViewById(R.id.edtxtDestino);
        edtxtQtdPessoas = (EditText) findViewById(R.id.edtxtQtd);
        edtxtOrcamento = (EditText) findViewById(R.id.edtxtOrcamento);
        
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        
        rbtgTipoViagem = (RadioGroup) findViewById(R.id.rbtgTipoViagem);
        
        datas = Calendar.getInstance();
        
        dia = datas.get(Calendar.DAY_OF_MONTH);
        mes = datas.get(Calendar.MONTH);
        ano = datas.get(Calendar.YEAR);
        
        banco = new BancoDados(this);
    }
    
    /**
     * 
     * Método responsável por tratar o evento do botão Salvar
     * armazenando os dados do formulário no banco
     * 
     * @param view
     * @author Álex Prado <alexsilvadoprado@gmail.com>
     * @since 04/11/2014 21:21:47
     * @version 1.0
     */
    public void salvarDados(View view)
    {
    	//Obtem o objeto de escrita no banco de dados
    	SQLiteDatabase bd = banco.getWritableDatabase();
    	
    	//Cria um pacote para escrita de valores
    	ContentValues pacote = new ContentValues();
    	
    	//Insere os valores do formulário no pacote
    	pacote.put("destino", edtxtDestino.getText().toString());
    	pacote.put("data_chegada", datas.getTimeInMillis());
    	pacote.put("data_saida", datas.getTimeInMillis());
    	pacote.put("orcamento", edtxtOrcamento.getText().toString());
    	pacote.put("quantidade_pessoas", edtxtQtdPessoas.getText().toString());
    	
    	//Verifica qual dos radiobuttons está selecionado
    	int tipo = (rbtgTipoViagem.getCheckedRadioButtonId() == R.id.rbtNegocios) ? 0 : 1;
    	pacote.put("tipo_viagem", tipo);
    	
    	//Insere no banco e armazena o resultado da operação
    	long resultado = bd.insert("viagem", null, pacote);
    	
    	//Testa o resultado daoperação
    	if(resultado != -1)
    	{
    		Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    		Toast.makeText(this, "Deu pau juvenal", Toast.LENGTH_LONG).show();
    	}
    }
    
    public void onDestroy()
    {
    	super.onDestroy();
    	banco.close();
    }
}
