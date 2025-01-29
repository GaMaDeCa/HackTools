package GaMaDeCa.code.obfuscator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.text.Html;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;

/*
	Obfuscador - Gabriel Matheus de Castro (13/07/2022)
	Separei os obfuscadores em classes como PythonObfuscator e usei a classe Utils como utilidades gerais para obfuscação.
*/
public class MainActivity extends Activity 
{
	Button obfuscar,limpar,limpar2;
	EditText et_input,et_output;
	TextView crdts;
	//Essa é uma versão simples de obfuscador, a segurança do código é mais contra quem não sabe programar,
	//quem sabe pode facilmente quebrá-la através de uma simples análise, mas no futuro ela poderá ser melhorada.
	//TODO>Deixar o usuário escolher o tipo de obfuscação desejada.
	//	  >Adicionar validador de código na própria obfuscação.
	//	  >Adicionar suporte a outras linguagens(Java).
	//	  >Adicionar mais codificações(rot47,base64crypto,hexadecimal,etc).
	//	  >Criar opção de obfuscação com senha.
	//	  >Corrigir o comportamento do scroll do EditText.
	PythonObfuscator obfuscadorPython;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		getActionBar().setTitle(Html.fromHtml("<font color=\"#FF0000\">"+getString(R.string.action_bar_name)+"</font>"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		obfuscar=findViewById(R.id.btOB);
		limpar=findViewById(R.id.btL1);
		limpar2=findViewById(R.id.btL2);
		
		et_input=findViewById(R.id.etIn);
		et_output=findViewById(R.id.etOut);
		
		crdts=findViewById(R.id.credits);
		
		obfuscar.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View n) {
				String entrada=et_input.getText().toString();
				et_output.setText(!entrada.equals("")?obfuscadorPython.Obfuscar(entrada,"UTF-8"):"");
			}
		});
		limpar.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View n) {
				et_input.setText("");
			}
		});
		limpar2.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View n) {
				et_output.setText("");
			}
		});
		credits();
    }
	public void credits() {
		crdts.setMovementMethod(LinkMovementMethod.getInstance());
		crdts.setText(Html.fromHtml("<h1>"
					 +getString(R.string.cr)+":</h1> - "
					 +getString(R.string.ed)+"<br><a href=\"https://github.com/Alien8652\">github.com/Alien8652</a><br><br> - "
					 +getString(R.string.it)+"<br><a href=\"https://www.flaticon.com/br/icones-gratis/talisma\" title=\"talismã ícones\">"
					 +getString(R.string.s)+"</a>"));
	}
}
